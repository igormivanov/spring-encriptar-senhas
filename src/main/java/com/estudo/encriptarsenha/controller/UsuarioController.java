package com.estudo.encriptarsenha.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estudo.encriptarsenha.model.UsuarioModel;
import com.estudo.encriptarsenha.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/listar")
	public ResponseEntity<List<UsuarioModel>> listarTodosUsuario(){
		return ResponseEntity.ok(usuarioRepository.findAll());
	}
	
	@PostMapping("/salvar")
	public ResponseEntity<UsuarioModel> salvarNovoUsuario(@RequestBody UsuarioModel usuarioModel){		
		usuarioModel.setPassword(passwordEncoder.encode(usuarioModel.getPassword()));
		return ResponseEntity.ok(usuarioRepository.save(usuarioModel));
	}
	
	@GetMapping("/validarSenha")
	public ResponseEntity<Boolean> validarSenha(@RequestParam String login, @RequestParam String password){
		Optional<UsuarioModel> optUsuario = usuarioRepository.findByLogin(login);
		if(optUsuario.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
		}
		
		boolean valid = false;
		
		UsuarioModel usuarioModel = optUsuario.get();
		valid = passwordEncoder.matches(password, usuarioModel.getPassword());
		
		HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
		
		return ResponseEntity.status(status).body(valid);
	}
}
