package miyamoto.ms_paciente.infrastructure.exceptions;

public class AuthException extends RuntimeException{

    //Erro de autenticação/token
    public AuthException(String mensagem){
        super(mensagem);
    }

    public AuthException(String mensagem, Throwable throwable){
        super(mensagem,throwable);

    }
}
