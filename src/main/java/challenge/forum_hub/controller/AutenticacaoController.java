package challenge.forum_hub.controller;

import challenge.forum_hub.domain.usuario.DadosAutenticacao;
import challenge.forum_hub.domain.usuario.Usuario;
import challenge.forum_hub.infra.DadosTokenJWT;
import challenge.forum_hub.infra.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService; //prestar atenção no nome, Spring tbm tem uma classe com esse nome


    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @PostMapping
    @Transactional
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) throws Exception {
        var authManager = authenticationConfiguration.getAuthenticationManager();
        var authToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = authManager.authenticate(authToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }


    //  return ResponseEntity.ok(tokenService.gerarToken((Usuario) authentication.getPrincipal()));
    }

