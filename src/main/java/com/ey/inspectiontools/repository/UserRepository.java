package com.ey.inspectiontools.repository;

import com.ey.inspectiontools.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/** Interface representing an User repository
 *
 * @author Mateus W. Machado
 * @since 09/05/2022
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
