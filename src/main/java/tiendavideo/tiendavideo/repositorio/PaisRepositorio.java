package tiendavideo.tiendavideo.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tiendavideo.tiendavideo.modelo.Pais;

@Repository
public interface PaisRepositorio extends JpaRepository<Pais, Long> {

    @Query("SELECT p FROM Pais p WHERE p.pais LIKE '%'||?1||'%'")
    List<Pais> buscar(String nombre);
}
