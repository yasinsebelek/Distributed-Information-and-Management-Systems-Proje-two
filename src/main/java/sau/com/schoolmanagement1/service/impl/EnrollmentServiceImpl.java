package sau.com.schoolmanagement1.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sau.com.schoolmanagement1.dto.EnrollmentRequestDTO;
import sau.com.schoolmanagement1.dto.EnrollmentResponseDTO;
import sau.com.schoolmanagement1.exception.ErrorMessages;
import sau.com.schoolmanagement1.exception.ResourceAlreadyException;
import sau.com.schoolmanagement1.exception.ResourceNotFoundException;
import sau.com.schoolmanagement1.mapper.EnrollmentMapper;
import sau.com.schoolmanagement1.model.Course;
import sau.com.schoolmanagement1.model.Enrollment;
import sau.com.schoolmanagement1.model.Student;
import sau.com.schoolmanagement1.repository.CourseRepository;
import sau.com.schoolmanagement1.repository.EnrollmentRepository;
import sau.com.schoolmanagement1.repository.StudentRepository;
import sau.com.schoolmanagement1.service.EnrollmentService;

import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;


    public  EnrollmentServiceImpl (EnrollmentRepository enrollmentRepository,
                                   StudentRepository studentRepository,
                                   CourseRepository courseRepository) {

        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public EnrollmentResponseDTO createEnrollment(EnrollmentRequestDTO enrollmentRequestDTO) {

        // kayıt yapaçağımız öğrenci varmı
        Student student = studentRepository.findById(enrollmentRequestDTO.getStudentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ErrorMessages.studentNotFound(enrollmentRequestDTO.getStudentId())
                        ));
        // kayıt yapacağımız kurs varmı
        Course course = courseRepository.findById(enrollmentRequestDTO.getCourseId())
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                ErrorMessages.courseNotFound(enrollmentRequestDTO.getCourseId())
                        ));
        // çakışma varmı diye bakaıyoz
        if (enrollmentRepository.existsByStudentIdAndCourseIdAndAcademicYearAndTerm(
                enrollmentRequestDTO.getStudentId(),
                enrollmentRequestDTO.getCourseId(),
                enrollmentRequestDTO.getAcademicYear(),
                enrollmentRequestDTO.getTerm()
        )){
            throw new ResourceAlreadyException(
                    ErrorMessages.enrollmentAlreadyExists(
                            enrollmentRequestDTO.getStudentId(),
                            enrollmentRequestDTO.getCourseId(),
                            enrollmentRequestDTO.getAcademicYear(),
                            enrollmentRequestDTO.getTerm()
                    )
            );
        }

        //DTO -> Entity
        Enrollment enrollment = new Enrollment();
        enrollment.setClassDate(enrollmentRequestDTO.getClassDate());
        enrollment.setAcademicYear(enrollmentRequestDTO.getAcademicYear());
        enrollment.setTerm(enrollmentRequestDTO.getTerm());
        enrollment.setTuition(enrollmentRequestDTO.getTuition());
        enrollment.setAttendance(enrollmentRequestDTO.getAttendance());
        enrollment.setStudent(student);
        enrollment.setCourse(course);


        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        //Entity -> DTO
        return EnrollmentMapper.toDTO(savedEnrollment);
    }

    @Override
    @Transactional()
    public List<EnrollmentResponseDTO> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentRepository.findAll();

         return EnrollmentMapper.toDTOList(enrollments);
    }


    @Override
    @Transactional()
    public EnrollmentResponseDTO getEnrollmentById(Long id) {

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                                ErrorMessages.enrollmentNotFound(id)
                ));

        return EnrollmentMapper.toDTO(enrollment);

    }

    @Override
    public EnrollmentResponseDTO updateEnrollment(Long id,
                                                  EnrollmentRequestDTO enrollmentRequestDTO) {

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorMessages.enrollmentNotFound(id)
                ));

        Student student = studentRepository.findById(enrollmentRequestDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorMessages.studentNotFound(enrollmentRequestDTO.getStudentId())
                ));

        Course course = courseRepository.findById(enrollmentRequestDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorMessages.courseNotFound(enrollmentRequestDTO.getCourseId())
                ) );

        boolean enrollmentChanged =
                !enrollment.getStudent().getId().equals(enrollmentRequestDTO.getStudentId()) ||
                !enrollment.getCourse().getId().equals(enrollmentRequestDTO.getCourseId()) ||
                !enrollment.getAcademicYear().equals(enrollmentRequestDTO.getAcademicYear())||
                !enrollment.getTerm().equals(enrollmentRequestDTO.getTerm());

        if (enrollmentChanged &&
                enrollmentRepository.existsByStudentIdAndCourseIdAndAcademicYearAndTerm(
                        enrollmentRequestDTO.getStudentId(),
                        enrollmentRequestDTO.getCourseId(),
                        enrollmentRequestDTO.getAcademicYear(),
                        enrollmentRequestDTO.getTerm()
                )) {
            throw new ResourceAlreadyException(
                    ErrorMessages.enrollmentAlreadyExists(
                            enrollmentRequestDTO.getStudentId(),
                            enrollmentRequestDTO.getCourseId(),
                            enrollmentRequestDTO.getAcademicYear(),
                            enrollmentRequestDTO.getTerm()
                    )
            );
        }

        enrollment.setClassDate(enrollmentRequestDTO.getClassDate());
        enrollment.setAcademicYear(enrollmentRequestDTO.getAcademicYear());
        enrollment.setTerm(enrollmentRequestDTO.getTerm());
        enrollment.setTuition(enrollmentRequestDTO.getTuition());
        enrollment.setAttendance(enrollmentRequestDTO.getAttendance());
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

        return EnrollmentMapper.toDTO(updatedEnrollment);

    }

    @Override
    public void deleteEnrollment(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorMessages.enrollmentNotFound(id)
                ));

        enrollmentRepository.delete(enrollment);
    }

}


