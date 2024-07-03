package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.repository.IngredientRepository;

@Component
public class IngredientValidator implements Validator{

    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Ingredient.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Ingredient ingredient = (Ingredient) target;
        if(ingredient.getName()!=null && ingredient.getQuantity()!=null && ingredient.getUnitaDiMisura()!=null && ingredientRepository.existsByNameAndQuantityAndUnitaDiMisura(ingredient.getName(),ingredient.getQuantity(),ingredient.getUnitaDiMisura())){
            errors.reject("Ingredient.duplicate");
        }
    }

}
