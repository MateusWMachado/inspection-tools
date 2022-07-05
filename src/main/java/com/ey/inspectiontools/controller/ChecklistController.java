package com.ey.inspectiontools.controller;

import com.ey.inspectiontools.dto.checklistDTO.ChecklistDTO;
import com.ey.inspectiontools.exception.ChecklistNotFoundException;
import com.ey.inspectiontools.service.ChecklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;

/** Class that represents a controller to handle information from Checklists
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1")
public class ChecklistController {

    @Autowired
    private ChecklistService checklistService;

    @GetMapping("/user/checklist")
    public List<ChecklistDTO> findAllChecklists(){
        return checklistService.findAllChecklists();
    }

    @GetMapping("/user/checklist/{id}")
    public ResponseEntity<ChecklistDTO> findChecklistById (@PathVariable Long id) throws ChecklistNotFoundException {
        return checklistService.findChecklistById(id);
    }

    @PostMapping(value = "/user/create-checklist/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createChecklist(@ModelAttribute ChecklistDTO checklistDTO, @PathVariable String username, UriComponentsBuilder uriBuilder) throws IOException {
        return checklistService.createChecklist(checklistDTO, username, uriBuilder);
    }

    @DeleteMapping("/user/checklist/{id}")
    public ResponseEntity<?> deleteChecklist(@PathVariable Long id) throws ChecklistNotFoundException {
        return checklistService.deleteChecklist(id);
    }


}
