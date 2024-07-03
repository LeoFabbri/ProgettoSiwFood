package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.Chef;

public interface ChefRepository extends CrudRepository<Chef,Long>{
    
    public boolean existsByNameAndSurnameAndDateOfBirth(String name, String surname, LocalDate dateOfBirth);

    @Query("select c from Chef c where c != :chef")
    public List<Chef> findAllExceptChef(Chef chef);

}
