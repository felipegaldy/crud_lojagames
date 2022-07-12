package com.generation.lojadegames.seguranca;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.generation.lojadegames.model.Usuario;
import com.generation.lojadegames.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	//metodo do userdetails
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		Optional<Usuario> usuario = usuarioRepository.findByUsuario(username);
		
		usuario.orElseThrow(() -> new UsernameNotFoundException(username + " não encontrado."));
		
		return usuario.map(UserDetailsImpl::new).get();
		
	}
	




}
