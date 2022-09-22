package tiendavideo.tiendavideo.vista.controlador;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import tiendavideo.tiendavideo.modelo.Empresa;
import tiendavideo.tiendavideo.modelo.Pais;
import tiendavideo.tiendavideo.modelo.Usuario;
import tiendavideo.tiendavideo.vista.servicio.EmpresaServicioVista;
import tiendavideo.tiendavideo.vista.servicio.PaisServicioVista;

@Controller
@RequestMapping("/empresa")
public class EmpresaControladorVista {

    @Autowired
    EmpresaServicioVista servicioEmpresa;
    @Autowired
    PaisServicioVista servicioPais;

    @GetMapping("/listar/{pagina}/{tamaño}")
    public String listar(@PathVariable int pagina, @PathVariable int tamaño,
            Model modelo, HttpSession sesion) {
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        if (usuario != null) {
            Page<Empresa> paginaActual = servicioEmpresa.listarPagina(PageRequest.of(pagina - 1, tamaño), usuario);

            modelo.addAttribute("paginaActual", paginaActual);
            List<Integer> numerosPaginas = IntStream.rangeClosed(1, paginaActual.getTotalPages())
                    .boxed()
                    .collect(Collectors.toList());

            modelo.addAttribute("numerosPaginas", numerosPaginas);
        }
        modelo.addAttribute("plantilla", "empresa");
        modelo.addAttribute("menu", InicioControladorVista.generarMenu());
        modelo.addAttribute("empresaeditada", new Empresa());
        return "inicio";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable long id, Model modelo, HttpSession sesion) {
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        servicioPais.listar(usuario);
        Empresa empresa = new Empresa();
        if (id > 0) {
            empresa = servicioEmpresa.obtener(id, usuario);
        }

        modelo.addAttribute("listapaises", servicioPais.getLista());
        modelo.addAttribute("empresaeditada", empresa);
        return "empresaeditar";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("empresaeditada") Empresa empresa, HttpSession sesion) {
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        empresa = servicioEmpresa.guardar(empresa, usuario);
        servicioEmpresa.listar(usuario);
        int pagina = servicioEmpresa.encontrarPagina(empresa.getId(),
                InicioControladorVista.TAMAÑO_PAGINA);

        String ruta = "redirect:/empresa/listar/" + pagina + "/" + InicioControladorVista.TAMAÑO_PAGINA;
        return ruta;
    }

}
