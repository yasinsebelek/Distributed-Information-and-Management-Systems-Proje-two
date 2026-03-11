package sau.com.schoolmanagement1.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sau.com.schoolmanagement1.dto.StudentDTO;
import sau.com.schoolmanagement1.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        StudentDTO createdStudent = studentService.createStudent(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> studentDTOS = studentService.getAllStudents();
        return ResponseEntity.status(HttpStatus.OK).body(studentDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById( @PathVariable Long id) {
        StudentDTO studentDTO = studentService.getStudentById(id);
        return ResponseEntity.status(HttpStatus.OK).body(studentDTO);

    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id,
                                    @Valid @RequestBody StudentDTO studentDTO) {

        StudentDTO updatedStudentDTO = studentService.updateStudent(id, studentDTO);

        return ResponseEntity.status(HttpStatus.OK).body(updatedStudentDTO);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

}
