package com.ey.inspectiontools.model.checklist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/** User Question List class entity
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
@Entity
@Table(name = "user_question_list")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQuestionList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String text;
    private Long category_id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "answer_id")
    private QuestionAnswer questionAnswer;



}
