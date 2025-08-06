package challenge.forum_hub.infra;

import challenge.forum_hub.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Lazy
public class SecurityFilter extends OncePerRequestFilter {


        @Autowired
        private TokenService tokenService;

//        @Autowired
//        private UsuarioRepository usuarioRepository;

    @Autowired
    private ApplicationContext applicationContext;

        @Override
        //FilterChain representa a cadeia de filtros na aplicação
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

            //  System.out.println("CHAMANDO FILTER ");
            //recuperar token
            var tokenJWT = recuperarToken(request);

            if (tokenJWT != null) {
                var subject = tokenService.getSubject(tokenJWT);

                var usuarioRepository = applicationContext.getBean(UsuarioRepository.class);
                var usuario = usuarioRepository.findByEmail(subject)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                //  System.out.println("logado na requisição!!!!");
            }

            filterChain.doFilter(request, response); //necessário para chamar os proximos filtros da aplicação
        }

        private String recuperarToken(HttpServletRequest request) {

            var authorizationHeader = request.getHeader("Authorization");
            if(authorizationHeader != null) {
                return authorizationHeader.replace("Bearer ", "");
            }
            return null;
        }
    }

