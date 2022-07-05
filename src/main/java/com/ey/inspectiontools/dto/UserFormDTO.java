package com.ey.inspectiontools.dto;

import com.ey.inspectiontools.constants.CustomDatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFormDTO {

    private Long id;
    @NotBlank(message = "Field cannot be empty or null")
    @Size(min = 5, message = "Field needs to be longer than 5 caracteres")
    private String username;
    @NotBlank(message = "Field cannot be empty or null")
    @Size(min = 5, message = "Field needs to be longer than 5 caracteres")
    private String password;
    private String role;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CustomDatePattern.LOCAL_DATE)
    private LocalDate created_at;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CustomDatePattern.LOCAL_DATE)
    private LocalDate updated_at;
    private ProfileDTO profile;

}
