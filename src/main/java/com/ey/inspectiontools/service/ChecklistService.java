package com.ey.inspectiontools.service;

import com.ey.inspectiontools.dto.checklistDTO.ChecklistDTO;
import com.ey.inspectiontools.exception.ChecklistNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;

/** Interface representing a Checklist Service operations contract
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
public interface ChecklistService {

    List<ChecklistDTO> findAllChecklists();

    ResponseEntity<String> createChecklist(ChecklistDTO checklistDTO, String username, UriComponentsBuilder uriBuilder) throws IOException;

    ResponseEntity<?> deleteChecklist(Long id) throws ChecklistNotFoundException;

    ResponseEntity<ChecklistDTO> findChecklistById(Long id) throws ChecklistNotFoundException;
}
