package com.ey.inspectiontools.service;

import com.ey.inspectiontools.dto.UserDTO;
import com.ey.inspectiontools.dto.checklistDTO.ChecklistDTO;
import com.ey.inspectiontools.dto.checklistDTO.ImageEvidenceDTO;
import com.ey.inspectiontools.exception.ChecklistNotFoundException;
import com.ey.inspectiontools.exception.FileNotFoundException;
import com.ey.inspectiontools.model.User;
import com.ey.inspectiontools.model.checklist.Checklist;
import com.ey.inspectiontools.repository.ChecklistRepository;
import com.ey.inspectiontools.repository.UserRepository;
import com.ey.inspectiontools.util.ImageUtility;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/** ChecklistService contract implementation class
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
public class ChecklistServiceImpl implements ChecklistService{

    private ChecklistRepository checklistRepository;
    private UserRepository userRepository;
    private ModelMapper mapper;

    /** Method to find all checklists
     *
     * @author Mateus W. Machado
     * @return a list of ChecklistDTO
     */
    @Override
    public List<ChecklistDTO> findAllChecklists(){
        List<Checklist> all = checklistRepository.findAll();
        return all.stream().map(checklist -> mapper.map(checklist, ChecklistDTO.class)).collect(Collectors.toList());
    }

    /** Method to create a Checklist
     *
     * @author Mateus W. Machado
     * @param checklistDTO the checklist form
     * @param username the user to be responsible for the checklist
     * @param uriBuilder used for constructing URI
     * @return the method must create the checklist, return a HttpStatus - CREATED and a string with a message
     */
    @Override
    public ResponseEntity<String> createChecklist(ChecklistDTO checklistDTO, String username, UriComponentsBuilder uriBuilder) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

        setImagesList(checklistDTO);

        checklistDTO.setUser(mapper.map(user, UserDTO.class));
        checklistDTO.setCreated_at(LocalDate.now());
        Checklist savedChecklist = checklistRepository.save(mapper.map(checklistDTO, Checklist.class));

        URI uri = uriBuilder.path("/api/v1/user/checklist/{id}").buildAndExpand(savedChecklist.getId()).toUri();
        return ResponseEntity.created(uri).body("Created checklist with id: " + savedChecklist.getId());
    }

    /** Method to get the user's ImageEvidence list and set the correct values
     *
     * @author Mateus W. Machado
     * @param checklistDTO the checklist form
     */
    private void setImagesList(ChecklistDTO checklistDTO) {
        checklistDTO.getUserCategoryChecklists()
                .forEach(userQuestionCategorizationDTO -> userQuestionCategorizationDTO.getQuestions()
                        .forEach(userQuestionListDTO -> userQuestionListDTO.getQuestionAnswer().getImageEvidence()
                                .forEach(this::setImageEvidenceDTO)
                        ));
    }

    /** Method to set the images correctly, this method will receive the image that the user adds in the MultipartFile format, set the correct variables and compress the bytes in the ImageEvidenceDTO class
     *
     * @author Mateus W. Machado
     * @param imageEvidenceDTO the image from User
     */
    private void setImageEvidenceDTO(ImageEvidenceDTO imageEvidenceDTO){
        try {
            MultipartFile file = imageEvidenceDTO.getFile();
            imageEvidenceDTO.setName(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
            imageEvidenceDTO.setType(file.getContentType());
            imageEvidenceDTO.setImage(ImageUtility.compressImage(file.getBytes()));
        } catch (NullPointerException | IOException e) {
            throw new IllegalArgumentException("File has to be present");
        }
    }

    /** Method to delete a Checklist by ID
     *
     * @author Mateus W. Machado
     * @param id the ID from Checklist
     * @return if the method finds a checklist it must delete the checklist and return a HttpStatus - OK, if the method does not found it must return a ChecklistNotFoundException
     */
    @Override
    public ResponseEntity<?> deleteChecklist(Long id) throws ChecklistNotFoundException {
        Checklist checklist = verifyIfChecklistExists(id);
        checklistRepository.delete(checklist);
        return ResponseEntity.ok().build();
    }

    /** Method to find a Checklist by ID
     *
     * @author Mateus W. Machado
     * @param id the ID from Checklist
     * @return if the method finds a checklist it must return a HttpStatus - OK and the ChecklistDTO, if the method does not found it must return a ChecklistNotFoundException
     */
    @Override
    public ResponseEntity<ChecklistDTO> findChecklistById(Long id) throws ChecklistNotFoundException {
        Checklist checklist = verifyIfChecklistExists(id);
        return ResponseEntity.ok().body(mapper.map(checklist, ChecklistDTO.class));
    }

    /** Method to verify if the Checklist exists
     *
     * @author Mateus W. Machado
     * @param id the ID from Checklist
     * @return if the method finds a user it must return the Checklist, if the method does not found it must return a ChecklistNotFoundException
     */
    private Checklist verifyIfChecklistExists(Long id) throws ChecklistNotFoundException {
        return checklistRepository.findById(id).orElseThrow(() -> new ChecklistNotFoundException(id));
    }
}
