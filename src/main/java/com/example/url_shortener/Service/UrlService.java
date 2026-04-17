package com.example.url_shortener.Service;


import com.example.url_shortener.Model.UrlMapping;
import com.example.url_shortener.Repository.UrlRepository;
import com.example.url_shortener.Util.Base62Encoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class UrlService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final UrlRepository repo;

    public UrlService(UrlRepository repo){
        this.repo=repo;
    }

    public String shortenUrl(String originalUrl){

        String code;

        // 🔁 avoid duplicates
        do {
            code = Base62Encoder.generateRandomCode();
        } while (repo.findByShortCode(code).isPresent());

        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(originalUrl);
        mapping.setShortCode(code);

        repo.save(mapping);

        redisTemplate.opsForValue().set(code, originalUrl, 10, TimeUnit.MINUTES);

        return code;
    }

    @Transactional
    public Optional<UrlMapping> getOriginalUrl(String code) {

        // 1. Check Redis
        String cachedUrl = redisTemplate.opsForValue().get(code);

        if (cachedUrl != null) {
            repo.incrementClick(code); // track click

            UrlMapping url = new UrlMapping();
            url.setShortCode(code);
            url.setOriginalUrl(cachedUrl);

            return Optional.of(url);
        }

        // 2. DB fetch
        Optional<UrlMapping> urlOpt = repo.findByShortCode(code);

        // 3. Cache + increment
        urlOpt.ifPresent(url -> {
            redisTemplate.opsForValue().set(code, url.getOriginalUrl(), 10, TimeUnit.MINUTES);
            repo.incrementClick(code);
        });

        return urlOpt;
    }
}
