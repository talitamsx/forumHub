package challenge.forum_hub.controller;

import challenge.forum_hub.domain.ValidacaoException;
import challenge.forum_hub.domain.curso.Curso;
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


//Controlador REST responsável por gerenciar os endpoints relacionados a Tópicos
@RestController
@RequestMapping("topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Enpoint para cadastrar um novo tópico
     * Recebe dados validados do corpo da requisição
     * Valida se curso existe
     * Verifica se já existe um tópico com o mesmo título e mensagem
     */
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

    /**
     * Endpoint para listar tópicos ativos
     * Retorna a lista ordenada pela data de criação
     */
    @GetMapping
    public ResponseEntity<Page<DadosListagemTopico>> listar(@PageableDefault(size =10, sort = {"dataCriacao"})Pageable paginacao) {
        var page = topicoRepository.findAllByAtivoTrue(paginacao).map(DadosListagemTopico::new);
        return ResponseEntity.ok(page);
    }

    /**
     * Endpoint para atualizar um tóico já existente
     * Valida o autor do tópico para edita-lo
     */
    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoTopico> atualizarInformacoes(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoTopico dados,
            @AuthenticationPrincipal Usuario autor //Usuário logado extraído do token JWT
    ){
        var topico = topicoRepository.findById(id)
                .orElseThrow(()-> new ValidacaoException("Tópico não encontrado"));

        if (Boolean.FALSE.equals(topico.getAtivo())) {
            throw new ValidacaoException("TÓPICO DELETADO, NÃO É POSSÍVEL EDITA-LO");
        }

        //verifica se usuário logado é o autor do tópico
        if(!topico.getAutor().getId().equals(autor.getId())){
            throw new ValidacaoException("VOCÊ NÃO TEM PERMISSÃO PARA EDITAR ESTE TÓPICO");
        }

        Curso novoCurso = null;

        if (dados.nomeCurso() != null && !dados.nomeCurso().isBlank()) {
            novoCurso = cursoRepository.findByNome(dados.nomeCurso())
                    .orElseThrow(()-> new ValidacaoException("Curso não encontrado"));
        }

        topico.atualizarInformacoes(dados, novoCurso);
        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    //Listar todos os tópicos
    @GetMapping("/todos")
    public ResponseEntity<Page<DadosListagemTopico>> listarTodosTopicos(
            @PageableDefault(size = 10, sort = {"id"}) Pageable paginacao){
        var page = topicoRepository.findAll(paginacao)
                .map(DadosListagemTopico::new);
        return ResponseEntity.ok(page);
    }

    //listar topico por id
    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoTopico> topicoPorId(@PathVariable Long id){
        var topico = topicoRepository.findById(id)
                .orElseThrow(()-> new ValidacaoException("Tópico não encontrado"));
        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    /**
     * Endpoint para "deletar" (inativar) um tópico
     * Como boas práticas, não remove o tópico do banco, apenas marca como inativo
     * Valida que somente o autor do tópico pode deleta-lo
     */
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
