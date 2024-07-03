package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Ingredient;
import java.util.List;


public interface IngredientRepository extends CrudRepository<Ingredient,Long>{

    public boolean existsByNameAndQuantityAndUnitaDiMisura(String name, Integer quantity, String unitaDiMisura);

    public Ingredient findByNameAndQuantity(String name, Integer quantity);
    
}
