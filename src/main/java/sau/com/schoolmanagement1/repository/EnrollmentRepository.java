package sau.com.schoolmanagement1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sau.com.schoolmanagement1.model.Enrollment;
import sau.com.schoolmanagement1.model.enums.Term;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByStudentIdAndCourseIdAndAcademicYearAndTerm(
            Long studentId,
            Long courseId,
            Integer academicYear,
            Term term
    );

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByCourseId(Long courseId);


}
