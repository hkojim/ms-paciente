package miyamoto.ms_paciente.business.dto.response;

import java.time.LocalDate;

public record PacienteResponseDTO(Long id,
                                  String nome,
                                  String cpf,
                                  String senha,
                                  LocalDate dataNascimento,
                                  String convenio) {
}
