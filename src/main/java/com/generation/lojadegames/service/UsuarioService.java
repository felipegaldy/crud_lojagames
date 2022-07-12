package com.generation.lojadegames.service;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.lojadegames.model.UserLogin;
import com.generation.lojadegames.model.Usuario;
import com.generation.lojadegames.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	//cadastrar usuario
	public Optional<Usuario> cadastrarUsuario(Usuario usuario){
		//verifica se usuario já existe
		if(usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
			return Optional.empty();
		
		//verificar idade do usuario proibir menores de 18 anos
		if (calcularIdade(usuario.getDataNascimento()) < 18)
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, "Usuário é menor de 18 anos", null);
		
		//verifica se usuario colocou foto
		if (usuario.getFoto().isBlank())
			usuario.setFoto("https://i.imgur.com/Zz4rzVR.png");
		//criptografa senha antes de enviar para o banco de dados
		usuario.setSenha(criptografarSenha(usuario.getSenha()));
		//retorna o usuario
		return Optional.ofNullable(usuarioRepository.save(usuario));
		
	}
	
	
	
	// metodo para criptografar senha

	private String criptografarSenha(String senha) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.encode(senha);
	}
	
	
	public Optional<Usuario> atualizarUsuario(Usuario usuario) {
		//pegar usuario pelo id e verificar
		if(usuarioRepository.findById(usuario.getId()).isPresent()) {
			//atribuir atributos do usuario para um objeto "mais seguro"
			Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario());
			
			//se o usuario já existe e o id do usuario for diferente do original bad request
			if ( (buscaUsuario.isPresent()) && ( buscaUsuario.get().getId() != usuario.getId()))
				throw new ResponseStatusException(
						HttpStatus.BAD_REQUEST, "Usuário já existe!", null);
			
			//verificar idade proibir menores de 18 anos
			if(calcularIdade(buscaUsuario.get().getDataNascimento()) < 18)
				throw new ResponseStatusException(
						HttpStatus.BAD_REQUEST, "Usuário é menor de 18 anos", null);
			
			//verificar foto
			if (usuario.getFoto().isBlank())
				usuario.setFoto("https://i.imgur.com/Zz4rzVR.png");
			
			//encripta a senha
			usuario.setSenha(criptografarSenha(usuario.getSenha()));
			
			//retorna usuario atualizado
			return Optional.ofNullable(usuarioRepository.save(usuario));			
		}//fim do if
		
		return Optional.empty();//usuario nao atende as regras de negocio
	}
	
	
	//AUTENTICAR USUARIO
	public Optional<UserLogin> autenticarUsuario(Optional<UserLogin> userLogin){
		
		//busca usuario e atribui a novo objeto "seguranca"
		
		Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(userLogin.get().getUsuario());
		
		//verificar se usuario realmente existe
		if(buscaUsuario.isPresent()) {
			//comparar senhas
			if(compararSenhas(userLogin.get().getSenha(), buscaUsuario.get().getSenha())) {
				//atribuir atributos a userLogin e gerar token
				userLogin.get().setId(buscaUsuario.get().getId());
				userLogin.get().setNome(buscaUsuario.get().getNome());
				userLogin.get().setFoto(buscaUsuario.get().getFoto());
				userLogin.get().setDataNascimento(buscaUsuario.get().getDataNascimento());
				userLogin.get().setToken(gerarBasicToken(userLogin.get().getUsuario(), userLogin.get().getSenha()));
				userLogin.get().setSenha(buscaUsuario.get().getSenha());
				
				return userLogin;
			}
		}
		return Optional.empty();
	}
	
	
	//metodo para gerar token --LGPD
	private String gerarBasicToken(String usuario, String senha) {
		
		String token = usuario + ":" + senha;
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
		return "Basic " + new String(tokenBase64);
	}
	
	
	//metodo para comparar senhas
	private boolean compararSenhas(String senhaDigitada, String senhaBanco) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(senhaDigitada, senhaBanco);
	}
	
	
	//metodo para verificar a idade
	private int calcularIdade(LocalDate dataNascimento) {

		return Period.between(dataNascimento, LocalDate.now()).getYears();
	}
	

}
