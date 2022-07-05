package com.ey.inspectiontools.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Question List class DTO
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionListDTO {

    private Long id;
    private String name;
    private String text;
    private Long category_id;
}
