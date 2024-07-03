package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.service.RecipeService;

@RestController
public class RecipeRestController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/rest/recipes")
    public List<Recipe> getRecipes(){
        List<Recipe> recipes = new ArrayList<Recipe>();
        for(Recipe r : this.recipeService.findAll()){
            recipes.add(r);
        }
        return recipes;
    }

    @GetMapping("/rest/recipes/{id}")
    public Recipe getRecipe(@PathVariable("id") Long id){
        return this.recipeService.findById(id);
    }

}
