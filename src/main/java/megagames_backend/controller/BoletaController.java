package megagames_backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import megagames_backend.model.Boleta;
import megagames_backend.model.DetalleBoleta;
import megagames_backend.model.Producto;
import megagames_backend.model.Usuario;
import megagames_backend.repository.BoletaRepository;
import megagames_backend.repository.ProductoRepository;
import megagames_backend.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "3. Boletas y Ventas", description = "Registro y consulta de órdenes de compra")
public class BoletaController {

    private final BoletaRepository boletaRepo;
    private final UsuarioRepository usuarioRepo;
    private final ProductoRepository productoRepo;

    public BoletaController(BoletaRepository boletaRepo, UsuarioRepository usuarioRepo, ProductoRepository productoRepo) {
        this.boletaRepo = boletaRepo;
        this.usuarioRepo = usuarioRepo;
        this.productoRepo = productoRepo;
    }

    @GetMapping("/boletas")
    public List<Boleta> listarBoletas() {
        return boletaRepo.findAll();
    }

    @PostMapping("/boletas")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> crearBoleta(@RequestBody Map<String, Object> body) {
        Long usuarioId = Long.valueOf(body.get("usuario_id").toString());
        Usuario usuario = usuarioRepo.findById(usuarioId).orElse(null);
        if (usuario == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuario no encontrado"));
        }

        List<?> items = (List<?>) body.get("items");
        Boleta boleta = new Boleta(LocalDate.now(), 0, usuario);
        int total = 0;

        for (Object rawItem : items) {
            Map<String, Object> item = (Map<String, Object>) rawItem;
            Long prodId = Long.valueOf(item.get("producto_id").toString());
            int cantidad = Integer.parseInt(item.get("cantidad").toString());
            Producto prod = productoRepo.findById(prodId).orElse(null);

            if (prod != null) {
                int precioUnit = prod.getPrecio();
                total += precioUnit * cantidad;
                prod.setStock(Math.max(0, prod.getStock() - cantidad));
                productoRepo.save(prod);
                boleta.getDetalles().add(new DetalleBoleta(cantidad, precioUnit, boleta, prod));
            }
        }

        boleta.setTotal(total);
        return ResponseEntity.ok(boletaRepo.save(boleta));
    }

    @PostMapping("/carrito")
    public ResponseEntity<?> agregarCarrito(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(Map.of("mensaje", "Producto añadido al carrito"));
    }
}
