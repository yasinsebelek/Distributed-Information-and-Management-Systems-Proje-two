package sau.com.schoolmanagement1.model;

import jakarta.persistence.*;
import lombok.*;
import sau.com.schoolmanagement1.model.enums.Department;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 32)
    private String firstName;

    @Column(nullable = false, length = 32)
    private String lastName;

    @Column(name = "student_number", nullable = false, unique = true, length = 32)
    private String studentNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Department department;

    @OneToMany(mappedBy = "student",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

}
