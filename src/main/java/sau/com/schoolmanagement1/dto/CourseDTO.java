package sau.com.schoolmanagement1.dto;

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

    private String title;

    private String description;

    private Semester semester;

    private String courseCode;
}