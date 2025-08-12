package challenge.forum_hub.controller;

import challenge.forum_hub.domain.ValidacaoException;
import challenge.forum_hub.domain.usuario.UsuarioRepository;
import challenge.forum_hub.infra.security.DadosAutenticacao;
import challenge.forum_hub.domain.usuario.Usuario;
import challenge.forum_hub.infra.security.DadosTokenJWT;
import challenge.forum_hub.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//Controller responsável pela autenticação dos usuários

//Realiza login e retorna um token JWT
@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    //Gerencia o processo de autenticação
    private AuthenticationManager manager;

    @Autowired
    //Serviço para gerar e validar o token
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Endpoint para efetuar login no sistema.
     * Recebe as credenciais do usuário, autentica no sistema e retorna um token JWT.
     * @param dados Objeto contendo email e senha do usuário.
     * @return ResponseEntity contendo o token JWT.
      */
    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados){
        usuarioRepository.findByEmail(dados.email())
                .orElseThrow(()-> new ValidacaoException("USUÁRIO NÃO ENCONTRADO"));
      // var authManager = authenticationConfiguration.getAuthenticationManager();
        var authToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = manager.authenticate(authToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }


//    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) throws Exception {
//        var authManager = authenticationConfiguration.getAuthenticationManager();
//        var authToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
//        var authentication = authManager.authenticate(authToken);
//
//        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
//        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
//    }
}