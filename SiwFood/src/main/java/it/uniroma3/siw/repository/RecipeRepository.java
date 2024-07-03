package it.uniroma3.siw.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.model.Recipe;
import java.util.List;


public interface RecipeRepository extends CrudRepository<Recipe,Long>{

    public List<Recipe> findAllByChef(Chef chef);

    public boolean existsByNameAndChef(String name, Chef chef);

    @Query("select r from Recipe r where :i member of r.ingredients")
    public List<Recipe> findByIngredient(Ingredient i);

}
