package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.service.ChefService;

@RestController
public class ChefRestController {

    @Autowired
    private ChefService ChefService;

    @GetMapping("/rest/chefs")
    public List<Chef> getChefs(){
        List<Chef> chefs = new ArrayList<Chef>();
        for(Chef c : this.ChefService.findAll()){
            chefs.add(c);
        }
        return chefs;
    }

    @GetMapping("/rest/chefs/{id}")
    public Chef getChef(@PathVariable("id") Long id){
        return this.ChefService.findById(id);
    }

}
