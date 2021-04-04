package com.ubt.gymmanagementsystem.controllers.auth;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.ubt.gymmanagementsystem.repositories.administration.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/403")
    public String forbidden403(){
        return "auth/403";
    }

    @GetMapping("/404")
    public String notFound404(){
        return "auth/404";
    }

    @GetMapping("/login")
    public String login(HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken))
            response.sendRedirect("/index");

        return "auth/login";
    }

    @GetMapping("/index")
    public String index(){
        return "fragments/dashboard";
    }
}