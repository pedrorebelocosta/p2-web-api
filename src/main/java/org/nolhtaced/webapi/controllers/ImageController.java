package org.nolhtaced.webapi.controllers;

import org.nolhtaced.core.utilities.ImageUtil;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/images")
public class ImageController {
    @GetMapping(value = "/{imagePath}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> getImage(@PathVariable String imagePath) {
        try {
            Resource r = new ByteArrayResource(ImageUtil.getImageBytes(imagePath));

            return ResponseEntity.ok()
                    .contentLength(r.contentLength())
                    .body(r);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
