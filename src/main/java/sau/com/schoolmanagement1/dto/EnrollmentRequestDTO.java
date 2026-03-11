package sau.com.schoolmanagement1.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sau.com.schoolmanagement1.model.enums.Term;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentRequestDTO {
    private Long id;

    @NotNull
    private LocalDate classDate;

    @NotNull
    @Positive
    @Min(2025)
    private Integer academicYear;

    @NotNull
    private Term term;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false )
    private BigDecimal tuition;

    @NotNull
    @PositiveOrZero
    @Max(14)
    private Integer attendance;

    @NotNull
    private Long studentId;

    @NotNull
    private Long courseId;

}
