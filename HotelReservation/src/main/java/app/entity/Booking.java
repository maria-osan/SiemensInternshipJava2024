package app.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "roomNumber")
    private Long roomNumber;

    @Column(name = "startDate")
    private LocalDate startDate;

    @Column(name = "endDate")
    private LocalDate endDate;

    @Column(name = "price")
    private double price;

    @Column(name = "status")
    private BookingStatus status;

    public Booking() {

    }

    public Booking(Long roomNumber, LocalDate startDate, LocalDate endDate, double price, BookingStatus status) {
        this.roomNumber = roomNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Long roomNumber) {
        this.roomNumber = roomNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", roomNumber=" + roomNumber +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", price=" + price +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Double.compare(price, booking.price) == 0 && Objects.equals(id, booking.id) && Objects.equals(roomNumber, booking.roomNumber) && Objects.equals(startDate, booking.startDate) && Objects.equals(endDate, booking.endDate) && status == booking.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomNumber, startDate, endDate, price, status);
    }
}
