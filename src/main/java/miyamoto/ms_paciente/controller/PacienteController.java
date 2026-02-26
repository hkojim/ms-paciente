package miyamoto.ms_paciente.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


import miyamoto.ms_paciente.business.PacienteService;
import miyamoto.ms_paciente.business.dto.request.LoginRequestDTO;
import miyamoto.ms_paciente.business.dto.request.PacienteRequestDTO;
import miyamoto.ms_paciente.business.dto.response.LoginResponseDTO;
import miyamoto.ms_paciente.business.dto.response.PacienteResponseDTO;
import miyamoto.ms_paciente.infrastructure.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
@Tag(name = "Paciente", description = "Endpoints para gestão e autenticação de pacientes")
public class PacienteController {

    private final PacienteService service;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping
    @Operation(summary = "Cadastrar novo paciente", description = "Cria um novo registro de paciente no banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou CPF já cadastrado")
    })
    public ResponseEntity<PacienteResponseDTO> salvarPaciente(@RequestBody @Valid PacienteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvarPaciente(dto));
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar paciente", description = "Realiza o login e retorna o token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO login) {
        // Sem try/catch. Se falhar no service, o Advice resolve.
        return ResponseEntity.ok(service.autenticarPaciente(login));
    }

    @GetMapping
    @Operation(summary = "Listar todos os pacientes", description = "Retorna uma lista de todos os pacientes cadastrados.")
    public ResponseEntity<List<PacienteResponseDTO>> getAll() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{cpf}")
    @Operation(summary = "Buscar por CPF", description = "Retorna os detalhes de um paciente específico através do CPF.")
    @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    public ResponseEntity<PacienteResponseDTO> buscarPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(service.buscarPorCpf(cpf));
    }

    @PutMapping("/{cpf}")
    @Operation(summary = "Atualizar dados", description = "Atualiza as informações de um paciente existente.")
    public ResponseEntity<PacienteResponseDTO> update(@PathVariable String cpf,
                                                      @RequestBody PacienteRequestDTO dto) {
        return ResponseEntity.ok(service.atualizarDadosPaciente(cpf, dto));
    }

    @DeleteMapping("/{cpf}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir paciente", description = "Remove um paciente do sistema permanentemente.")
    public void deleteByCpf(@PathVariable String cpf) {
        service.deleteByCpf(cpf);
    }
}
