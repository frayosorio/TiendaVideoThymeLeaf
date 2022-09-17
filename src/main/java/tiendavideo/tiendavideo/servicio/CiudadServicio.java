package tiendavideo.tiendavideo.servicio;

import java.util.List;

import tiendavideo.tiendavideo.modelo.*;


public interface CiudadServicio {

	public List<Ciudad> listar();
	
	public Ciudad obtener(Long id);

    public List<Ciudad> buscar(String nombre);

    public List<Tercero> buscarClientes(String nombre);

	public Ciudad guardar(Ciudad ciudad);

	public boolean eliminar(Long id);

}