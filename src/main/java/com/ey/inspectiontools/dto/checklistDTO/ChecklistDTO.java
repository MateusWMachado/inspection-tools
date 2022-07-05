package com.ey.inspectiontools.dto.checklistDTO;

import com.ey.inspectiontools.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/** Checklist class DTO
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChecklistDTO {

    private Long id;
    private LocalDate created_at;
    private UserDTO user;
    private List<UserQuestionCategorizationDTO> userCategoryChecklists;
}
