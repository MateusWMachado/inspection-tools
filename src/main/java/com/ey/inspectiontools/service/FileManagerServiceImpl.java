package com.ey.inspectiontools.service;

import com.ey.inspectiontools.dto.FileResponseDTO;
import com.ey.inspectiontools.model.FileEntity;
import com.ey.inspectiontools.repository.FileManagerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileManagerServiceImpl implements FileManagerService{

    @Autowired
    private FileManagerRepository fileManagerRepository;

    @Override
    public ResponseEntity<String> uploadFile(MultipartFile file) throws IOException {
        FileEntity fileEntity = FileEntity.builder()
                .name(StringUtils.cleanPath(file.getName()))
                .contentType(file.getContentType())
                .size(file.getSize())
                .data(file.getBytes())
                .build();
        try {
            fileManagerRepository.save(fileEntity);
            return ResponseEntity.ok().body("File created");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    public ResponseEntity<byte[]> downloadFile(String uuid) {
        Optional<FileEntity> foundedFile = fileManagerRepository.findById(uuid);

        if (!foundedFile.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        FileEntity fileEntity = foundedFile.get();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getName() + "\"")
                .contentType(MediaType.valueOf(fileEntity.getContentType()))
                .body(fileEntity.getData());
    }

    @Override
    public List<FileResponseDTO> getAllfiles() {
        List<FileEntity> all = fileManagerRepository.findAll();
        return all.stream().map(this::mapToFileResponse).collect(Collectors.toList());

    }

    private FileResponseDTO mapToFileResponse(FileEntity fileEntity) {
        String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(fileEntity.getId())
                .toUriString();

        return FileResponseDTO.builder()
                .id(fileEntity.getId())
                .name(fileEntity.getName())
                .contentType(fileEntity.getContentType())
                .size(fileEntity.getSize())
                .url(downloadURL)
                .build();
    }
}
