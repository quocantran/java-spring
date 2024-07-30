package com.example.test.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.test.domain.Role;
import com.example.test.domain.User;
import com.example.test.service.UserService;
import com.example.test.core.error.ForbiddenException;
import com.example.test.domain.Permission;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    @Transactional
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String path = request.getRequestURI();
        String method = request.getMethod();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email != null && !email.isEmpty()) {
            User user = userService.getUserByUsername(email);
            if (user != null) {
                Role role = user.getRole();
                if (role != null) {
                    List<Permission> permissions = role.getPermissions();
                    boolean isVerified = permissions.stream().anyMatch(permission -> {
                        return permission.getApiPath().equals(path) && permission.getMethod().equals(method);
                    });

                    // if (!isVerified) {
                    // throw new ForbiddenException("You don't have permission to access this
                    // resource");
                    // }

                }
            }
        }
        return true;
    }
}
