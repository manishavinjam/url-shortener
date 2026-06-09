package com.mani.urlshortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.util.List;
import java.util.UUID;

@RestController
public class UrlController {

    @Autowired
    private UrlRepository urlRepository;

    // Shorten URL
    @PostMapping("/shorten")
    public Url shortenUrl(@RequestBody Url url) {
        String shortCode = UUID.randomUUID().toString().substring(0, 6);
        url.setShortCode(shortCode);
        return urlRepository.save(url);
    }

    // Redirect to original URL
    @GetMapping("/r/{shortCode}")
    public RedirectView redirect(@PathVariable String shortCode) {
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("URL not found"));
        url.setClickCount(url.getClickCount() + 1);
        urlRepository.save(url);
        return new RedirectView(url.getOriginalUrl());
    }

    // Get all URLs with analytics
    @GetMapping("/analytics")
    public List<Url> getAnalytics() {
        return urlRepository.findAll();
    }
}