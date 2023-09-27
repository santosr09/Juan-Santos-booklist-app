package com.jsantos.apigateway.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    @Autowired
    private Environment environment;

    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    public static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if(authorizationHeader == null) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }
            String jwt = authorizationHeader.replace("Bearer ", "");
            log.info("jwt: " + jwt);

            if(!isJwtValid(jwt))
                return  onError(exchange, "JWT is not valid", HttpStatus.UNAUTHORIZED);
            log.info("VALID jwt: " + jwt);
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean isJwtValid(String jwt) {
        Algorithm algorithm = Algorithm.HMAC256(environment.getProperty("token.secret").getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT;
        try{
            decodedJWT = verifier.verify(jwt);
        } catch(Exception ex) {
            log.error("Error while decoding token: " + ex.getMessage());
            return false;
        }
        String subject = decodedJWT.getSubject();
        if(subject == null || subject.isEmpty()) {
            return false;
        }

        return true;
    }
}
