package megagames_backend.config;

import megagames_backend.model.*;
import megagames_backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepo;
    private final CategoriaRepository categoriaRepo;
    private final ProductoRepository productoRepo;
    private final BoletaRepository boletaRepo;

    public DataLoader(UsuarioRepository usuarioRepo, CategoriaRepository categoriaRepo,
                      ProductoRepository productoRepo, BoletaRepository boletaRepo) {
        this.usuarioRepo = usuarioRepo;
        this.categoriaRepo = categoriaRepo;
        this.productoRepo = productoRepo;
        this.boletaRepo = boletaRepo;
    }

    @Override
    public void run(String... args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 1. Crear usuarios para los 3 roles (clave: 123456)
        if (usuarioRepo.count() == 0) {
            usuarioRepo.saveAll(List.of(
                new Usuario("Administrador MegaGames", "admin@megagames.cl", encoder.encode("123456"), "+56900000000", "ADMIN"),
                new Usuario("Vendedor Tienda", "vendedor@megagames.cl", encoder.encode("123456"), "+56911111111", "VENDEDOR"),
                new Usuario("Gamer Demo", "gamer@megagames.cl", encoder.encode("123456"), "+56912345678", "CLIENTE")
            ));
        }

        // 2. Crear categorías y productos iniciales
        if (productoRepo.count() == 0) {
            Categoria nintendo = categoriaRepo.save(new Categoria("Nintendo", "Consolas y juegos Nintendo Switch"));
            Categoria playstation = categoriaRepo.save(new Categoria("PlayStation", "Consolas y juegos PlayStation 5"));
            Categoria xbox = categoriaRepo.save(new Categoria("Xbox", "Consolas y juegos Xbox Series X|S"));

            Producto zelda = productoRepo.save(new Producto(
                "The Legend of Zelda: Tears of the Kingdom", "nintendo",
                "Aventura épica por Hyrule.", 59990, "/img/tloz_totk.jpeg",
                0, "Disponible ya", 25, nintendo
            ));

            productoRepo.save(new Producto(
                "Super Mario Bros. Wonder", "nintendo",
                "Nuevas aventuras y diversión de Mario.", 49990, "/img/sm_wonder.jpg",
                0, "Disponible ya", 18, nintendo
            ));

            Producto wolverine = productoRepo.save(new Producto(
                "Marvel's Wolverine", "playstation",
                "Acción visceral de Wolverine en PS5.", 69990, "/img/Wolverine.jpg",
                1, "Otoño 2026", 50, playstation
            ));

            productoRepo.save(new Producto(
                "Metal Gear Solid Delta: Snake Eater", "playstation",
                "Remake con gráficos hiperrealistas.", 64990, "/img/MGS.png",
                0, "Disponible ya", 14, playstation
            ));

            productoRepo.save(new Producto(
                "Grand Theft Auto VI", "xbox",
                "La experiencia de mundo abierto más esperada.", 79990, "/img/Gta6.jpg",
                1, "2026", 100, xbox
            ));

            productoRepo.save(new Producto(
                "Gears of War: E-Day", "xbox",
                "El origen de la pesadilla Locust.", 69990, "/img/gears_eday.jpg",
                1, "2026", 60, xbox
            ));

            // 3. Crear una boleta inicial de ejemplo para el Cliente Demo
            Usuario cliente = usuarioRepo.findByEmail("gamer@megagames.cl").orElse(null);
            if (cliente != null) {
                Boleta boleta = new Boleta(LocalDate.now(), 129980, cliente);
                boleta.getDetalles().add(new DetalleBoleta(1, zelda.getPrecio(), boleta, zelda));
                boleta.getDetalles().add(new DetalleBoleta(1, wolverine.getPrecio(), boleta, wolverine));
                boletaRepo.save(boleta);
            }
        }
    }
}