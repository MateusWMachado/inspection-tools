package com.ey.inspectiontools.controller;

import com.ey.inspectiontools.model.checklist.ImageEvidence;
import com.ey.inspectiontools.repository.ImageRepository;
import com.ey.inspectiontools.util.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user/image")
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @PostMapping
    public ResponseEntity<ImageEvidence> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {

        imageRepository.save(ImageEvidence.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .image(ImageUtility.compressImage(file.getBytes()))
                .build());

        return ResponseEntity.ok().build();

    }

    @GetMapping(path = {"/info/{name}"})
    public ImageEvidence getImageDetails(@PathVariable("name") String name) throws IOException {

        final Optional<ImageEvidence> dbImage = imageRepository.findByName(name);

        return ImageEvidence.builder()
                .name(dbImage.get().getName())
                .type(dbImage.get().getType())
                .image(ImageUtility.decompressImage(dbImage.get().getImage())).build();
    }

    @GetMapping(path = {"/{name}"})
    public ResponseEntity<byte[]> getImage(@PathVariable("name") String name) throws IOException {

        final Optional<ImageEvidence> dbImage = imageRepository.findByName(name);

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(dbImage.get().getType()))
                .body(ImageUtility.decompressImage(dbImage.get().getImage()));
    }

}
