package tiendavideo.tiendavideo.vista.servicio;


import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

import tiendavideo.tiendavideo.modelo.*;

@Service
public class PaisServicioVista {
    
    //private String urlBase = "http://localhost:8080/paises";
    private String urlBase = "https://tiendavideomintic.herokuapp.com/paises";

    private List<Pais> paises;

    public List<Pais> getLista(){
        return paises;
    }

    private HttpHeaders obtenerHeader(Usuario usuario) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", usuario.getToken());
        return headers;
    }

    public void listar(Usuario usuario) {
        String url = urlBase + "/listar";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = obtenerHeader(usuario);
        HttpEntity<String> request = new HttpEntity<String>(headers);

        ResponseEntity<Pais[]> response = restTemplate.exchange(url, HttpMethod.GET, request, Pais[].class);
        paises = Arrays.asList(response.getBody());
    }

}
