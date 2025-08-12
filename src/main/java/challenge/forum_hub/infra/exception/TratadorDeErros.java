package challenge.forum_hub.infra.exception;

import challenge.forum_hub.domain.ValidacaoException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
//Classe de intercepetação global de exceções lançadas em controllers
public class TratadorDeErros {

    //Trata quando uma entidade não é encontrada no banco
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    //Trata erro de validação
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
        //Pega os erros de validação do body do request
        List<DadosErroValidacao> erros = ex.getFieldErrors().stream()
                .map(DadosErroValidacao::new) //transforma erro em um DTO personalizado
                .toList();
        //Retorna status 400 com a lista de erros
        return ResponseEntity.badRequest().body(erros);
    }


    //class (record) para representar mensagem de erro de validação e mostrar formatada
    private record DadosErroValidacao(String campo, String mensagem) {
        // Construtor que recebe um FieldError (erro de validação do Spring)
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());// Pega o nome do campo e a mensagem do erro
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity tratarErroJson(HttpMessageNotReadableException ex) {

        if (ex.getCause() instanceof InvalidFormatException ife && ife.getTargetType().isEnum()) {
            Object[] valoresAceitos = ife.getTargetType().getEnumConstants();
            String mensagem = "Essa categoria não existe. Lista de categorias cadastradas: " + Arrays.toString(valoresAceitos);
            return ResponseEntity.badRequest().body(mensagem);
        }

        return ResponseEntity.badRequest().body("Erro na leitura do corpo da requisição.");
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity tratarRegraDeNegocio(ValidacaoException ex){
        String msg = ex.getMessage();

        if (msg.contains("não encontrado")){
            return ResponseEntity.status(404).body(msg);
        }
        if(msg.contains("permissão")) {
            return ResponseEntity.status(403).body(msg);
        }
        return ResponseEntity.badRequest().body(msg);

    }
//
//    @ExceptionHandler(UsernameNotFoundException.class)
//    public ResponseEntity<String> tratarUsuarioNaoEncontrado() {
//        return ResponseEntity.status(404).body("USUÁRIO NÃO ENCONTRADO");
//    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> tratarCredenciaisinvalidas() {
        return ResponseEntity.status(401).body("E-mail ou senha inválidos");
    }


}