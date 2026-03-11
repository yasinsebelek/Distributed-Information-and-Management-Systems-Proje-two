package sau.com.schoolmanagement1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sau.com.schoolmanagement1.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByCourseCode(String courseCode);
}

