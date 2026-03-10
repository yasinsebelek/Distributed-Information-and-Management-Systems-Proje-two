package sau.com.schoolmanagement1.exception;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {

    private LocalDateTime timestamp;

    private int status;

    private String error;

    private String message;

    private String path;

    private String requestId;

    private Map<String, String> validationErrors;

}