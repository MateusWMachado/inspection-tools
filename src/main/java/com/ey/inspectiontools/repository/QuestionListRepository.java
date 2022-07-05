package com.ey.inspectiontools.repository;

import com.ey.inspectiontools.model.QuestionList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Interface representing a Question List repository
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
@Repository
public interface QuestionListRepository extends JpaRepository<QuestionList, Long> {
}
