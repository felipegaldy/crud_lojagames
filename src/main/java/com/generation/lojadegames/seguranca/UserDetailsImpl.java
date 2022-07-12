package com.generation.lojadegames.seguranca;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.generation.lojadegames.model.Usuario;

public class UserDetailsImpl implements UserDetails{

	/**
	 * gerado automaticamente pelo userdetails
	 */
	private static final long serialVersionUID = 1L;
	
	//declarando variaveis a serem usadas pelo userdetails
	
	private String userName;
	private String password;
	
	//autoridade
	private List<GrantedAuthority> authorities;
	
	public UserDetailsImpl(Usuario user) {

		this.userName = user.getUsuario();
		this.password = user.getSenha();
	}
	
	public UserDetailsImpl() {}
	
	
	/*
	 * gerado automaticamente pelo userdetails
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	


}
