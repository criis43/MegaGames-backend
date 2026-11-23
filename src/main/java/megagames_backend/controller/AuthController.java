package megagames_backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import megagames_backend.model.Usuario;
import megagames_backend.repository.UsuarioRepository;
import megagames_backend.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "1. Autenticación", description = "Login y registro de usuarios con JWT")
public class AuthController {

    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UsuarioRepository usuarioRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        Optional<Usuario> userOpt = usuarioRepo.findByEmail(email);
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Correo o contraseña incorrectos"));
        }

        Usuario usuario = userOpt.get();
        String token = jwtService.generarToken(usuario.getEmail(), usuario.getRol());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "usuario", usuario
        ));
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (usuarioRepo.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("error", "El correo ya está registrado"));
        }

        Usuario nuevo = new Usuario(
                body.get("nombre_completo"),
                email,
                passwordEncoder.encode(body.get("password")),
                body.getOrDefault("telefono", ""),
                "CLIENTE"
        );

        Usuario guardado = usuarioRepo.save(nuevo);
        String token = jwtService.generarToken(guardado.getEmail(), guardado.getRol());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "usuario", guardado
        ));
    }
}
