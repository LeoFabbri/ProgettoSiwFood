package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.repository.IngredientRepository;

@Service
public class IngredientService {
    
    @Autowired
    private IngredientRepository ingredientRepository;

    public Ingredient findById(Long id){
        return this.ingredientRepository.findById(id).get();
    }

    public Iterable<Ingredient> findAll(){
        return this.ingredientRepository.findAll();
    }

    public Ingredient save(Ingredient ingredient){
        return this.ingredientRepository.save(ingredient);
    }

    public void deleteById(Long id){
        this.ingredientRepository.deleteById(id);
    }

    public boolean existsByNameAndQuantityAndUnitaDiMisura(Ingredient ingredient){
        return this.ingredientRepository.existsByNameAndQuantityAndUnitaDiMisura(ingredient.getName(), ingredient.getQuantity(), ingredient.getUnitaDiMisura());
    }

    public Ingredient findByNameAndQuantity(Ingredient ingredient){
        return this.ingredientRepository.findByNameAndQuantity(ingredient.getName(), ingredient.getQuantity());
    }

}
