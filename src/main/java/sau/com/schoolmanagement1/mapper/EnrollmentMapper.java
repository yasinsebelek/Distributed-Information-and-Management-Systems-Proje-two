package sau.com.schoolmanagement1.mapper;

import sau.com.schoolmanagement1.dto.EnrollmentResponseDTO;
import sau.com.schoolmanagement1.model.Enrollment;

import java.util.List;
import java.util.stream.Collectors;

public class EnrollmentMapper {

    public static EnrollmentResponseDTO toDTO(Enrollment enrollment) {
        if (enrollment == null) {
            return null;
        }

        return new EnrollmentResponseDTO(
                enrollment.getId(),
                enrollment.getClassDate(),
                enrollment.getAcademicYear(),
                enrollment.getTerm(),
                enrollment.getTuition(),
                enrollment.getAttendance(),
                enrollment.getStudent().getStudentNumber(),
                enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName(),
                enrollment.getCourse().getTitle(),
                enrollment.getCourse().getCourseCode(),
                enrollment.getCourse().getSemester()
        );
    }

    public static List<EnrollmentResponseDTO> toDTOList(List<Enrollment> enrollments) {
        if (enrollments == null) {
            return null;
        }

        return enrollments.stream()
                .map(EnrollmentMapper::toDTO)
                .collect(Collectors.toList());
    }
}