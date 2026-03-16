package sau.com.schoolmanagement1.service;

import sau.com.schoolmanagement1.dto.CourseDTO;

import java.util.List;

public interface CourseService {

    CourseDTO createCourse(CourseDTO courseDTO);

    CourseDTO getCourseById(Long id);

    List<CourseDTO> getAllCourses();

    CourseDTO updateCourse(Long id, CourseDTO courseDTO);

    void deleteCourse(Long id);


}
