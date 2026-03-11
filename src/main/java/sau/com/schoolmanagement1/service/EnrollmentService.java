package sau.com.schoolmanagement1.service;

import sau.com.schoolmanagement1.dto.EnrollmentRequestDTO;
import sau.com.schoolmanagement1.dto.EnrollmentResponseDTO;

import java.util.List;

public interface EnrollmentService {

    EnrollmentResponseDTO createEnrollment(EnrollmentRequestDTO enrollmentRequestDTO);

    List<EnrollmentResponseDTO> getAllEnrollments();

    EnrollmentResponseDTO getEnrollmentById(Long id);

    EnrollmentResponseDTO updateEnrollment(Long id, EnrollmentRequestDTO enrollmentRequestDTO);

    void deleteEnrollment(Long id);
}