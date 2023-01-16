package com.ecom.ecom.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class AuthentController {

    @RequestMapping("/logout")
    public void logout(final HttpServletRequest request, final Authentication user) throws ServletException {
        final HttpSession session = request.getSession(false);
        SecurityContextHolder.clearContext();
        request.logout();
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
