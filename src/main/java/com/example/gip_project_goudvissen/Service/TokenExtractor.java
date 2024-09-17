package com.example.gip_project_goudvissen.Service;

import javax.servlet.http.HttpServletRequest;

public class TokenExtractor {
    public static String extractToken(HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
