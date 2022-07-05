package com.ey.inspectiontools.dto.checklistDTO;


import com.ey.inspectiontools.util.ImageUtility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageEvidenceDTO {

    private Long id;
    private String name;
    private String type;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private byte[] image;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MultipartFile file;

}
