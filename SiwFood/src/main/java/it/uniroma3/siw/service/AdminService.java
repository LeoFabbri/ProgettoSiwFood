package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.uniroma3.siw.model.Admin;
import it.uniroma3.siw.repository.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Admin getAdmin(Long id){
        return this.adminRepository.findById(id).get();
    }

    public Admin saveAdmin(Admin admin){
        return this.adminRepository.save(admin);
    }

}
