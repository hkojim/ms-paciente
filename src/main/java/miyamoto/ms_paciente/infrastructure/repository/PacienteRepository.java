package miyamoto.ms_paciente.infrastructure.repository;

import jakarta.transaction.Transactional;
import miyamoto.ms_paciente.infrastructure.entity.PacienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<PacienteEntity, Long> {
    // Busca por CPF para evitar duplicidade
    Optional<PacienteEntity> findByCpf(String cpf);

    boolean existsByCpf(String cpf);

    @Transactional
    void deleteByCpf(String email);
}
