package challenge.forum_hub.domain.curso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastrarCurso(
        @NotBlank
        String nome,

        @NotNull(message = "Categoria é obrigatória")
        Categoria categoria
) {

}
