package michelangelodicello.CapstoneBackEnd.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import michelangelodicello.CapstoneBackEnd.entities.Utente;
import michelangelodicello.CapstoneBackEnd.exceptions.UnauthorizedException;
import michelangelodicello.CapstoneBackEnd.payloads.ErrorDTO;
import michelangelodicello.CapstoneBackEnd.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {

            String authHeader = request.getHeader("Authorization");

            /*
             * Se NON c'è un token, il filtro NON deve bloccare la richiesta.
             *
             * Sarà SecurityConfig a decidere se quella rotta:
             * - è pubblica -> permitAll()
             * - richiede autenticazione -> 401/403
             */
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String accessToken = authHeader.substring(7);

            // Verifica firma e validità token
            jwtTools.verifyToken(accessToken);

            // Estrae id utente
            String id = jwtTools.extractIdFromToken(accessToken);

            Utente currentUtente = utenteRepository
                    .findById(Long.parseLong(id))
                    .orElseThrow(() ->
                            new UnauthorizedException(
                                    "Utente associato al token non trovato!"
                            )
                    );

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            currentUtente,
                            null,
                            currentUtente.getAuthorities()
                    );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (UnauthorizedException ex) {

            SecurityContextHolder.clearContext();

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.setContentType(
                    MediaType.APPLICATION_JSON_VALUE
            );

            response.setCharacterEncoding("UTF-8");

            response.getWriter().write(
                    objectMapper.writeValueAsString(
                            new ErrorDTO(ex.getMessage())
                    )
            );
        }
    }

    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request
    ) {

        String path = request.getServletPath();
        String method = request.getMethod();

        // LOGIN / REGISTER
        if (path.startsWith("/auth/")) {
            return true;
        }

        if (path.startsWith("/api/auth/")) {
            return true;
        }

        // GET PRODOTTI PUBBLICI
        if (
                method.equalsIgnoreCase("GET")
                        && path.startsWith("/api/products")
        ) {
            return true;
        }

        // GET CATEGORIE PUBBLICHE
        if (
                method.equalsIgnoreCase("GET")
                        && path.startsWith("/api/categories")
        ) {
            return true;
        }

        return false;
    }
}