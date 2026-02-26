package miyamoto.ms_paciente.business.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(@NotBlank String cpf,
                              @NotBlank String senha) {
}
