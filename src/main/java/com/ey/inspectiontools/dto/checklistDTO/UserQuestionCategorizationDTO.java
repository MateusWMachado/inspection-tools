package com.ey.inspectiontools.dto.checklistDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** User Question Categorization class DTO
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserQuestionCategorizationDTO {

    private Long id;
    private String category;
    private List<UserQuestionListDTO> questions;
}
