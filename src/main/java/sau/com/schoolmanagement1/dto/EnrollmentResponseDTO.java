package sau.com.schoolmanagement1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sau.com.schoolmanagement1.model.enums.Semester;
import sau.com.schoolmanagement1.model.enums.Term;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentResponseDTO {

    private Long id;

    private LocalDate classDate;

    private Integer academicYear;

    private Term term;

    private BigDecimal tuition;

    private Integer attendance;

    private String studentNumber;

    private String studentFullName;

    private String courseTitle;

    private String courseCode;

    private Semester courseSemester;
}