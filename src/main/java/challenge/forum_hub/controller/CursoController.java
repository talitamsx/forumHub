package challenge.forum_hub.controller;

import challenge.forum_hub.domain.ValidacaoException;
import challenge.forum_hub.domain.curso.Curso;
import challenge.forum_hub.domain.curso.CursoRepository;
import challenge.forum_hub.domain.curso.DadosCadastrarCurso;
import challenge.forum_hub.domain.curso.DadosListagemCurso;
import challenge.forum_hub.domain.topico.DadosListagemTopico;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoRepository repository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastrarCurso dados) {

        // Verifica se já existe um curso com esse nome
        if (repository.findByNome(dados.nome()).isPresent()) {
            throw new ValidacaoException("Já existe um curso com esse nome.");
        }
        var curso = new Curso(dados.nome(), dados.categoria());
        repository.save(curso);
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemCurso>> listar(@PageableDefault(size =10, sort = {"nome"}) Pageable paginacao) {
        var page = repository.findAll(paginacao).map(DadosListagemCurso::new);
        return ResponseEntity.ok(page);
    }
}


