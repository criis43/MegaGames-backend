package megagames_backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import megagames_backend.model.Usuario;
import megagames_backend.repository.UsuarioRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "4. Usuarios", description = "Consulta de usuarios registrados (Solo Admin)")
public class UsuarioController {

    private final UsuarioRepository usuarioRepo;

    public UsuarioController(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepo.findAll();
    }
}
