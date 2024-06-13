package app.entity.dto;

public class FeedbackDTO {
    private String feedback;
    private Long hotelId;

    public FeedbackDTO() {

    }

    public FeedbackDTO(String feedback, Long hotelId) {
        this.feedback = feedback;
        this.hotelId = hotelId;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }
}
