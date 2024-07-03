package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.repository.ChefRepository;

@Service
public class ChefService {
    
    @Autowired
    private ChefRepository chefRepository;

    public Chef findById(Long id){
        return chefRepository.findById(id).get();
    }

    public Iterable<Chef> findAll(){
        return chefRepository.findAll();
    }

    public Chef saveChef(Chef chef){
        return this.chefRepository.save(chef);
    }

    public void deleteById(Long id){
        this.chefRepository.deleteById(id);
    }

}
