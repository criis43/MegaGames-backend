package megagames_backend.repository;

import megagames_backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByPlataformaContainingIgnoreCase(String plataforma);
    List<Producto> findByEsPreventa(Integer esPreventa);
}
