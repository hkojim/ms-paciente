package miyamoto.ms_paciente.business.dto.response;

import java.time.LocalDate;

public record PacienteResponseDTO(Long id,
                                  String nome,
                                  String cpf,
                                  LocalDate dataNascimento,
                                  String convenio) {
}
