package com.example.url_shortener.Repository;
import com.example.url_shortener.Model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlMapping,Long> {

    Optional<UrlMapping> findByShortCode(String shortCode);

    @Modifying
    @Query("UPDATE UrlMapping u SET u.clickCount = u.clickCount + 1 WHERE u.shortCode = :code")
    void incrementClick(@Param("code") String code);
}
