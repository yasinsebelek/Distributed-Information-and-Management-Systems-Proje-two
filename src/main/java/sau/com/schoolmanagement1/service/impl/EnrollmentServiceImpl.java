package sau.com.schoolmanagement1.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private static final Logger log =
            LoggerFactory.getLogger(EnrollmentServiceImpl.class);

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository,
                                 StudentRepository studentRepository,
                                 CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public EnrollmentResponseDTO createEnrollment(EnrollmentRequestDTO enrollmentRequestDTO) {

        log.info("ENROLLMENT CREATE START | studentId={} | courseId={} | academicYear={} | term={}",
                enrollmentRequestDTO.getStudentId(),
                enrollmentRequestDTO.getCourseId(),
                enrollmentRequestDTO.getAcademicYear(),
                enrollmentRequestDTO.getTerm());

        Student student = studentRepository.findById(enrollmentRequestDTO.getStudentId())
                .orElseThrow(() -> {
                    log.warn("ENROLLMENT CREATE STUDENT NOT FOUND | studentId={}",
                            enrollmentRequestDTO.getStudentId());

                    return new ResourceNotFoundException(
                            ErrorMessages.studentNotFound(enrollmentRequestDTO.getStudentId())
                    );
                });

        Course course = courseRepository.findById(enrollmentRequestDTO.getCourseId())
                .orElseThrow(() -> {
                    log.warn("ENROLLMENT CREATE COURSE NOT FOUND | courseId={}",
                            enrollmentRequestDTO.getCourseId());

                    return new ResourceNotFoundException(
                            ErrorMessages.courseNotFound(enrollmentRequestDTO.getCourseId())
                    );
                });

        if (enrollmentRepository.existsByStudentIdAndCourseIdAndAcademicYearAndTerm(
                enrollmentRequestDTO.getStudentId(),
                enrollmentRequestDTO.getCourseId(),
                enrollmentRequestDTO.getAcademicYear(),
                enrollmentRequestDTO.getTerm()
        )) {
            log.warn("ENROLLMENT CREATE CONFLICT | studentId={} | courseId={} | academicYear={} | term={}",
                    enrollmentRequestDTO.getStudentId(),
                    enrollmentRequestDTO.getCourseId(),
                    enrollmentRequestDTO.getAcademicYear(),
                    enrollmentRequestDTO.getTerm());

            throw new ResourceAlreadyException(
                    ErrorMessages.enrollmentAlreadyExists(
                            enrollmentRequestDTO.getStudentId(),
                            enrollmentRequestDTO.getCourseId(),
                            enrollmentRequestDTO.getAcademicYear(),
                            enrollmentRequestDTO.getTerm()
                    )
            );
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setClassDate(enrollmentRequestDTO.getClassDate());
        enrollment.setAcademicYear(enrollmentRequestDTO.getAcademicYear());
        enrollment.setTerm(enrollmentRequestDTO.getTerm());
        enrollment.setTuition(enrollmentRequestDTO.getTuition());
        enrollment.setAttendance(enrollmentRequestDTO.getAttendance());
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        log.info("ENROLLMENT CREATE SUCCESS | id={} | studentId={} | courseId={}",
                savedEnrollment.getId(),
                savedEnrollment.getStudent().getId(),
                savedEnrollment.getCourse().getId());

        return EnrollmentMapper.toDTO(savedEnrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> getAllEnrollments() {

        log.info("ENROLLMENT GET ALL START");

        List<Enrollment> enrollments = enrollmentRepository.findAll();

        log.info("ENROLLMENT GET ALL SUCCESS | count={}", enrollments.size());

        return EnrollmentMapper.toDTOList(enrollments);
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentResponseDTO getEnrollmentById(Long id) {

        log.info("ENROLLMENT GET BY ID START | id={}", id);

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("ENROLLMENT GET BY ID NOT FOUND | id={}", id);
                    return new ResourceNotFoundException(
                            ErrorMessages.enrollmentNotFound(id)
                    );
                });

        log.info("ENROLLMENT GET BY ID SUCCESS | id={} | studentId={} | courseId={}",
                enrollment.getId(),
                enrollment.getStudent().getId(),
                enrollment.getCourse().getId());

        return EnrollmentMapper.toDTO(enrollment);
    }

    @Override
    @Transactional
    public EnrollmentResponseDTO updateEnrollment(Long id,
                                                  EnrollmentRequestDTO enrollmentRequestDTO) {

        log.info("ENROLLMENT UPDATE START | id={}", id);

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("ENROLLMENT UPDATE NOT FOUND | id={}", id);
                    return new ResourceNotFoundException(
                            ErrorMessages.enrollmentNotFound(id)
                    );
                });

        Student student = studentRepository.findById(enrollmentRequestDTO.getStudentId())
                .orElseThrow(() -> {
                    log.warn("ENROLLMENT UPDATE STUDENT NOT FOUND | studentId={}",
                            enrollmentRequestDTO.getStudentId());

                    return new ResourceNotFoundException(
                            ErrorMessages.studentNotFound(enrollmentRequestDTO.getStudentId())
                    );
                });

        Course course = courseRepository.findById(enrollmentRequestDTO.getCourseId())
                .orElseThrow(() -> {
                    log.warn("ENROLLMENT UPDATE COURSE NOT FOUND | courseId={}",
                            enrollmentRequestDTO.getCourseId());

                    return new ResourceNotFoundException(
                            ErrorMessages.courseNotFound(enrollmentRequestDTO.getCourseId())
                    );
                });

        boolean enrollmentChanged =
                !enrollment.getStudent().getId().equals(enrollmentRequestDTO.getStudentId()) ||
                        !enrollment.getCourse().getId().equals(enrollmentRequestDTO.getCourseId()) ||
                        !enrollment.getAcademicYear().equals(enrollmentRequestDTO.getAcademicYear()) ||
                        !enrollment.getTerm().equals(enrollmentRequestDTO.getTerm());

        if (enrollmentChanged &&
                enrollmentRepository.existsByStudentIdAndCourseIdAndAcademicYearAndTerm(
                        enrollmentRequestDTO.getStudentId(),
                        enrollmentRequestDTO.getCourseId(),
                        enrollmentRequestDTO.getAcademicYear(),
                        enrollmentRequestDTO.getTerm()
                )) {

            log.warn("ENROLLMENT UPDATE CONFLICT | id={} | studentId={} | courseId={} | academicYear={} | term={}",
                    id,
                    enrollmentRequestDTO.getStudentId(),
                    enrollmentRequestDTO.getCourseId(),
                    enrollmentRequestDTO.getAcademicYear(),
                    enrollmentRequestDTO.getTerm());

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

        log.info("ENROLLMENT UPDATE SUCCESS | id={} | studentId={} | courseId={}",
                updatedEnrollment.getId(),
                updatedEnrollment.getStudent().getId(),
                updatedEnrollment.getCourse().getId());

        return EnrollmentMapper.toDTO(updatedEnrollment);
    }

    @Override
    public void deleteEnrollment(Long id) {

        log.info("ENROLLMENT DELETE START | id={}", id);

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("ENROLLMENT DELETE NOT FOUND | id={}", id);
                    return new ResourceNotFoundException(
                            ErrorMessages.enrollmentNotFound(id)
                    );
                });

        enrollmentRepository.delete(enrollment);

        log.info("ENROLLMENT DELETE SUCCESS | id={} | studentId={} | courseId={}",
                enrollment.getId(),
                enrollment.getStudent().getId(),
                enrollment.getCourse().getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> getEnrollmentsByStudentId(Long studentId) {

        log.info("ENROLLMENT GET BY STUDENT ID START | studentId={}", studentId);

        studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    log.warn("ENROLLMENT GET BY STUDENT ID STUDENT NOT FOUND | studentId={}", studentId);
                    return new ResourceNotFoundException(
                            ErrorMessages.studentNotFound(studentId)
                    );
                });

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

        log.info("ENROLLMENT GET BY STUDENT ID SUCCESS | studentId={} | count={}",
                studentId,
                enrollments.size());

        return EnrollmentMapper.toDTOList(enrollments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> getEnrollmentsByCourseId(Long courseId) {

        log.info("ENROLLMENT GET BY COURSE ID START | courseId={}", courseId);

        courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.warn("ENROLLMENT GET BY COURSE ID COURSE NOT FOUND | courseId={}", courseId);
                    return new ResourceNotFoundException(
                            ErrorMessages.courseNotFound(courseId)
                    );
                });

        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);

        log.info("ENROLLMENT GET BY COURSE ID SUCCESS | courseId={} | count={}",
                courseId,
                enrollments.size());

        return EnrollmentMapper.toDTOList(enrollments);
    }
}