package tiendavideo.tiendavideo.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tiendavideo.tiendavideo.modelo.Tercero;

@Repository
public interface TerceroRepositorio extends JpaRepository<Tercero, Long> {


    
}
