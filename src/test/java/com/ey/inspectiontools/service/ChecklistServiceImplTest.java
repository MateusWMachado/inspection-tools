package com.ey.inspectiontools.service;

import com.ey.inspectiontools.dto.UserDTO;
import com.ey.inspectiontools.dto.UserFormDTO;
import com.ey.inspectiontools.dto.checklistDTO.*;
import com.ey.inspectiontools.exception.ChecklistNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/** Checklist service test class
 *
 * @author Mateus W. Machado
 * @since 31/05/2022
 * @version 1.0.0
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ChecklistServiceImplTest {

    @Autowired
    private final UserService userService = new UserServiceImpl();

    @Autowired
    private final ChecklistService checklistService = new ChecklistServiceImpl();

    private String userID;

    private ChecklistDTO mockChecklistDTO;
    @BeforeEach
    void createChecklistBeforeEachTestSetup() throws IOException {
        UserDTO userFormDTO = UserDTO.builder()
                .created_at(LocalDate.now())
                .password("MockAccount")
                .role("USER")
                .username("MockAccount")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
        );

        ImageEvidenceDTO imageEvidenceDTO = ImageEvidenceDTO.builder()
                .file(file)
                .build();

        userService.createAccount(userFormDTO, UriComponentsBuilder.newInstance());

        QuestionAnswerDTO questionAnswerDTO = QuestionAnswerDTO.builder()
                .answer("Não")
                .imageEvidence(Collections.singletonList(imageEvidenceDTO))
                .build();

        UserQuestionListDTO userQuestionListDTO = UserQuestionListDTO.builder()
                .name("Materiais Enferrujados")
                .text("Os materiais estão enferrujados?")
                .questionAnswer(questionAnswerDTO)
                .build();

        UserQuestionCategorizationDTO userQuestionCategorizationDTO = UserQuestionCategorizationDTO.builder()
                .category("Administração")
                .questions(Collections.singletonList(userQuestionListDTO))
                .build();

        ChecklistDTO checklistDTO = ChecklistDTO.builder()
                .created_at(LocalDate.now())
                .userCategoryChecklists(Collections.singletonList(userQuestionCategorizationDTO))
                .build();

        ResponseEntity<String> createdChecklistSetup = checklistService.createChecklist(checklistDTO, "MockAccount", UriComponentsBuilder.newInstance());

        this.userID = Objects.requireNonNull(createdChecklistSetup.getBody()).substring(27);
        this.mockChecklistDTO = checklistDTO;
    }

    @Test
    @DisplayName("Should find all checklists, return status code - OK and assert all the conditions")
    void findAllChecklistsTest() {
        List<ChecklistDTO> foundedAllChecklists = checklistService.findAllChecklists();

        assertNotNull(foundedAllChecklists);
        assertTrue(foundedAllChecklists.size() > 0);
    }

    @Test
    @DisplayName("Should create the checklist, return status code - CREATED and assert all the conditions")
    void createChecklistTest() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
        );

        ImageEvidenceDTO imageEvidenceDTO = ImageEvidenceDTO.builder()
                .file(file)
                .build();

        QuestionAnswerDTO questionAnswerDTO = QuestionAnswerDTO.builder()
                .answer("Não")
                .imageEvidence(Collections.singletonList(imageEvidenceDTO))
                .build();

        UserQuestionListDTO userQuestionListDTO = UserQuestionListDTO.builder()
                .name("Materiais Enferrujados")
                .text("Os materiais estão enferrujados?")
                .questionAnswer(questionAnswerDTO)
                .build();

        UserQuestionCategorizationDTO userQuestionCategorizationDTO = UserQuestionCategorizationDTO.builder()
                .category("Administração")
                .questions(Collections.singletonList(userQuestionListDTO))
                .build();

        ChecklistDTO checklistDTO = ChecklistDTO.builder()
                .created_at(LocalDate.now())
                .userCategoryChecklists(Collections.singletonList(userQuestionCategorizationDTO))
                .build();

        ResponseEntity<String> createdChecklist = checklistService.createChecklist(checklistDTO, "MockAccount", UriComponentsBuilder.newInstance());

        assertNotNull(createdChecklist);
        assertEquals(HttpStatus.CREATED, createdChecklist.getStatusCode());
        assertTrue(Objects.requireNonNull(createdChecklist.getBody()).contains("Created checklist with id: "));
    }

    @Test
    @DisplayName("Should delete the checklist with ID, return status code - OK and assert all the conditions")
    void deleteChecklistTest() throws ChecklistNotFoundException {
        ResponseEntity<?> responseEntity = checklistService.deleteChecklist(Long.valueOf(userID));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should find a checklist by ID, return status code - OK and assert all the conditions")
    void findChecklistByIdTest() throws ChecklistNotFoundException {
        ResponseEntity<ChecklistDTO> foundedChecklistByID = checklistService.findChecklistById(Long.valueOf(userID));

        assertNotNull(foundedChecklistByID);
        assertEquals(HttpStatus.OK, foundedChecklistByID.getStatusCode());
        assertEquals(this.mockChecklistDTO.getUser().getUsername(), Objects.requireNonNull(foundedChecklistByID.getBody()).getUser().getUsername());
        assertEquals(this.mockChecklistDTO.getUserCategoryChecklists().get(0).getCategory(),
                    Objects.requireNonNull(foundedChecklistByID.getBody().getUserCategoryChecklists().get(0).getCategory()));
    }

    @Test
    @DisplayName("Should throw an IllegalArgumentException if can not find the user file")
    void shouldThrowNewExceptionIfCanNotFindFileIntoTheChecklist() {

        ImageEvidenceDTO imageEvidenceDTO = ImageEvidenceDTO.builder()
                .build();

        QuestionAnswerDTO questionAnswerDTO = QuestionAnswerDTO.builder()
                .answer("Não")
                .imageEvidence(Collections.singletonList(imageEvidenceDTO))
                .build();

        UserQuestionListDTO userQuestionListDTO = UserQuestionListDTO.builder()
                .name("Materiais Enferrujados")
                .text("Os materiais estão enferrujados?")
                .questionAnswer(questionAnswerDTO)
                .build();

        UserQuestionCategorizationDTO userQuestionCategorizationDTO = UserQuestionCategorizationDTO.builder()
                .category("Administração")
                .questions(Collections.singletonList(userQuestionListDTO))
                .build();

        ChecklistDTO checklistDTO = ChecklistDTO.builder()
                .created_at(LocalDate.now())
                .userCategoryChecklists(Collections.singletonList(userQuestionCategorizationDTO))
                .build();

        assertThrows(IllegalArgumentException.class, () -> checklistService.createChecklist(checklistDTO, "MockAccount", UriComponentsBuilder.newInstance()));
    }
    @Test
    @DisplayName("Should throw an UsernameNotFoundException if can not find the profile by username")
    void shouldThrowNewExceptionIfCanNotFindChecklistById() {
        assertThrows(ChecklistNotFoundException.class, () -> checklistService.findChecklistById(0L));
    }
}