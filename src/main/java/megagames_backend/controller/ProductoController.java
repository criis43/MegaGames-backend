package megagames_backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import megagames_backend.model.Categoria;
import megagames_backend.model.Producto;
import megagames_backend.repository.CategoriaRepository;
import megagames_backend.repository.ProductoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "2. Productos", description = "Gestión de videojuegos y preventas")
public class ProductoController {

    private final ProductoRepository productoRepo;
    private final CategoriaRepository categoriaRepo;

    public ProductoController(ProductoRepository productoRepo, CategoriaRepository categoriaRepo) {
        this.productoRepo = productoRepo;
        this.categoriaRepo = categoriaRepo;
    }

    @GetMapping
    public List<Producto> listar(
            @RequestParam(required = false) String plataforma,
            @RequestParam(required = false) Integer preventa) {

        if (plataforma != null && !plataforma.isBlank()) {
            return productoRepo.findByPlataformaContainingIgnoreCase(plataforma);
        }
        if (preventa != null) {
            return productoRepo.findByEsPreventa(preventa);
        }
        return productoRepo.findAll();
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        asignarCategoria(producto);
        return ResponseEntity.ok(productoRepo.save(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Producto datos) {
        return productoRepo.findById(id).map(p -> {
            p.setTitulo(datos.getTitulo());
            p.setPlataforma(datos.getPlataforma());
            p.setDescripcion(datos.getDescripcion());
            p.setPrecio(datos.getPrecio());
            p.setImagen(datos.getImagen());
            p.setEsPreventa(datos.getEsPreventa());
            p.setFechaLanzamiento(datos.getFechaLanzamiento());
            p.setStock(datos.getStock());
            asignarCategoria(p);
            return ResponseEntity.ok(productoRepo.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!productoRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productoRepo.deleteById(id);
        return ResponseEntity.ok(Map.of("mensaje", "Producto eliminado"));
    }

    private void asignarCategoria(Producto p) {
        if (p.getPlataforma() != null) {
            String primera = p.getPlataforma().split(",")[0].trim();
            Categoria cat = categoriaRepo.findByNombreIgnoreCase(primera)
                    .orElseGet(() -> categoriaRepo.save(new Categoria(primera.toUpperCase(), "Categoría " + primera)));
            p.setCategoria(cat);
        }
    }
}