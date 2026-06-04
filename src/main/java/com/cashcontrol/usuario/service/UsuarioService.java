package com.cashcontrol.usuario.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cashcontrol.exception.EmailAlreadyExistsException;
import com.cashcontrol.usuario.dto.UsuarioRequestDTO;
import com.cashcontrol.usuario.dto.UsuarioResponseDTO;
import com.cashcontrol.usuario.entity.Usuario;
import com.cashcontrol.usuario.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO usuarioRequestDTO) {
        if (usuarioRepository.findByEmail(usuarioRequestDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email já cadastrado");
        }

        Usuario usuario = Usuario.builder()
                .nome(usuarioRequestDTO.getNome().trim().replaceAll("\\s+", " "))
                .email(usuarioRequestDTO.getEmail())
                .senha(passwordEncoder.encode(usuarioRequestDTO.getSenha()))
                .build();

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(usuarioSalvo.getNome(), usuarioSalvo.getEmail());
    }

}
