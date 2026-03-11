package sau.com.schoolmanagement1.exception;

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

}