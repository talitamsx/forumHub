package challenge.forum_hub.domain.topico;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroTopico (

        @NotBlank
        String titulo,

        @NotBlank
        String mensagem,

        @NotBlank
        String nomeCurso
) {}
