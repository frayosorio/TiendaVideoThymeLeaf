package tiendavideo.tiendavideo.vista.controlador;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import tiendavideo.tiendavideo.util.ItemMenu;

@Controller
@RequestMapping("/")
public class InicioControladorVista {

    public static List<ItemMenu> generarMenu() {
        List<ItemMenu> menu = new ArrayList<>();

        List<ItemMenu> subMenu = new ArrayList<>();
        subMenu.add(new ItemMenu("Paises", "/menu/pais", "fas fa-globe"));
        subMenu.add(new ItemMenu("Regiones", "/menu/region", "far fa-flag"));
        subMenu.add(new ItemMenu("Ciudades", "/menu/ciudad", "fas fa-city"));

        ItemMenu itemMenu = new ItemMenu("Geografia", "/", "fa fa-globe");
        itemMenu.setHijos(subMenu);
        menu.add(itemMenu);

        itemMenu = new ItemMenu("Empresa", "/menu/empresa", "fas fa-industry");
        menu.add(itemMenu);

        return menu;
    }

    @GetMapping("/")
    public String inicio(Model modelo) {

        modelo.addAttribute("menu", generarMenu());

        return "inicio";
    }

}
