package com.cashcontrol.usuario.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cashcontrol.exception.EmailAlreadyExistsException;
import com.cashcontrol.usuario.dto.LoginRequestDTO;
import com.cashcontrol.usuario.dto.TokenResponseDTO;
import com.cashcontrol.usuario.dto.UsuarioRequestDTO;
import com.cashcontrol.usuario.dto.UsuarioResponseDTO;
import com.cashcontrol.usuario.entity.Usuario;
import com.cashcontrol.usuario.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public UsuarioService(UsuarioRepository usuarioRepository, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtService = jwtService;
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

    public TokenResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Usuario usuario = usuarioRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(loginRequestDTO.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Senha incorreta");
        }

        String accessToken = jwtService.gerarAccessToken(usuario);
        String refreshToken = jwtService.gerarRefreshToken(usuario);

        return new TokenResponseDTO(accessToken, refreshToken);
    }

    public TokenResponseDTO refresh(String refreshToken) {
        if (!jwtService.isTokenValido(refreshToken)) {
            throw new RuntimeException("Refresh token inválido");
        }

        if (!jwtService.extrairTipo(refreshToken).equals("refresh")) {
            throw new RuntimeException("Token não é um refresh token");
        }

        String email = jwtService.extrairEmail(refreshToken);
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String newAccessToken = jwtService.gerarAccessToken(usuario);
        String newRefreshToken = jwtService.gerarRefreshToken(usuario);

        return new TokenResponseDTO(newAccessToken, newRefreshToken);
    }
}

