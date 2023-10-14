package br.com.wesleymoraes.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.wesleymoraes.todolist.user.IUserRepository;
import br.com.wesleymoraes.todolist.user.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String servetPath = request.getServletPath();

        if (servetPath.startsWith("/tasks/")) {
            // pegar a autenticação (usuário e senha)
            String authorization = request.getHeader("Authorization");

            String authEncoded = authorization.substring("Basic".length()).trim();

            byte[] authDecode = Base64.getDecoder().decode(authEncoded);

            String authString = new String(authDecode);
            String[] credentials = authString.split(":");

            String userName = credentials[0];
            String password = credentials[1];

            // validar usuário

            UserModel user = this.userRepository.findByUserName(userName);

            if (user == null) {
                response.sendError(401);
            } else {

                // validar a senha

                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

                if (passwordVerify.verified) {
                    // Seguindo o fluxo de autenticação
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }

            }

        } else

        {
            filterChain.doFilter(request, response);
        }

    }

}
