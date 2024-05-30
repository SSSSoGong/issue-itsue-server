package com.ssssogong.issuemanager.controller;

import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ImageController {

    @GetMapping("/images/{fileName}")
    public ResponseEntity<Resource> loadImage(@PathVariable(value = "fileName") String fileName) throws MalformedURLException {
        final URI image = Paths.get("").toAbsolutePath().resolve("saveimages").resolve(fileName).toUri();
        final UrlResource urlResource = new UrlResource(image);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(urlResource);
    }
}
