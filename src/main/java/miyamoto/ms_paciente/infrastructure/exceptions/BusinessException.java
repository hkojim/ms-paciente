package miyamoto.ms_paciente.infrastructure.exceptions;

public class BusinessException extends RuntimeException{

    //Erro de regra de negócio (ex: CPF não encontrado)
    public BusinessException(String mensagem){
        super(mensagem);
    }

    public BusinessException(String mensagem, Throwable throwable){
        super(mensagem,throwable);

    }
}
