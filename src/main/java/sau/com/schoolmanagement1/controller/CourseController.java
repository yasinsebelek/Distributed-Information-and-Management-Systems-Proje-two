package sau.com.schoolmanagement1.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sau.com.schoolmanagement1.dto.CourseDTO;
import sau.com.schoolmanagement1.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController (CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse (@Valid @RequestBody CourseDTO courseDTO){
        CourseDTO createdCourse = courseService.createCourse(courseDTO);
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(createdCourse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById (@PathVariable Long id){
        CourseDTO courseDTO = courseService.getCourseById(id);
        return ResponseEntity.status(HttpStatus.OK).body(courseDTO);
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<CourseDTO> courseDTOS = courseService.getAllStudents();
        return ResponseEntity.status(HttpStatus.OK).body(courseDTOS);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<CourseDTO> updateCourse(@PathVariable Long id,
                                                   @Valid @RequestBody CourseDTO courseDTO) {

        CourseDTO updateCourseDTO = courseService.updateCourse(id, courseDTO);

        return ResponseEntity.status(HttpStatus.OK).body(updateCourseDTO);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);

        return ResponseEntity.noContent().build();
    }

}
