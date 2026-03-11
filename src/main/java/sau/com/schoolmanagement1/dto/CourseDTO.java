package sau.com.schoolmanagement1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sau.com.schoolmanagement1.model.enums.Semester;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {

    private Long id;

    @NotBlank
    @Size(max = 32)
    private String courseCode;

    @NotBlank
    @Size(max = 128)
    private String title;

    @NotBlank
    @Size(max = 256)
    private String description;

    @NotNull
    private Semester semester;

}