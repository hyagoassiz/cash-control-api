package com.cashcontrol.usuario.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cashcontrol.usuario.dto.UsuarioRequestDTO;
import com.cashcontrol.usuario.dto.UsuarioResponseDTO;
import com.cashcontrol.usuario.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public UsuarioResponseDTO criarUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        return usuarioService.criarUsuario(usuarioRequestDTO);
    }
}
