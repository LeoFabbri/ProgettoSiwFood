package it.uniroma3.siw.controller.validator;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.repository.ChefRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.Null;
import jakarta.validation.executable.ExecutableValidator;
import jakarta.validation.metadata.BeanDescriptor;

@Component
public class ChefValidator implements Validator{

    @Autowired
    private ChefRepository chefRepository;

    @Override
    public void validate(Object o, Errors errors){
        Chef c = (Chef)o;
        if(c.getName()!=null && c.getSurname()!=null && c.getDateOfBirth()!=null && chefRepository.existsByNameAndSurnameAndDateOfBirth(c.getName(), c.getSurname(),c.getDateOfBirth())){
            errors.reject("chef.duplicate");
        }
   }

    @Override
    public boolean supports(Class<?> aClass){
        return Chef.class.equals(aClass);
    }
    
}
