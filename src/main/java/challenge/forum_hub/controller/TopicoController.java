package challenge.forum_hub.controller;

import challenge.forum_hub.domain.curso.CursoRepository;
import challenge.forum_hub.domain.topico.DadosCadastroTopico;
import challenge.forum_hub.domain.topico.Topico;
import challenge.forum_hub.domain.topico.TopicoRepository;
import challenge.forum_hub.domain.usuario.Usuario;
import challenge.forum_hub.domain.usuario.UsuarioRepository;
import challenge.forum_hub.infra.DadosTokenJWT;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public void cadastrar (@RequestBody  @Valid DadosCadastroTopico dados, @AuthenticationPrincipal Usuario autor){
        var curso = cursoRepository.findByNome(dados.nomeCurso())
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        var topico = new Topico(dados, curso, autor);

        topicoRepository.save(topico);

    }

}
