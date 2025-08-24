package com.example.Sports_Arena.Authentication;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null) {
                template.header("Authorization", authHeader);
            }
        }

    }
}
/*
The apply(RequestTemplate template) method in your FeignClientConfig is implementing the RequestInterceptor interface from OpenFeign.

Here’s what it does step by step:

Intercepts every outgoing Feign request
Whenever a Feign client makes an HTTP call, this interceptor is executed before the request is sent.

Gets the current HTTP request (from Spring context)
Using:

ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();


it fetches the current incoming HTTP request (the one hitting your controller).

Extracts the Authorization header from the incoming request

String authHeader = request.getHeader("Authorization");


Copies the Authorization header into the outgoing Feign request

template.header("Authorization", authHeader);


This means if your client request to your service had a JWT or Bearer token in the Authorization header, the same token will automatically be added when your service calls another microservice through Feign.

Why is this useful?

In microservices, one service often needs to call another.

If you’re using JWT tokens or OAuth2, you want the authentication/authorization header to “propagate” so downstream services know who the user is.

Without this interceptor, the Feign client would send requests without the token, and the downstream service would reject them as unauthorized.
 */