package com.mugide.cafe_popp_backend.controller;

import com.mugide.cafe_popp_backend.entity.AppUser;
import com.mugide.cafe_popp_backend.service.AppUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public List<AppUser> getAll() {
        return appUserService.getAll();
    }

    @GetMapping("/{id}")
    public AppUser getById(@PathVariable Long id) {
        return appUserService.getById(id);
    }

    @PostMapping
    public AppUser create(@RequestBody AppUser user) {
        return appUserService.create(user);
    }
}