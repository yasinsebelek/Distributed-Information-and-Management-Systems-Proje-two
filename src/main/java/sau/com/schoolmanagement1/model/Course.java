package sau.com.schoolmanagement1.model;

import jakarta.persistence.*;
import lombok.*;
import sau.com.schoolmanagement1.model.enums.Semester;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_code", nullable = false, unique = true, length = 32)
    private String courseCode;

    @Column(nullable = false, length = 128)
    private String title;

    @Column(nullable = false, length = 256)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Semester semester;


    @OneToMany(mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();


    
}
