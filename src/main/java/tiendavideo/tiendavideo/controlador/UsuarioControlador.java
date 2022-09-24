package tiendavideo.tiendavideo.controlador;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import tiendavideo.tiendavideo.modelo.Usuario;
import tiendavideo.tiendavideo.repositorio.UsuarioRepositorio;
import tiendavideo.tiendavideo.seguridad.FiltroSeguridad;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControlador {

        @Autowired
        private UsuarioRepositorio repositorio;

        @GetMapping("/login")
        public Usuario login(@RequestParam("usuario") String nombreUsuario, @RequestParam("clave") String clave) {
                Usuario usuario = repositorio.validar(nombreUsuario, clave);
                if (usuario != null) {
                        usuario.setToken(generarToken(nombreUsuario));
                        return usuario;
                }
                return null;
        }

        private String generarToken(String nombreUsuario) {
                List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                                .commaSeparatedStringToAuthorityList("ROLE_USER");

                String token = Jwts
                                .builder()
                                .setId("tiendavideo")
                                .setSubject(nombreUsuario)
                                .claim("authorities",
                                                grantedAuthorities.stream()
                                                                .map(GrantedAuthority::getAuthority)
                                                                .collect(Collectors.toList()))
                                .setIssuedAt(new Date(System.currentTimeMillis()))
                                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                                .signWith(SignatureAlgorithm.HS512,
                                                FiltroSeguridad.SECRETO.getBytes())
                                .compact();

                return "Bearer " + token;
        }

}
