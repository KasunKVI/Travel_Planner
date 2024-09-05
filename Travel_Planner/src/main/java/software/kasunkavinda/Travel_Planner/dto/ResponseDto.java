package software.kasunkavinda.Travel_Planner.dto;

public class ResponseDto<T> {
    private T data;
    private String status;
    private String message;

    // Constructors
    public ResponseDto() {
    }
    
    public ResponseDto(T data, String status, String message) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public ResponseDto(String accessToken) {
    }

    // Getters and setters
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
