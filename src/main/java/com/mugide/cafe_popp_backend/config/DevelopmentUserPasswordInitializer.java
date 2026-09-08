package com.mugide.cafe_popp_backend.config;

import com.mugide.cafe_popp_backend.entity.AppUser;
import com.mugide.cafe_popp_backend.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class DevelopmentUserPasswordInitializer implements CommandLineRunner {
    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final boolean enabled;

    public DevelopmentUserPasswordInitializer(
            AppUserRepository repository,
            PasswordEncoder passwordEncoder,
            @Value("${app.security.seed-development-passwords:false}") boolean enabled) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.enabled = enabled;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!enabled) return;

        Map<String, String> developmentPasswords = Map.of(
                "admin@restaurant.com", "Admin@12345",
                "jane.waiter@restaurant.com", "Waiter@12345",
                "bob.cashier@restaurant.com", "Cashier@12345",
                "marco.kitchen@restaurant.com", "Chef@12345",
                "guest-orders@cafe-popp.local", "GuestOrder@12345"
        );

        developmentPasswords.forEach((email, password) -> repository.findByEmail(email).ifPresent(user -> repairHash(user, password)));
    }

    private void repairHash(AppUser user, String password) {
        String hash = user.getPasswordHash();
        if (hash == null || hash.length() != 60 || !hash.startsWith("$2")) {
            user.setPasswordHash(passwordEncoder.encode(password));
            repository.save(user);
        }
    }
}
