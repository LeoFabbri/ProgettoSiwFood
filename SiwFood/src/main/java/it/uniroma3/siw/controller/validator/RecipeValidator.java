package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.repository.IngredientRepository;
import it.uniroma3.siw.repository.RecipeRepository;

@Component
public class RecipeValidator implements Validator{

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientValidator ingredientValidator;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Recipe.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Recipe recipe = (Recipe) target;
        if(recipe.getName()!=null && recipe.getChef()!=null && recipeRepository.existsByNameAndChef(recipe.getName(),recipe.getChef())){
            errors.reject("recipe.duplicate");
        }else{
            for(Ingredient i : recipe.getIngredients()){
                if(ingredientRepository.existsByNameAndQuantityAndUnitaDiMisura(i.getName(), i.getQuantity(), i.getUnitaDiMisura())){
                    i.setId(ingredientRepository.findByNameAndQuantity(i.getName(), i.getQuantity()).getId());
                }else{
                    this.ingredientRepository.save(i);
                }
            }
        }
    }
    
}
