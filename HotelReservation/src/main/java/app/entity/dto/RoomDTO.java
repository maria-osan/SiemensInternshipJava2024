package app.entity.dto;

import app.entity.RoomType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomDTO {
    private Long roomNumber;
    private RoomType type;
    private double price;
    @JsonProperty("isAvailable")
    private boolean isAvailable;

    public RoomDTO(Long roomNumber, RoomType type, double price, boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    public Long getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Long roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
