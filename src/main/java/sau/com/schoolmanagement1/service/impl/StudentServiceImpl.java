package sau.com.schoolmanagement1.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sau.com.schoolmanagement1.dto.StudentDTO;
import sau.com.schoolmanagement1.exception.ErrorMessages;
import sau.com.schoolmanagement1.exception.ResourceAlreadyException;
import sau.com.schoolmanagement1.exception.ResourceNotFoundException;
import sau.com.schoolmanagement1.mapper.StudentMapper;
import sau.com.schoolmanagement1.model.Student;
import sau.com.schoolmanagement1.repository.StudentRepository;
import sau.com.schoolmanagement1.service.StudentService;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger log =
            LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {

        log.info("STUDENT CREATE START | studentNumber={}", studentDTO.getStudentNumber());

        if (studentRepository.existsByStudentNumber(studentDTO.getStudentNumber())) {
            log.warn("STUDENT CREATE CONFLICT | studentNumber={}", studentDTO.getStudentNumber());

            throw new ResourceAlreadyException(
                    ErrorMessages.studentAlreadyExists(studentDTO.getStudentNumber())
            );
        }

        Student student = StudentMapper.toEntity(studentDTO);
        Student savedStudent = studentRepository.save(student);

        log.info("STUDENT CREATE SUCCESS | id={} | studentNumber={}",
                savedStudent.getId(),
                savedStudent.getStudentNumber());

        return StudentMapper.toDTO(savedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getAllStudents() {

        log.info("STUDENT GET ALL START");

        List<Student> students = studentRepository.findAll();

        log.info("STUDENT GET ALL SUCCESS | count={}", students.size());

        return StudentMapper.toDTOList(students);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDTO getStudentById(Long id) {

        log.info("STUDENT GET BY ID START | id={}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("STUDENT GET BY ID NOT FOUND | id={}", id);
                    return new ResourceNotFoundException(
                            ErrorMessages.studentNotFound(id)
                    );
                });

        log.info("STUDENT GET BY ID SUCCESS | id={} | studentNumber={}",
                student.getId(),
                student.getStudentNumber());

        return StudentMapper.toDTO(student);
    }

    @Override
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {

        log.info("STUDENT UPDATE START | id={}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("STUDENT UPDATE NOT FOUND | id={}", id);
                    return new ResourceNotFoundException(
                            ErrorMessages.studentNotFound(id)
                    );
                });

        if (!student.getStudentNumber().equals(studentDTO.getStudentNumber())) {
            if (studentRepository.existsByStudentNumber(studentDTO.getStudentNumber())) {
                log.warn("STUDENT UPDATE CONFLICT | id={} | studentNumber={}",
                        id,
                        studentDTO.getStudentNumber());

                throw new ResourceAlreadyException(
                        ErrorMessages.studentAlreadyExists(studentDTO.getStudentNumber())
                );
            }
        }

        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        student.setDepartment(studentDTO.getDepartment());
        student.setStudentNumber(studentDTO.getStudentNumber());

        Student updatedStudent = studentRepository.save(student);

        log.info("STUDENT UPDATE SUCCESS | id={} | studentNumber={}",
                updatedStudent.getId(),
                updatedStudent.getStudentNumber());

        return StudentMapper.toDTO(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {

        log.info("STUDENT DELETE START | id={}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("STUDENT DELETE NOT FOUND | id={}", id);
                    return new ResourceNotFoundException(
                            ErrorMessages.studentNotFound(id)
                    );
                });

        studentRepository.delete(student);

        log.info("STUDENT DELETE SUCCESS | id={} | studentNumber={}",
                student.getId(),
                student.getStudentNumber());
    }
}