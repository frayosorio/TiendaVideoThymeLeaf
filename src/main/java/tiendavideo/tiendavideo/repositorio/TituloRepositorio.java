package tiendavideo.tiendavideo.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tiendavideo.tiendavideo.modelo.*;

@Repository
public interface TituloRepositorio extends JpaRepository<Titulo, Long> {

}
