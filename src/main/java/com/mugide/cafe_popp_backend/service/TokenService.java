package com.mugide.cafe_popp_backend.service;

import com.mugide.cafe_popp_backend.entity.AppUser;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {
    private final Map<String, AppUser> activeTokens = new ConcurrentHashMap<>();

    public String issue(AppUser user) {
        String token = UUID.randomUUID().toString();
        activeTokens.put(token, user);
        return token;
    }

    public AppUser resolve(String token) {
        return activeTokens.get(token);
    }

    public void revoke(String token) {
        activeTokens.remove(token);
    }
}
