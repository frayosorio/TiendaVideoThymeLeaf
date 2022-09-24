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
public class EmpresaServicioVista {

    //private String urlBase = "http://localhost:8080/empresas";
    private String urlBase = "https://tiendavideomintic.herokuapp.com/empresas";

    private List<Empresa> empresas;

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

        ResponseEntity<Empresa[]> response = restTemplate.exchange(url, HttpMethod.GET, request, Empresa[].class);
        empresas = Arrays.asList(response.getBody());
    }

    public Page<Empresa> listarPagina(Pageable pageable, Usuario usuario) {
        if (empresas == null) {
            listar(usuario);
        }
        int tamañoPagina = pageable.getPageSize();
        int paginaActual = pageable.getPageNumber();
        int posicionInicial = tamañoPagina * paginaActual;
        List<Empresa> lista;
        if (empresas.size() < posicionInicial) {
            lista = Collections.emptyList();
        } else {
            int posicionFinal = Math.min(posicionInicial + tamañoPagina, empresas.size());
            lista = empresas.subList(posicionInicial, posicionFinal);
        }

        Page<Empresa> empresasPage = new PageImpl<Empresa>(lista, PageRequest.of(paginaActual, tamañoPagina),
                empresas.size());
        return empresasPage;
    }

    public void buscar(String dato, Usuario usuario) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = obtenerHeader(usuario);

        HttpEntity<String> request = new HttpEntity<String>(headers);

        String urlT = urlBase + "/buscar/" + dato;
        ResponseEntity<Empresa[]> response = restTemplate.exchange(urlT, HttpMethod.GET, request, Empresa[].class);

        empresas = Arrays.asList(response.getBody());
    }

    public Empresa guardar(Empresa empresa, Usuario usuario) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = obtenerHeader(usuario);

        HttpEntity<Empresa> request = new HttpEntity<Empresa>(empresa, headers);

        ResponseEntity<Empresa> response;
        String url;
        if (empresa.getId() == 0) {
            url = urlBase + "/agregar";
            response = restTemplate.exchange(url, HttpMethod.POST, request, Empresa.class);
        } else {
            url = urlBase + "/modificar";
            response = restTemplate.exchange(url, HttpMethod.PUT, request, Empresa.class);
        }
        return response.getBody();

    }

    public int encontrarPagina(long id, int tamañoPagina) {
        int indice = IntStream.range(0, empresas.size())
                .filter(i -> empresas.get(i).getId() == id)
                .findFirst()
                .orElse(-1);

        int pagina = (int) (indice / tamañoPagina) + 1;
        return pagina;
    }

    public Empresa obtener(long id, Usuario usuario) {
        String url = urlBase + "/obtener/" + id;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = obtenerHeader(usuario);
        HttpEntity<String> request = new HttpEntity<String>(headers);

        ResponseEntity<Empresa> response = restTemplate.exchange(url, HttpMethod.GET, request, Empresa.class);
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
