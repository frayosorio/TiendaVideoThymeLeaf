package tiendavideo.tiendavideo.vista.controlador;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import tiendavideo.tiendavideo.modelo.Empresa;
import tiendavideo.tiendavideo.modelo.Usuario;
import tiendavideo.tiendavideo.vista.servicio.EmpresaServicioVista;

@Controller
@RequestMapping("/empresa")
public class EmpresaControladorVista {

    @Autowired
    EmpresaServicioVista servicioEmpresa;

    @GetMapping("/listar/{pagina}/{tamaño}")
    public String listar(@PathVariable int pagina, @PathVariable int tamaño,
            Model modelo, HttpSession sesion) {
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        if (usuario != null) {
            Page<Empresa> paginaActual = servicioEmpresa.listarPagina(PageRequest.of(pagina - 1, tamaño), usuario);

            modelo.addAttribute("paginaActual", paginaActual);
        }

        return "empresa";
    }

}
