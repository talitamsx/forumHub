package challenge.forum_hub.domain.topico;

import java.time.format.DateTimeFormatter;

public record DadosDetalhamentoTopico(
        Long id,
        String titulo,
        String mensagem,
        String nomeCurso,
        String autor,
        String dataCriacao,
        String status
) {
    public DadosDetalhamentoTopico(Topico topico){
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getCurso().getNome(),
                topico.getAutor().getNome(),
                topico.getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                (topico.getAtivo() != null && topico.getAtivo() ? "Ativo" : "Inativo"));
    }
}
