package miyamoto.ms_paciente.business;

import lombok.RequiredArgsConstructor;
import miyamoto.ms_paciente.business.converter.PacienteMapper;
import miyamoto.ms_paciente.business.dto.request.LoginRequestDTO;
import miyamoto.ms_paciente.business.dto.request.PacienteRequestDTO;
import miyamoto.ms_paciente.business.dto.response.LoginResponseDTO;
import miyamoto.ms_paciente.business.dto.response.PacienteResponseDTO;
import miyamoto.ms_paciente.infrastructure.exceptions.AuthException;
import miyamoto.ms_paciente.infrastructure.exceptions.BusinessException;
import miyamoto.ms_paciente.infrastructure.exceptions.ResourceNotFoundException;
import miyamoto.ms_paciente.infrastructure.repository.PacienteRepository;
import miyamoto.ms_paciente.infrastructure.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {
    private final PacienteRepository repository;
    private final PacienteMapper mapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public PacienteResponseDTO salvarPaciente(PacienteRequestDTO dto) {
        // 1. Validação de Regra de Negócio
        if(repository.findByCpf(dto.cpf()).isPresent()) {
            throw new BusinessException("CPF já cadastrado no sistema.");
        }

        // 2. Conversão para Entidade
        var entity = mapper.toEntity(dto);

        // 3. CRIPTOGRAFIA - Aqui transformamos "senha123" em um hash seguro antes de salvar
        String senhaCriptografada = passwordEncoder.encode(dto.senha());
        entity.setSenha(senhaCriptografada);

        // 4. Persistência
        var entitySalva = repository.save(entity);

        return mapper.toResponse(entitySalva);
    }

    public LoginResponseDTO autenticarPaciente(LoginRequestDTO login) {
        try {
            // O AuthenticationManager lança BadCredentialsException se falhar
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.cpf(), login.senha())
            );

            String token = jwtUtil.generateToken(login.cpf());
            return new LoginResponseDTO(token, "Bearer ", 36000L);

        } catch (BadCredentialsException e) {
            // Lançamos nossa exceção customizada que o GlobalExceptionHandler já conhece
            throw new AuthException("Credenciais inválidas para o CPF: " + login.cpf());
        }
    }

    public List<PacienteResponseDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    public PacienteResponseDTO buscarPorCpf(String cpf) {
        return repository.findByCpf(cpf)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado."));
    }

    public PacienteResponseDTO atualizarDadosPaciente(String cpf, PacienteRequestDTO dto) {
        var entity = repository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado."));

        // O Mapstruct ignora campos nulos do DTO, mantendo os dados anteriores
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toResponse(repository.save(entity));
    }

    public void deleteByCpf(String cpf) {
        if(!repository.existsByCpf(cpf)) throw new ResourceNotFoundException("Paciente não existe.");
        repository.deleteByCpf(cpf);
    }
}
