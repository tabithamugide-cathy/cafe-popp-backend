package com.mugide.cafe_popp_backend.service;

import com.mugide.cafe_popp_backend.entity.AppUser;
import com.mugide.cafe_popp_backend.repository.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public List<AppUser> getAll() {
        return appUserRepository.findAll();
    }

    public AppUser getById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found: " + id));
    }

    public AppUser create(AppUser user) {
        return appUserRepository.save(user);
    }
}