package sau.com.schoolmanagement1.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sau.com.schoolmanagement1.dto.EnrollmentRequestDTO;
import sau.com.schoolmanagement1.dto.EnrollmentResponseDTO;
import sau.com.schoolmanagement1.service.EnrollmentService;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public ResponseEntity<EnrollmentResponseDTO> createEnrollment(
            @Valid @RequestBody EnrollmentRequestDTO enrollmentRequestDTO) {
        EnrollmentResponseDTO createdEnrollment =
                enrollmentService.createEnrollment(enrollmentRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEnrollment);
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentResponseDTO>> getAllEnrollments() {
        List<EnrollmentResponseDTO> enrollments = enrollmentService.getAllEnrollments();

        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDTO> getEnrollmentById(@PathVariable Long id) {
        EnrollmentResponseDTO enrollmentResponseDTO = enrollmentService.getEnrollmentById(id);

        return ResponseEntity.ok(enrollmentResponseDTO);

    }

    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDTO> updateEnrollment(@PathVariable Long id,
                                @Valid @RequestBody EnrollmentRequestDTO enrollmentRequestDTO) {

        EnrollmentResponseDTO updatedEnrollment =
                enrollmentService.updateEnrollment(id, enrollmentRequestDTO);

        return ResponseEntity.ok(updatedEnrollment);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponseDTO>> getEnrollmentsByStudentId(
            @PathVariable Long studentId) {

        List<EnrollmentResponseDTO> enrollmentResponseDTOS =
                enrollmentService.getEnrollmentsByStudentId(studentId);

        return ResponseEntity.ok(enrollmentResponseDTOS);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentResponseDTO>> getEnrollmentsByCourseId(
            @PathVariable Long courseId){

        List<EnrollmentResponseDTO> enrollmentResponseDTOS =
                enrollmentService.getEnrollmentsByCourseId(courseId);

        return ResponseEntity.ok(enrollmentResponseDTOS);
    }


}
