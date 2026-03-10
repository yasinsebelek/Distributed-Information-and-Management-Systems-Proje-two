package sau.com.schoolmanagement1.mapper;

import sau.com.schoolmanagement1.dto.StudentDTO;
import sau.com.schoolmanagement1.model.Student;

import java.util.List;
import java.util.stream.Collectors;

public class StudentMapper {

    public static StudentDTO toDTO(Student student) {
        if (student == null) {
            return null;
        }

        return new StudentDTO(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getDepartment(),
                student.getStudentNumber()
        );
    }

    public static Student toEntity(StudentDTO studentDTO) {
        if (studentDTO == null) {
            return null;
        }

        Student student = new Student();
        student.setId(studentDTO.getId());
        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        student.setDepartment(studentDTO.getDepartment());
        student.setStudentNumber(studentDTO.getStudentNumber());

        return student;
    }

    public static List<StudentDTO> toDTOList(List<Student> students) {
        if (students == null) {
            return null;
        }

        return students.stream()
                .map(StudentMapper::toDTO)
                .collect(Collectors.toList());
    }
}