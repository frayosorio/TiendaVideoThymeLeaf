package tiendavideo.tiendavideo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import tiendavideo.tiendavideo.seguridad.FiltroSeguridad;

@SpringBootApplication
public class TiendavideoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiendavideoApplication.class, args);
	}

	@EnableWebSecurity
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable()
					.addFilterAfter(new FiltroSeguridad(), UsernamePasswordAuthenticationFilter.class)
					.authorizeRequests()
					.antMatchers(HttpMethod.GET, "/usuarios/login").permitAll()
					.antMatchers("/", "/menu/**", "/empresa/**", "/login").permitAll()
					.antMatchers("/webjars/**", "/resources/**", "/css/**", "/js/**", "/imagenes/**").permitAll()
					.anyRequest().authenticated();
		}
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:/mensajes");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

}
