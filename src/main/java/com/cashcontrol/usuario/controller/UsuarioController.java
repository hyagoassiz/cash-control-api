package com.cashcontrol.usuario.controller;

import com.cashcontrol.usuario.dto.UsuarioRequestDTO;
import com.cashcontrol.usuario.dto.UsuarioResponseDTO;
import com.cashcontrol.usuario.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public UsuarioResponseDTO criarUsuario(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        return usuarioService.criarUsuario(usuarioRequestDTO);
    }
}
