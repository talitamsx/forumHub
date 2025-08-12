package challenge.forum_hub.domain.topico;

import challenge.forum_hub.domain.curso.Curso;
import challenge.forum_hub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity(name = "topicos") //representa uma entidade no banco
@Table(name = "topicos") //nome da tabela no banco
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id") //garante que a comparação entre objetos use apenas o id

public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mensagem;
    private String titulo;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    //relacionamento muitos-pra-um: muitos tópicos podem ter o mesmo autor
    //EAGER carrega autor junto com o tópico
    @ManyToOne (fetch = FetchType.EAGER)
    private Usuario autor;

    //Muitos tópicos podem pertencer ao mesmo curso
    @ManyToOne (fetch = FetchType.EAGER)
    Curso curso;

    private Boolean ativo;


    /**
     * Construtor para criar um novo tópico
     * @param dados - DTO com informações do tópico
     * @param curso - Objeto Curso recuperado do banco
     * @param autor - Usuário que criou o tópico
     */
    public Topico (DadosCadastroTopico dados, Curso curso, Usuario autor){
        this.titulo = dados.titulo();
        this.mensagem = dados.mensagem();
        this.curso = curso;
        this.autor = autor;
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true; //novo tópico começa como ativo
    }

    /**
     * Atualiza/Edita as informações do tópico
     * @param dados - DTO contendo as alterações
     * @param novoCurso - Curso atualizado se for alterado
     */

    public void atualizarInformacoes(DadosAtualizacaoTopico dados, Curso novoCurso  ){
        //Necessário fazer comprativo para não sobreescrever os campos que não forem alterados
        if (dados.titulo() != null && !dados.titulo().isBlank()) {
            this.titulo = dados.titulo();
        }
        if (dados.mensagem() != null && !dados.mensagem().isBlank()) {
            this.mensagem = dados.mensagem();
        }
        if (novoCurso != null){
            this.curso = novoCurso;
        }
    }

    public void inativarTopico() {
        this.ativo = false;
    }
}
