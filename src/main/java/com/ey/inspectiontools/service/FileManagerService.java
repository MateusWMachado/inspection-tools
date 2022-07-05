package com.ey.inspectiontools.service;

import com.ey.inspectiontools.dto.FileResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileManagerService {

    ResponseEntity<String> uploadFile(MultipartFile file) throws IOException;

    ResponseEntity<byte[]> downloadFile(String uuid);

    List<FileResponseDTO> getAllfiles();
}
