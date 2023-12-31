package com.jsantos.userservice.model.exception.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 404: {
                if(methodKey.contains("getBooks"))
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()), "Books were not found");
                break;
            }
            default:
                return new Exception(response.reason());
        }
        return null;
    }
}
