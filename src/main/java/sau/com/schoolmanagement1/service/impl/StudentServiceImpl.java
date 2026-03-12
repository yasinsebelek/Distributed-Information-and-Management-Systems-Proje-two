package sau.com.schoolmanagement1.service.impl;

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
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {

        this.studentRepository = studentRepository;
    }

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {

        if (studentRepository.existsByStudentNumber(studentDTO.getStudentNumber())) {
            throw new ResourceAlreadyException(
                    ErrorMessages.studentAlreadyExists(studentDTO.getStudentNumber())
            );
        }

        Student student = StudentMapper.toEntity(studentDTO);
        Student savedStudent = studentRepository.save(student);

        return StudentMapper.toDTO(savedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getAllStudents() {

        List<Student> students = studentRepository.findAll();
        return StudentMapper.toDTOList(students);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDTO getStudentById(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ErrorMessages.studentNotFound(id)
                        )
                );

        return StudentMapper.toDTO(student);
    }

    @Override
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ErrorMessages.studentNotFound(id)
                        )
                );

        if (!student.getStudentNumber().equals(studentDTO.getStudentNumber())) {

            if (studentRepository.existsByStudentNumber(studentDTO.getStudentNumber())) {
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

        return StudentMapper.toDTO(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ErrorMessages.studentNotFound(id)
                        )
                );

        studentRepository.delete(student);
    }



}