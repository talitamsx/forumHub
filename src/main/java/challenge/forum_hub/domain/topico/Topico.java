package challenge.forum_hub.domain.topico;

import challenge.forum_hub.domain.curso.Curso;
import challenge.forum_hub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity(name = "topicos")
@Table(name = "topicos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mensagem;
    private String titulo;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    @ManyToOne (fetch = FetchType.EAGER)
    private Usuario autor;

    @ManyToOne (fetch = FetchType.EAGER)
    Curso curso;

    private Boolean ativo;

    public Topico (DadosCadastroTopico dados, Curso curso, Usuario autor){
        this.titulo = dados.titulo();
        this.mensagem = dados.mensagem();
        this.curso = curso;
        this.autor = autor;
        this.dataCriacao = LocalDateTime.now();
    }

    public void atualizarInformacoes(DadosCadastroTopico dados){
        this.titulo = dados.titulo();
        this.mensagem = dados.mensagem();
        this.curso = curso;
    }

    public void inativarTopico() {
        this.ativo = false;
    }
}
