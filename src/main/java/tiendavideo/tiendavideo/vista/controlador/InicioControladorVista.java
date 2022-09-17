package tiendavideo.tiendavideo.vista.controlador;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import tiendavideo.tiendavideo.modelo.Usuario;
import tiendavideo.tiendavideo.util.ItemMenu;
import tiendavideo.tiendavideo.vista.servicio.UsuarioServicioVista;

@Controller
@RequestMapping("/")
public class InicioControladorVista {

    public static int TAMAÑO_PAGINA = 10;

    @Autowired
    private UsuarioServicioVista usuarioServicio;

    public static List<ItemMenu> generarMenu() {
        List<ItemMenu> menu = new ArrayList<>();

        List<ItemMenu> subMenu = new ArrayList<>();
        subMenu.add(new ItemMenu("Paises", "/menu/pais", "fas fa-globe"));
        subMenu.add(new ItemMenu("Regiones", "/menu/region", "far fa-flag"));
        subMenu.add(new ItemMenu("Ciudades", "/menu/ciudad", "fas fa-city"));

        ItemMenu itemMenu = new ItemMenu("Geografia", "#", "fa fa-globe");
        itemMenu.setHijos(subMenu);
        menu.add(itemMenu);

        itemMenu = new ItemMenu("Empresa", "/menu/empresa", "fas fa-industry");
        menu.add(itemMenu);

        return menu;
    }

    @GetMapping("/")
    public String inicio(Model modelo, HttpSession sesion) {
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        if (usuario == null) {
            usuario = usuarioServicio.login("FRAY", "123");
            sesion.setAttribute("usuario", usuario);
        }

        modelo.addAttribute("menu", generarMenu());
        modelo.addAttribute("plantilla", "presentacion");

        return "inicio";
    }

    @GetMapping("/menu/{opcion}")
    public String seleccionarMenu(@PathVariable String opcion) {
        String ruta = "redirect:/" + opcion + "/listar/1/" + TAMAÑO_PAGINA;
        return ruta;
    }

}
