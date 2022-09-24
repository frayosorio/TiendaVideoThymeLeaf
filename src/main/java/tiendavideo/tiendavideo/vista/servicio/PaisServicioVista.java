package tiendavideo.tiendavideo.vista.servicio;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    public Page<Pais> listarPagina(Pageable pageable, Usuario usuario) {
        if (paises == null) {
            listar(usuario);
        }
        int tamañoPagina = pageable.getPageSize();
        int paginaActual = pageable.getPageNumber();
        int posicionInicial = tamañoPagina * paginaActual;
        List<Pais> lista;
        if (paises.size() < posicionInicial) {
            lista = Collections.emptyList();
        } else {
            int posicionFinal = Math.min(posicionInicial + tamañoPagina, paises.size());
            lista = paises.subList(posicionInicial, posicionFinal);
        }

        Page<Pais> paisesPage = new PageImpl<Pais>(lista, PageRequest.of(paginaActual, tamañoPagina),
                paises.size());
        return paisesPage;
    }

    public void buscar(String dato, Usuario usuario) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = obtenerHeader(usuario);

        HttpEntity<String> request = new HttpEntity<String>(headers);

        String urlT = urlBase + "/buscar/" + dato;
        ResponseEntity<Pais[]> response = restTemplate.exchange(urlT, HttpMethod.GET, request, Pais[].class);

        paises = Arrays.asList(response.getBody());
    }

    public Pais guardar(Pais empresa, Usuario usuario) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = obtenerHeader(usuario);

        HttpEntity<Pais> request = new HttpEntity<Pais>(empresa, headers);

        ResponseEntity<Pais> response;
        String url;
        if (empresa.getId() == 0) {
            url = urlBase + "/agregar";
            response = restTemplate.exchange(url, HttpMethod.POST, request, Pais.class);
        } else {
            url = urlBase + "/modificar";
            response = restTemplate.exchange(url, HttpMethod.PUT, request, Pais.class);
        }
        return response.getBody();

    }

    public int encontrarPagina(long id, int tamañoPagina) {
        int indice = IntStream.range(0, paises.size())
                .filter(i -> paises.get(i).getId() == id)
                .findFirst()
                .orElse(-1);

        int pagina = (int) (indice / tamañoPagina) + 1;
        return pagina;
    }

    public Pais obtener(long id, Usuario usuario) {
        String url = urlBase + "/obtener/" + id;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = obtenerHeader(usuario);
        HttpEntity<String> request = new HttpEntity<String>(headers);

        ResponseEntity<Pais> response = restTemplate.exchange(url, HttpMethod.GET, request, Pais.class);
        return response.getBody();
    }

    public boolean eliminar(long id, Usuario usuario) {
        String url = urlBase + "/eliminar/" + id;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = obtenerHeader(usuario);
        HttpEntity<String> request = new HttpEntity<String>(headers);

        try{
            ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.DELETE, request, Boolean.class);
            return response.getBody();
        }
        catch(Exception ex){
            return false;
        }
    }


    

}
