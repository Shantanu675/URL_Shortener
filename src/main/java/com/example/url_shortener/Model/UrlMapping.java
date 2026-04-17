package com.example.url_shortener.Model;

import jakarta.persistence.*;
import lombok.Setter;

@Setter
@Entity
@Table(
        name = "url_mapping",
        indexes = {
                @Index(name = "idx_short_code", columnList = "shortCode")
        }
)
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String shortCode;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String originalUrl;

    @Column(nullable = false)
    private long clickCount;

    public UrlMapping(){}

    public UrlMapping(String shortCode, String originalUrl){
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.clickCount = 0;
    }

    public Long getId(){ return id; }

    public String getShortCode(){ return shortCode; }

    public String getOriginalUrl(){ return originalUrl; }

    public long getClickCount(){ return clickCount; }

    public void incrementClicks(){
        this.clickCount++;
    }
}