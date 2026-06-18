package com.cashcontrol.usuario.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cashcontrol.usuario.dto.LoginRequestDTO;
import com.cashcontrol.usuario.dto.LoginResponseDTO;
import com.cashcontrol.usuario.dto.TokenResponseDTO;
import com.cashcontrol.usuario.dto.UsuarioRequestDTO;
import com.cashcontrol.usuario.dto.UsuarioResponseDTO;
import com.cashcontrol.usuario.service.JwtService;
import com.cashcontrol.usuario.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public UsuarioController(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public UsuarioResponseDTO criarUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        return usuarioService.criarUsuario(usuarioRequestDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        TokenResponseDTO tokens = usuarioService.login(loginRequestDTO);
        
        LoginResponseDTO response = new LoginResponseDTO(
            "Login realizado com sucesso",
            loginRequestDTO.getEmail()
        );
        
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, jwtService.gerarAccessTokenCookie(tokens.getAccessToken()))
            .header(HttpHeaders.SET_COOKIE, jwtService.gerarRefreshTokenCookie(tokens.getRefreshToken()))
            .body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken) {
        
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RuntimeException("Refresh token não encontrado");
        }

        TokenResponseDTO tokens = usuarioService.refresh(refreshToken);
        
        LoginResponseDTO response = new LoginResponseDTO(
            "Token renovado com sucesso",
            null
        );
        
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, jwtService.gerarAccessTokenCookie(tokens.getAccessToken()))
            .header(HttpHeaders.SET_COOKIE, jwtService.gerarRefreshTokenCookie(tokens.getRefreshToken()))
            .body(response);
    }
    
}
