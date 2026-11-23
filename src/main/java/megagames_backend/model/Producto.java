package megagames_backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String plataforma; // nintendo, playstation, xbox

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private Integer precio;

    private String imagen;

    @Column(name = "es_preventa")
    @JsonProperty("es_preventa")
    private Integer esPreventa = 0;

    @Column(name = "fecha_lanzamiento")
    @JsonProperty("fecha_lanzamiento")
    private String fechaLanzamiento = "Disponible ya";

    private Integer stock = 10;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public Producto() {}

    public Producto(String titulo, String plataforma, String descripcion, Integer precio, String imagen,
                    Integer esPreventa, String fechaLanzamiento, Integer stock, Categoria categoria) {
        this.titulo = titulo;
        this.plataforma = plataforma;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
        this.esPreventa = esPreventa;
        this.fechaLanzamiento = fechaLanzamiento;
        this.stock = stock;
        this.categoria = categoria;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getPlataforma() { return plataforma; }
    public void setPlataforma(String plataforma) { this.plataforma = plataforma; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getPrecio() { return precio; }
    public void setPrecio(Integer precio) { this.precio = precio; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public Integer getEsPreventa() { return esPreventa; }
    public void setEsPreventa(Integer esPreventa) { this.esPreventa = esPreventa; }

    public String getFechaLanzamiento() { return fechaLanzamiento; }
    public void setFechaLanzamiento(String fechaLanzamiento) { this.fechaLanzamiento = fechaLanzamiento; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}
