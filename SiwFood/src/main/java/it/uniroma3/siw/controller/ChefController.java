package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.ChefValidator;
import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.service.ChefService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;



@Controller
public class ChefController {

    @Autowired
    private ChefService chefService;

    @Autowired
    private ChefValidator chefValidator;

    @Autowired
    private CredentialsRepository credentialsRepository;

    @GetMapping("/chefs")
    public String getAllChefs(Model model){
        model.addAttribute("chefs", this.chefService.findAll());
        return "chefs.html";
    }

    @GetMapping("/chefs/{id}")
    public String getChef(@PathVariable("id") Long id, Model model){
        model.addAttribute("chef", this.chefService.findById(id));
        return "chef.html";
    }

    @GetMapping("/admin/formNewChef")
    public String getFormNewChef(Model model){
        model.addAttribute("chef", new Chef());
        return "admin/adminFormNewChef.html";
    }

    @PostMapping("/admin/newChef/chef")
    public String newChef(@Valid @ModelAttribute("chef") Chef chef, @RequestParam("image") MultipartFile file, Model model, BindingResult bindingResult){
        try{
            this.chefValidator.validate(chef, bindingResult);
            if(!bindingResult.hasErrors()){
                byte[] byteFoto = file.getBytes();
                chef.setBase64(Base64.getEncoder().encodeToString(byteFoto));
                this.chefService.saveChef(chef);
                return "redirect:/chefs/"+chef.getId();
            }else{
                model.addAttribute("error", "Chef already exists");
                return "admin/adminFormNewChef.html";
            }
        }catch(IOException e){
            model.addAttribute("error", "Error uploading image");
            return "admin/adminFormNewChef.html";
        }
    }

    @GetMapping("/admin/deleteChefs")
    public String getDeleteChefs(Model model){
        model.addAttribute("chefs", this.chefService.findAll());
        return "admin/adminDeleteChefs.html";
    }

    @GetMapping("/admin/deleteChefs/{id}")
    public String deleteChefs(@PathVariable("id") Long id){
        Chef c = this.chefService.findById(id);
        Credentials cr = this.credentialsRepository.findByChef(c);
        if(cr!=null){
            cr.setChef(null);
            this.credentialsRepository.delete(cr);
        }
        this.chefService.deleteById(id);
        return "redirect:/admin/deleteChefs";
    }

    @GetMapping("/admin/updateChefs")
    public String getUpdateChef(Model model) {
        model.addAttribute("chefs", this.chefService.findAll());
        return "admin/adminUpdateChef.html";
    }

    @GetMapping("/admin/formUpdateChef/{id}")
    public String getFormUpdateChef(@PathVariable("id") Long id, Model model) {
        model.addAttribute("newChef", this.chefService.findById(id));
        model.addAttribute("chefID", id);
        return "admin/adminFormUpdateChef.html";
    }

    @PostMapping("/admin/updateChef/{id}/newChef")
    public String updateChef(@PathVariable("id") Long id, @RequestParam(name = "image",required = false) MultipartFile file, @ModelAttribute("newChef") Chef newChef, Model model) {
        try{
            if(file!=null){
                byte[] byteFoto = file.getBytes();
                newChef.setBase64(Base64.getEncoder().encodeToString(byteFoto));
            }else{
                newChef.setBase64(this.chefService.findById(id).getBase64());
            }
            newChef.setId(id);
            this.chefService.saveChef(newChef);
            return "redirect:/admin/updateChefs";
        }catch(IOException e){
            model.addAttribute("error", "Error uploading image");
            return "admin/adminFormUpdateChef.html";
        }
    }

    @GetMapping("/admin/updateChef/removeImg/{id}")
    public String removeImg(@PathVariable("id") Long id, Model model) {
        Chef chef = this.chefService.findById(id);
        chef.setBase64(null);
        this.chefService.saveChef(chef);
        return "redirect:/admin/formUpdateChef/"+chef.getId();
    }
    
    
}
