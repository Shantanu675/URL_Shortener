package com.example.url_shortener.Controller;

import com.example.url_shortener.DTO.UrlRequest;
import com.example.url_shortener.Model.UrlMapping;
import com.example.url_shortener.Service.RateLimiterService;
import com.example.url_shortener.Service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@RestController
public class UrlController {

    private final UrlService service;

    private final RateLimiterService rateLimiterService;

    public UrlController(UrlService service, RateLimiterService rateLimiterService) {
        this.service = service;
        this.rateLimiterService = rateLimiterService;
    }

    @PostMapping("/shorten")
    public String shorten(@RequestBody UrlRequest request,
                          HttpServletRequest httpRequest) {

        String ip = httpRequest.getRemoteAddr();

        if (!rateLimiterService.isAllowed(ip)) {
            throw new RuntimeException("Too many requests");
        }

        return "http://localhost:8080/" + service.shortenUrl(request.url);
    }

    @GetMapping("/{code}")
    public void redirect(@PathVariable String code,
                         HttpServletResponse response,
                         HttpServletRequest request) throws IOException {

        String ip = request.getRemoteAddr();

        if (!rateLimiterService.isAllowed(ip)) {
            response.setStatus(429);
            return;
        }

        Optional<UrlMapping> mapping = service.getOriginalUrl(code);

        if (mapping.isPresent()) {
            response.sendRedirect(mapping.get().getOriginalUrl());
        } else {
            response.setStatus(404);
        }
    }
}
