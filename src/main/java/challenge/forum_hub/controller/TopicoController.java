package challenge.forum_hub.controller;

import challenge.forum_hub.domain.ValidacaoException;
import challenge.forum_hub.domain.curso.CursoRepository;
import challenge.forum_hub.domain.topico.*;
import challenge.forum_hub.domain.usuario.Usuario;
import challenge.forum_hub.domain.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.cert.Extension;

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
    public ResponseEntity<DadosDetalhamentoTopico> cadastrar (@RequestBody  @Valid DadosCadastroTopico dados, @AuthenticationPrincipal Usuario autor){
        var curso = cursoRepository.findByNome(dados.nomeCurso())
                .orElseThrow(() -> new ValidacaoException("Curso não encontrado"));

        if(topicoRepository.existsBytituloAndMensagem(dados.titulo(), dados.mensagem())){
            throw new ValidacaoException("Já existe um tópico com este título e mensagem.");
        }

        var topico = new Topico(dados, curso, autor);

        topicoRepository.save(topico);

        return ResponseEntity.status(HttpStatus.CREATED).body(new DadosDetalhamentoTopico(topico));

    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemTopico>> listar(@PageableDefault(size =10, sort = {"dataCriacao"})Pageable paginacao) {
        var page = topicoRepository.findAllByAtivoTrue(paginacao).map(DadosListagemTopico::new);
        return ResponseEntity.ok(page);
    }
    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoTopico> editar(
            @PathVariable Long id,
            @RequestBody @Valid DadosCadastroTopico dados,
            @AuthenticationPrincipal Usuario autor
    ){
        var topico = topicoRepository.findById(id)
                .orElseThrow(()-> new ValidacaoException("Tópico não encontrado"));
        if(!topico.getAutor().getId().equals(autor.getId())){
            throw new ValidacaoException("VOCÊ NÃO TEM PERMISSÃO PARA EDITAR ESTE TÓPICO");
        }

       topico.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deletar(@PathVariable Long id,
                                        @AuthenticationPrincipal Usuario usuarioLogado) {
        var topico = topicoRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Tópico não encontrado"));

        if(!topico.getAutor().getId().equals(usuarioLogado.getId())){
            throw new ValidacaoException("VOCÊ NÃO TEM PERMISSÃO PARA DELETAR ESSE TÓPICO");
        }
        topico.inativarTopico();
        return ResponseEntity.noContent().build();
    }
}
