package app.entity.dto;

import app.entity.BookingStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class BookingDTO {
    private Long roomNumber;
    private String startDate;
    private String endDate;

    public BookingDTO() {

    }

    public BookingDTO(Long roomNumber, String startDate, String endDate) {
        this.roomNumber = roomNumber;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Long roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
