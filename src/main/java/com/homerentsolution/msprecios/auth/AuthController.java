package com.homerentsolution.msprecios.auth;

import com.homerentsolution.msprecios.security.JwtUtil;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody AuthRequest request) {

        if (request.getUsername().equals("admin")
                &&
                request.getPassword().equals("1234")) {

            String token =
                    JwtUtil.generarToken(
                            "admin",
                            "ADMIN"
                    );

            Map<String, String> response =
                    new HashMap<>();

            response.put("token", token);
            response.put("role", "ADMIN");

            return ResponseEntity.ok(response);
        }

        if (request.getUsername().equals("cliente")
                &&
                request.getPassword().equals("1234")) {

            String token =
                    JwtUtil.generarToken(
                            "cliente",
                            "USER"
                    );

            Map<String, String> response =
                    new HashMap<>();

            response.put("token", token);
            response.put("role", "USER");

            return ResponseEntity.ok(response);
        }

        return ResponseEntity
                .badRequest()
                .body("Credenciales inválidas");
    }
}
