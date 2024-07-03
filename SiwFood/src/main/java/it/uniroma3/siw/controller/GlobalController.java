package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw.repository.CredentialsRepository;

@ControllerAdvice
public class GlobalController {

    @Autowired
    private CredentialsRepository credentialsRepository;

    @ModelAttribute("userID")
    public Long getUserId(){
        UserDetails user = null;
        Long userId = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userId = credentialsRepository.findByUsername(user.getUsername()).get().getId();
        }
        return userId;
    }

    @ModelAttribute("userDetails")
    public UserDetails getUser(){
        UserDetails user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return user;
    }

}
