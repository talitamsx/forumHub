package challenge.forum_hub.domain;

// Exceção customizada para regras de negócio
public class ValidacaoException extends RuntimeException {
    public ValidacaoException(String mensagem) {
        super(mensagem); // Passa a mensagem para a superclasse (RuntimeException)
    }
}
