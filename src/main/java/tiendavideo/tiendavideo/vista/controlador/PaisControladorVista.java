package tiendavideo.tiendavideo.vista.controlador;

import java.util.List;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tiendavideo.tiendavideo.modelo.Pais;
import tiendavideo.tiendavideo.modelo.Usuario;
import tiendavideo.tiendavideo.util.Login;
import tiendavideo.tiendavideo.vista.servicio.PaisServicioVista;

@Controller
@RequestMapping("/pais")
public class PaisControladorVista {

    @Autowired
    PaisServicioVista servicioPais;

    @GetMapping("/listar/{pagina}/{tamaño}")
    public String listar(@PathVariable int pagina, @PathVariable int tamaño,
            Model modelo, HttpSession sesion) {
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        if (usuario != null) {
            Page<Pais> paginaActual = servicioPais.listarPagina(PageRequest.of(pagina - 1, tamaño), usuario);

            modelo.addAttribute("paginaActual", paginaActual);
            List<Integer> numerosPaginas = IntStream.rangeClosed(1, paginaActual.getTotalPages())
                    .boxed()
                    .collect(Collectors.toList());

            modelo.addAttribute("numerosPaginas", numerosPaginas);
            modelo.addAttribute("plantilla", "pais");
            sesion.setAttribute("numeroPaginaActual", paginaActual.getNumber() + 1);
        } else {
            modelo.addAttribute("plantilla", "presentacion");
            modelo.addAttribute("mensajeerror", "usuario no logueado");
        }
        modelo.addAttribute("menu", InicioControladorVista.generarMenu());
        modelo.addAttribute("paiseditado", new Pais());
        modelo.addAttribute("login", new Login());
        return "inicio";
    }

    @GetMapping("/buscar")
    public String buscar(String dato, Model modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            if (dato.isEmpty()) {
                servicioPais.listar(usuario);
            } else {
                servicioPais.buscar(dato, usuario);
            }

            Page<Pais> paginaActual = servicioPais
                    .listarPagina(PageRequest.of(1, InicioControladorVista.TAMAÑO_PAGINA), usuario);

            modelo.addAttribute("paginaActual", paginaActual);
            session.setAttribute("numeroPaginaActual", paginaActual.getNumber() + 1);

            int totalPaginas = paginaActual.getTotalPages();
            if (totalPaginas > 0) {
                List<Integer> numerosPaginas = IntStream.rangeClosed(1, totalPaginas)
                        .boxed()
                        .collect(Collectors.toList());
                modelo.addAttribute("numerosPaginas", numerosPaginas);
            }
        }
        modelo.addAttribute("paiseditada", new Pais());
        return "pais";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable long id, Model modelo, HttpSession sesion) {
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        servicioPais.listar(usuario);
        Pais pais = new Pais();
        if (id > 0) {
            pais = servicioPais.obtener(id, usuario);
        }

        modelo.addAttribute("listapaises", servicioPais.getLista());
        modelo.addAttribute("paiseditada", pais);
        return "paiseditar";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("paiseditada") Pais pais, HttpSession sesion) {
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        pais = servicioPais.guardar(pais, usuario);
        servicioPais.listar(usuario);
        int pagina = servicioPais.encontrarPagina(pais.getId(),
                InicioControladorVista.TAMAÑO_PAGINA);

        String ruta = "redirect:/pais/listar/" + pagina + "/" + InicioControladorVista.TAMAÑO_PAGINA;
        return ruta;
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable long id, Model modelo, HttpSession sesion,
            RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        if (servicioPais.eliminar(id, usuario)) {
            servicioPais.listar(usuario);
        } else {
            redirectAttributes.addFlashAttribute("mensajeerror", "No se pudo eliminar la Pais");
        }
        int pagina = (int) sesion.getAttribute("numeroPaginaActual");
        String ruta = "redirect:/pais/listar/" + pagina + "/" + InicioControladorVista.TAMAÑO_PAGINA;
        return ruta;
    }

}
