package sau.com.schoolmanagement1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sau.com.schoolmanagement1.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByStudentNumber(String studentNumber);
}
