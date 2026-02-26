package miyamoto.ms_paciente.business.dto.response;

public record LoginResponseDTO(String token,
                               String type, // Ex: "Bearer"
                               Long expiresIn) {
}
