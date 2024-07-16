package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.hibernate.engine.jdbc.mutation.spi.BindingGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.controller.validator.IngredientValidator;
import it.uniroma3.siw.controller.validator.RecipeValidator;
import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.repository.ChefRepository;
import it.uniroma3.siw.repository.IngredientRepository;
import it.uniroma3.siw.service.IngredientService;
import it.uniroma3.siw.service.RecipeService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class RecipeController {
    
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeValidator recipeValidator;

    @Autowired
    private ChefRepository chefRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private IngredientValidator ingredientValidator;

    @Autowired
    private IngredientService ingredientService;

    @GetMapping("/recipes")
    public String getAllRecipes(Model model){
        model.addAttribute("recipes", this.recipeService.findAll());
        return "recipes.html";
    }

    @GetMapping("/recipes/{id}")
    public String getRecipe(@PathVariable("id") Long id, Model model){
        model.addAttribute("recipe", this.recipeService.findById(id));
        return "recipe.html";
    }

    @GetMapping("/formNewRecipe")
    public String getFormNewRecipe(Model model) {
        model.addAttribute("recipe", new Recipe());
        return "formNewRecipe.html";
    }

    @PostMapping("/newRecipe/recipe")
    public String newRecipe(@ModelAttribute("recipe") Recipe recipe, @RequestParam("image") List<MultipartFile> file, Model model, BindingResult bindingResult){
        try{
            recipe.setChef(this.chefRepository.findById((Long)model.getAttribute("userID")).get());
            this.recipeValidator.validate(recipe, bindingResult);
            if(bindingResult.hasErrors()){
                model.addAttribute("error", "Recipe already exists");
                return "redirect:/formNewRecipe";
            }else{
                List<String> bytes = new ArrayList<String>();
                for(MultipartFile f : file){
                    byte[] byteFoto = f.getBytes();
                    bytes.add(Base64.getEncoder().encodeToString(byteFoto));
                }
                recipe.setBase64(bytes);
                this.recipeService.save(recipe);
                model.addAttribute("recipe", recipe);
                return "redirect:/recipes/"+recipe.getId();
            }
        }catch(IOException e){
            model.addAttribute("error", "Error uploading image");
            return "formNewRecipe.html";
        }
    }
    
    @GetMapping("/deleteRecipes")
    public String getDeleteRecipes(Model model) {
        model.addAttribute("recipes", this.recipeService.findAllByChef(this.chefRepository.findById((Long)model.getAttribute("userID")).get()));
        return "deleteRecipes.html";
    }
    
    @GetMapping("/deleteRecipes/{id}")
    public String deleteRecipe(@PathVariable("id") Long id) {
        this.recipeService.deleteById(id);
        return "redirect:/deleteRecipes";
    }

    @GetMapping("/admin/deleteRecipes")
    public String getAdminDeleteRecipes(Model model) {
        model.addAttribute("recipes", this.recipeService.findAll());
        return "admin/adminDeleteRecipes.html";
    }

    @GetMapping("/admin/deleteRecipes/{id}")
    public String adminDeleteRecipe(@PathVariable("id") Long id) {
        this.recipeService.deleteById(id);
        return "redirect:/admin/deleteRecipes";
    }
    
    
    @GetMapping("/updateRecipes")
    public String getUpdateRecipes(Model model) {
        model.addAttribute("recipes", this.recipeService.findAllByChef(this.chefRepository.findById((Long)model.getAttribute("userID")).get()));
        return "updateRecipes.html";
    }

    @GetMapping("/formUpdateRecipes/{id}")
    public String getFormUpdateRecipes(@PathVariable("id") Long id, Model model) {
        model.addAttribute("newRecipe", this.recipeService.findById(id));
        model.addAttribute("recipeID", id);
        return "formUpdateRecipes.html";
    }

    @GetMapping("/formAddIngredientsToRecipe/{id}")
    public String getFormAddIngredientsToRecipe(@PathVariable("id") Long id, Model model) {
        model.addAttribute("recipeAppo", new Recipe());
        model.addAttribute("recipe", this.recipeService.findById(id));
        return "formAddIngredientToRecipe.html";
    }
    
    @PostMapping("/addIngredientsToRecipe/{id}/recipeAppo")
    public String addIngredientToRecipe(@ModelAttribute("recipeAppo") Recipe recipeAppo, @PathVariable("id") Long id, Model model, BindingResult bindingResult) {
        Recipe r = this.recipeService.findById(id);
        List<Ingredient> ingredients = new ArrayList<Ingredient>(recipeAppo.getIngredients());
        for (Ingredient i : ingredients) {
            this.ingredientValidator.validate(i, bindingResult);
            if(bindingResult.hasErrors()){
                i.setId(this.ingredientRepository.findByNameAndQuantity(i.getName(), i.getQuantity()).getId());
            }else{
                this.ingredientService.save(i);
            }
            r.getIngredients().add(i);
        }
        this.recipeService.save(r);
        return "redirect:/formUpdateRecipes/"+r.getId();
    }

    @PostMapping("/updateRecipes/{id}/newRecipe")
    public String updateRecipes(@ModelAttribute("newRecipe") Recipe recipe, @RequestParam(value="newImages",required=false) List<MultipartFile> file, @RequestParam(value="removeImageIndexes", required=false) List<Integer> removeImageIndexes, 
                                @PathVariable("id") Long id, Model model, BindingResult bindingResult) {
        try{
            Recipe oldRecipe = this.recipeService.findById(id);
            recipe.setId(id);
            recipe.setChef(oldRecipe.getChef());
            recipe.setIngredients(oldRecipe.getIngredients());
            if(removeImageIndexes!=null){
                for(int i : removeImageIndexes){
                    oldRecipe.getBase64().remove(i);
                }
            }
            if(file!=null){
                System.out.println(file.isEmpty());
                List<String> bytes = new ArrayList<String>();
                for(MultipartFile f : file){
                    byte[] byteFoto = f.getBytes();
                    bytes.add(Base64.getEncoder().encodeToString(byteFoto));
                }
                recipe.setBase64(bytes);
                if(recipe.getBase64()!=null && oldRecipe.getBase64()!=null){
                    recipe.getBase64().addAll(oldRecipe.getBase64());
                }else if(recipe.getBase64()==null && oldRecipe.getBase64()!=null){
                    recipe.setBase64(oldRecipe.getBase64());
                }
            }else{
                recipe.setBase64(oldRecipe.getBase64());
            }
            this.recipeService.save(recipe);
            return "redirect:/recipes/"+recipe.getId();
        }catch(IOException e){
            model.addAttribute("error", "Error uploading image");
            return "formUpdateRecipes.html";
        }
    }
    
    @GetMapping("/admin/formNewRecipe")
    public String getAdminFormNewRecipe(Model model) {
        model.addAttribute("chefs", this.chefRepository.findAll());
        model.addAttribute("recipe", new Recipe());
        return "admin/adminFormNewRecipe.html";
    }

    @PostMapping("/admin/newRecipe/recipe")
    public String adminNewRecipe(@ModelAttribute("recipe") Recipe recipe, @RequestParam("image") List<MultipartFile> file, Model model, BindingResult bindingResult){
        try{
            recipe.setChef(this.chefRepository.findById(Long.parseLong(recipe.getChefId())).get());
            this.recipeValidator.validate(recipe, bindingResult);
            if(bindingResult.hasErrors()){
                model.addAttribute("error", "Recipe already exists");
                return "redirect:/formNewRecipe";
            }else{
                List<String> bytes = new ArrayList<String>();
                for(MultipartFile f : file){
                    byte[] byteFoto = f.getBytes();
                    bytes.add(Base64.getEncoder().encodeToString(byteFoto));
                }
                recipe.setBase64(bytes);
                this.recipeService.save(recipe);
                model.addAttribute("recipe", recipe);
                return "redirect:/recipes/"+recipe.getId();
            }
        }catch(IOException e){
            model.addAttribute("error", "Error uploading image");
            return "formNewRecipe.html";
        }
    }

    @GetMapping("/deleteIngredientFromRecipe/{recipeID}/{ingredientID}")
    public String deleteIngredientFromRecipe(@PathVariable("recipeID") Long idR, @PathVariable("ingredientID") Long idI, Model model) {
        Recipe r = this.recipeService.findById(idR);
        List<Ingredient> i = new ArrayList<Ingredient>(r.getIngredients());
        i.remove(this.ingredientRepository.findById(idI).get());
        r.setIngredients(i);
        this.recipeService.save(r);
        return "redirect:/formUpdateRecipes/"+r.getId();
    }
    
    @GetMapping("/admin/updateRecipes")
    public String getAdminUpdateRecipes(Model model) {
        model.addAttribute("recipes", this.recipeService.findAll());
        return "admin/adminUpdateRecipes.html";
    }

    @GetMapping("/admin/formUpdateRecipes/{id}")
    public String getAdminFormUpdateRecipes(@PathVariable("id") Long id, Model model) {
        model.addAttribute("recipeID", id);
        model.addAttribute("newRecipe", this.recipeService.findById(id));
        model.addAttribute("chefs", this.chefRepository.findAll());
        return "admin/adminFormUpdateRecipes.html";
    }

    @PostMapping("admin/updateRecipes/{id}/newRecipe")
    public String adminUpdateRecipes(@ModelAttribute("newRecipe") Recipe recipe, @RequestParam(value="newImages",required=false) List<MultipartFile> file, @RequestParam(value="removeImageIndexes", required=false) List<Integer> removeImageIndexes, 
                                @PathVariable("id") Long id, Model model, BindingResult bindingResult) {
        try{
            Recipe oldRecipe = this.recipeService.findById(id);
            recipe.setId(id);
            recipe.setChef(this.chefRepository.findById(Long.parseLong(recipe.getChefId())).get());
            recipe.setIngredients(oldRecipe.getIngredients());
            if(removeImageIndexes!=null){
                for(int i : removeImageIndexes){
                    oldRecipe.getBase64().remove(i);
                }
            }
            if(file!=null){
                System.out.println(file.isEmpty());
                List<String> bytes = new ArrayList<String>();
                for(MultipartFile f : file){
                    byte[] byteFoto = f.getBytes();
                    bytes.add(Base64.getEncoder().encodeToString(byteFoto));
                }
                recipe.setBase64(bytes);
                if(recipe.getBase64()!=null && oldRecipe.getBase64()!=null){
                    recipe.getBase64().addAll(oldRecipe.getBase64());
                }else if(recipe.getBase64()==null && oldRecipe.getBase64()!=null){
                    recipe.setBase64(oldRecipe.getBase64());
                }
            }else{
                recipe.setBase64(oldRecipe.getBase64());
            }
            this.recipeService.save(recipe);
            return "redirect:/recipes/"+recipe.getId();
        }catch(IOException e){
            model.addAttribute("error", "Error uploading image");
            return "admin/adminFormUpdateRecipes.html";
        }
    }

}
