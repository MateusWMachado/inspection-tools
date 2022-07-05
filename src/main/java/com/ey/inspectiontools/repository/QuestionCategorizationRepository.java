package com.ey.inspectiontools.repository;

import com.ey.inspectiontools.model.QuestionCategorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Interface representing a Question Categorization repository
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
@Repository
public interface QuestionCategorizationRepository extends JpaRepository<QuestionCategorization, Long> {
}
