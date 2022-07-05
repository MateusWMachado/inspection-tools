package com.ey.inspectiontools.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileResponseDTO {

    private String id;
    private String name;
    private Long size;
    private String contentType;
    private String url;
}
