package tiendavideo.tiendavideo.vista.servicio;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    private String urlBase = "http://localhost:8080/empresas";

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
            lista = empresas.subList(posicionFinal, posicionFinal);
        }

        Page<Empresa> empresasPage = new PageImpl<Empresa>(lista, PageRequest.of(paginaActual, tamañoPagina),
                empresas.size());

        return empresasPage;
    }

}
