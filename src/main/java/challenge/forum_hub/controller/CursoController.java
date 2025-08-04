package challenge.forum_hub.controller;

import challenge.forum_hub.domain.curso.Curso;
import challenge.forum_hub.domain.curso.CursoRepository;
import challenge.forum_hub.domain.curso.DadosCadastrarCurso;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoRepository repository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastrarCurso dados) {
        var curso = new Curso(dados.nome(), dados.categoria());
        repository.save(curso);
    }



}
