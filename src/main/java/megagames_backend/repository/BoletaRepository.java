package megagames_backend.repository;

import megagames_backend.model.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BoletaRepository extends JpaRepository<Boleta, Long> {
    List<Boleta> findByUsuarioIdOrderByIdDesc(Long usuarioId);
}