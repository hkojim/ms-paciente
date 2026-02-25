package miyamoto.ms_paciente.business.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;

public record PacienteRequestDTO(@NotBlank String nome,
                                 @NotBlank @Size(min = 11, max = 11) String cpf,
                                 @NotNull LocalDate dataNascimento,
                                 String convenio) {
}
