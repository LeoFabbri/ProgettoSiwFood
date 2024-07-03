package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import it.uniroma3.siw.controller.validator.IngredientValidator;
import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.repository.ChefRepository;
import it.uniroma3.siw.repository.RecipeRepository;
import it.uniroma3.siw.service.IngredientService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private IngredientValidator ingredientValidator;

    @Autowired
    private RecipeRepository recipeRepository;

    @GetMapping("/formNewIngredient")
    public String getFormNewIngredient(Model model) {
        Ingredient ingredient = new Ingredient();
        model.addAttribute("ingredient", ingredient);
        return "formNewIngredient.html";
    }

    @PostMapping("newIngredient/ingredient")
    public String newIngredient(@Valid @ModelAttribute("ingredient") Ingredient ingredient, BindingResult bindingResult) {
        this.ingredientValidator.validate(ingredient, bindingResult);
        if(!bindingResult.hasErrors()){
            ingredientService.save(ingredient);
            return "redirect:/";
        }else{
            return "redirect:/formNewIngredient";
        }
    }

    @GetMapping("/admin/deleteIngredients")
    public String getDeleteIngredients(Model model) {
        model.addAttribute("ingredients", this.ingredientService.findAll());
        return "admin/adminDeleteIngredients.html";
    }

    @GetMapping("/admin/deleteIngredients/{id}")
    public String deleteIngredient(@PathVariable("id") Long id, Model model){
        for(Recipe r : this.recipeRepository.findByIngredient(this.ingredientService.findById(id))){
            r.getIngredients().remove(this.ingredientService.findById(id));
        }
        this.ingredientService.deleteById(id);
        return "redirect:/admin/deleteIngredients";
    }
    
    @GetMapping("/admin/updateIngredients")
    public String getUpdateIngredients(Model model) {
        model.addAttribute("ingredients", this.ingredientService.findAll());
        return "admin/adminUpdateIngredients.html";
    }
    
    @GetMapping("/admin/formUpdateIngredient/{id}")
    public String getFormUpdateIngredient(@PathVariable("id") Long id, Model model) {
        model.addAttribute("newIngredient", this.ingredientService.findById(id));
        model.addAttribute("ingredientID", id);
        return "admin/adminFormUpdateIngredient.html";
    }

    @PostMapping("/admin/updateIngredient/{id}/newIngredient")
    public String updateIngredient(@Valid @ModelAttribute("newIngredient") Ingredient newIngredient, @ModelAttribute("id") Long id, Model model) {
        newIngredient.setId(id);
        this.ingredientService.save(newIngredient);
        return "redirect:/admin/updateIngredients";
    }    
    
}
