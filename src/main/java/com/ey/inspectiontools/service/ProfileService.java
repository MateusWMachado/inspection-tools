package com.ey.inspectiontools.service;

import com.ey.inspectiontools.dto.ProfileDTO;
import com.ey.inspectiontools.exception.UsernameNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

/** Interface representing a Profile service operations contract
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
public interface ProfileService {

    ResponseEntity<String> createProfile(String username, ProfileDTO profile, UriComponentsBuilder uriBuilder) throws UsernameNotFoundException;

    ResponseEntity<ProfileDTO> updateProfile(String username, ProfileDTO profile) throws UsernameNotFoundException;

    ResponseEntity<ProfileDTO> findProfile(String username) throws UsernameNotFoundException;

}
