package com.ey.inspectiontools.dto.checklistDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** User Question List class DTO
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserQuestionListDTO {

    private Long id;
    private String name;
    private String text;
    private Long category_id;
    private QuestionAnswerDTO questionAnswer;
}
