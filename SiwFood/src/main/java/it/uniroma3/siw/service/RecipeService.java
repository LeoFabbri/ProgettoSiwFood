package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.repository.RecipeRepository;
import jakarta.transaction.Transactional;

@Service
public class RecipeService {
    
    @Autowired
    private RecipeRepository recipeRepository;

    public Recipe findById(Long id){
        return recipeRepository.findById(id).get();
    }

    public void deleteById(Long id){
        this.recipeRepository.deleteById(id);
    }

    public Iterable<Recipe> findAll(){
        return recipeRepository.findAll();
    }

    public Recipe save(Recipe recipe){
        return this.recipeRepository.save(recipe);
    }

    public List<Recipe> findAllByChef(Chef chef){
        return this.recipeRepository.findAllByChef(chef);
    }

    public boolean existsByNameAndChef(String name, Chef chef){
        return this.existsByNameAndChef(name,chef);
    }

}
