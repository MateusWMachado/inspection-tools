package com.ey.inspectiontools.controller;

import com.ey.inspectiontools.dto.FileResponseDTO;
import com.ey.inspectiontools.service.FileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/** Class that represents a controller to handle information from Checklists
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1")
public class FileManagerController {

    @Autowired
    private FileManagerService fileManagerService;

    @PostMapping("/user/file")
    public ResponseEntity<String> upload(@RequestParam(name = "file") MultipartFile file) throws IOException {
        return fileManagerService.uploadFile(file);
    }

    @GetMapping("/user/file/{uuid}")
    public ResponseEntity<byte[]> download(@PathVariable String uuid) {
        return fileManagerService.downloadFile(uuid);
    }

    @GetMapping("/user/file")
    public List<FileResponseDTO> list() {
        return fileManagerService.getAllfiles();
    }

}
