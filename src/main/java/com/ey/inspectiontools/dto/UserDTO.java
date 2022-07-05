package com.ey.inspectiontools.dto;

import com.ey.inspectiontools.constants.CustomDatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/** User class DTO
 *
 * @author Mateus W. Machado
 * @since 09/05/2022
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    @NotBlank(message = "Field cannot be empty or null")
    @Size(min = 5, message = "Field needs to be longer than 5 caracteres")
    private String username;

    @NotBlank(message = "Field cannot be empty or null")
    @Size(min = 5, message = "Field needs to be longer than 5 caracteres")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String role;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CustomDatePattern.LOCAL_DATE)
    private LocalDate created_at;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CustomDatePattern.LOCAL_DATE)
    private LocalDate updated_at;
    private ProfileDTO profile;


}
