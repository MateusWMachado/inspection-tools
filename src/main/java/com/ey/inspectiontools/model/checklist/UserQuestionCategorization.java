package com.ey.inspectiontools.model.checklist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/** User Question Categorization class entity
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
@Entity
@Table(name = "user_question_categorization")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQuestionCategorization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String category;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private List<UserQuestionList> questions;
}
