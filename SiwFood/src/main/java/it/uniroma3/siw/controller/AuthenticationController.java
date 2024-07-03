package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw.controller.validator.ChefValidator;
import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.service.ChefService;
import it.uniroma3.siw.service.CredentialsService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class AuthenticationController {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private ChefService chefService;

    @Autowired
    private ChefValidator chefValidator;

    @GetMapping("/home")
    public String getHome(){
        return "home.html";
    }

    @GetMapping("/login")
    public String getFormLogin() {
        return "formLogin.html";
    }
    

    @GetMapping("/")
    public String getIndex(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
	        return "index.html";
		}else{

            UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals("ADMIN")) {
				return "admin/adminHome.html";
			}else{
                return "chefHome.html";
            }

        }
    }
    
    @GetMapping("/success")
    public String getSuccessPage(Model model) {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
    	if (credentials.getRole().equals("ADMIN")) {
            return "admin/adminHome.html";
        }
        return "chefHome.html";
    }
    
    @GetMapping("/register") 
	public String showRegisterForm (Model model) {
		model.addAttribute("chef", new Chef());
		model.addAttribute("credentials", new Credentials());
		return "formRegisterUser.html";
	}

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("chef") Chef chef, @Valid @ModelAttribute("credentials") Credentials credentials, Model model, BindingResult bindingResult) {

            this.chefValidator.validate(chef, bindingResult);
            if(bindingResult.hasErrors()){
                model.addAttribute("chef", new Chef());
		        model.addAttribute("credentials", new Credentials());
                model.addAttribute("error", "This chef already exists");
                return "formRegisterUser.html";
            }
            chefService.saveChef(chef);
            credentials.setChef(chef);
            credentialsService.saveCredentials(credentials);
            return "redirect:/login";

    }

}
