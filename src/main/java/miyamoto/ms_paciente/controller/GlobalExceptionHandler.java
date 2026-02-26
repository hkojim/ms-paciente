package miyamoto.ms_paciente.controller;

import jakarta.servlet.http.HttpServletRequest;
import miyamoto.ms_paciente.infrastructure.exceptions.AuthException;
import miyamoto.ms_paciente.infrastructure.exceptions.BusinessException;
import miyamoto.ms_paciente.infrastructure.exceptions.ErrorResponseDTO;
import miyamoto.ms_paciente.infrastructure.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusiness(BusinessException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }



    @ExceptionHandler({AuthException.class, BadCredentialsException.class})
    public ResponseEntity<ErrorResponseDTO> handleAuth(Exception ex, HttpServletRequest request) {
        String message = (ex instanceof BadCredentialsException) ? "Credenciais inválidas." : ex.getMessage();
        return buildResponse(HttpStatus.UNAUTHORIZED, message, request);
    }

    // Captura erros inesperados do sistema (Segurança e Robustez)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro interno inesperado.", request);
    }

    //Método centralizador apara contraução de resposta
    //Garante que o JSON de eerro seja sempre o mesmo
    private ResponseEntity<ErrorResponseDTO> buildResponse(HttpStatus status,
                                                           String message,
                                                           HttpServletRequest request) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }
}
