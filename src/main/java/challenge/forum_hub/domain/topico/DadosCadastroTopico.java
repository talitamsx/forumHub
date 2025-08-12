package challenge.forum_hub.domain.topico;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO usado para receber os dados de cadastro de um novo tópico.
 * Contém anotações de validação para garantir que os campos obrigatórios não sejam nulos ou vazios.
 */

public record DadosCadastroTopico (

        @NotBlank
        String titulo,

        @NotBlank
        String mensagem,

        @NotBlank
        String nomeCurso
) {}
