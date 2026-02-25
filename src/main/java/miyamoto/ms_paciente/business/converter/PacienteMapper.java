package miyamoto.ms_paciente.business.converter;

import miyamoto.ms_paciente.business.dto.request.PacienteRequestDTO;
import miyamoto.ms_paciente.business.dto.response.PacienteResponseDTO;
import miyamoto.ms_paciente.infrastrcture.entity.PacienteEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface PacienteMapper {
    // Converte DTO para Entidade
    PacienteEntity toEntity(PacienteRequestDTO dto);

    // Converte Entidade para DTO de Resposta
    PacienteResponseDTO toResponse(PacienteEntity entity);

    // Atualização Parcial (PATCH/PUT inteligente)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PacienteRequestDTO dto, @MappingTarget PacienteEntity entity);
}
