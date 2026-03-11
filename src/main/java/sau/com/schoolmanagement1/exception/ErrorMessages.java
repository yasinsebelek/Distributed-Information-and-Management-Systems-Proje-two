package sau.com.schoolmanagement1.exception;

import sau.com.schoolmanagement1.model.enums.Term;

public final class ErrorMessages {

    private ErrorMessages() {}

    public static String studentNotFound(Long id) {
        return "Student not found with id: " + id;
    }

    public static String studentAlreadyExists(String studentNumber) {
        return "Student already exists with student number: " + studentNumber;
    }

    public static String courseNotFound(Long id) {
        return "Course not found with id: " + id;
    }

    public static String courseAlreadyExists(String courseCode) {
        return "Student already exists with student number: " + courseCode;
    }

    public static String enrollmentAlreadyExists(Long studentId, Long courseId, Integer year, Term term) {
        return "Student " + studentId + " is already enrolled in course " + courseId +
                " for " + year + " " + term;
    }

    public static String enrollmentNotFound(Long id) {
        return "Enrollment not found with id: " + id;
    }
}