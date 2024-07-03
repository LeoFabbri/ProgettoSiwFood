package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.service.IngredientService;

@RestController
public class IngredientRestController {

    @Autowired
    private IngredientService ingredientService;

    @GetMapping("/rest/ingredients")
    public List<Ingredient> getIngredients(){
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        for(Ingredient i : this.ingredientService.findAll()){
            ingredients.add(i);
        }
        return ingredients;
    }

    @GetMapping("/rest/ingredients/{id}")
    public Ingredient getIngredient(@PathVariable("id") Long id){
        return this.ingredientService.findById(id);
    }

}
