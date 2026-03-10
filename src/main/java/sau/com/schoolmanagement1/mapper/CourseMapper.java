package sau.com.schoolmanagement1.mapper;

import sau.com.schoolmanagement1.dto.CourseDTO;
import sau.com.schoolmanagement1.model.Course;

import java.util.List;
import java.util.stream.Collectors;

public class CourseMapper {

    public static CourseDTO toDTO(Course course) {
        if (course == null) {
            return null;
        }

        return new CourseDTO(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getSemester(),
                course.getCourseCode()
        );
    }

    public static Course toEntity(CourseDTO courseDTO) {
        if (courseDTO == null) {
            return null;
        }

        Course course = new Course();
        course.setId(courseDTO.getId());
        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());
        course.setSemester(courseDTO.getSemester());
        course.setCourseCode(courseDTO.getCourseCode());

        return course;
    }

    public static List<CourseDTO> toDTOList(List<Course> courses) {
        if (courses == null) {
            return null;
        }

        return courses.stream()
                .map(CourseMapper::toDTO)
                .collect(Collectors.toList());
    }
}