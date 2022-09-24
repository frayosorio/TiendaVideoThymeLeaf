package tiendavideo.tiendavideo.vista.servicio;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import tiendavideo.tiendavideo.modelo.Usuario;

@Service
public class UsuarioServicioVista {

    //private String urlBase = "http://localhost:8080/usuarios";
    private String urlBase = "https://tiendavideomintic.herokuapp.com/usuarios";

    public Usuario login(String nombreUsuario, String clave) {
        String url = urlBase + "/login?usuario=" + nombreUsuario + "&clave=" + clave;
        RestTemplate restTemplate = new RestTemplate();
        Usuario usuario = restTemplate.getForObject(url, Usuario.class);
        return usuario;
    }
}
