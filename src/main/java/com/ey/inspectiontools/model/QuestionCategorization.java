package com.ey.inspectiontools.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/** Question Categorization class entity
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
@Entity
@Data
@Table(name = "question_categorization")
public class QuestionCategorization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String category;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private List<QuestionList> questionLists;

}
