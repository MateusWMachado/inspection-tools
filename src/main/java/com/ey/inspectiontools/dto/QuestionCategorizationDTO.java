package com.ey.inspectiontools.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** Question Categorization class DTO
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCategorizationDTO {

    private Long id;
    private String category;
    private List<QuestionListDTO> questions;
}
