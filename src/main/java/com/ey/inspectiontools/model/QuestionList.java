package com.ey.inspectiontools.model;

import lombok.Data;

import javax.persistence.*;

/** Question List class entity
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
@Data
@Entity(name = "question_list")
public class QuestionList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String text;
    private Long category_id;



}
