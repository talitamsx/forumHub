package challenge.forum_hub.domain.topico;

import java.time.format.DateTimeFormatter;

/**
 * DTO para retornar as informações de um tópico em uma lista
 * Construído a partir de um objeto Tópico
 */

public record DadosListagemTopico(
        Long id,
        String titulo,
        String mensagem,
        String nomeCurso,
        String autor,
        String dataCriacao,
        String status
) {

    // Construtor que recebe um objeto Topico e preenche os campos do DTO
    public DadosListagemTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getCurso().getNome(),
                topico.getAutor().getNome(),
                topico.getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                (topico.getAtivo() != null && topico.getAtivo() ? "Ativo" : "Inativo")
        );
    }
}
