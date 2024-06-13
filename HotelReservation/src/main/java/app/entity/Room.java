package app.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @Column(name = "roomNumber")
    private Long roomNumber;

    @Column(name = "type")
    private RoomType type; // 1: single, 2: double, 3: suite, 4: matrimonial

    @Column(name = "price")
    private double price;

    @JsonProperty("isAvailable")
    @Column(name = "isAvailable")
    private boolean available;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotelId")
    private Hotel hotel;

    public Room() {

    }

    public Room(Long roomNumber, RoomType type, double price, boolean available) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.available = available;
    }

    public Room(Long roomNumber, RoomType type, double price, boolean available, Hotel hotel) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.available = available;
        this.hotel = hotel;
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
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber=" + roomNumber +
                ", type=" + type +
                ", price=" + price +
                ", isAvailable=" + available +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return type == room.type && Double.compare(price, room.price) == 0 && available == room.available && Objects.equals(roomNumber, room.roomNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber, type, price, available);
    }
}
