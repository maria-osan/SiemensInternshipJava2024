package app.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "feedbacks")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "hotelID")
    private Long hotelId;

    public Feedback() {

    }

    public Feedback(String feedback, LocalDate date, Long hotelId) {
        this.feedback = feedback;
        this.date = date;
        this.hotelId = hotelId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", feedback='" + feedback + '\'' +
                ", date=" + date +
                ", hotelId=" + hotelId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback1 = (Feedback) o;
        return Objects.equals(id, feedback1.id) && Objects.equals(feedback, feedback1.feedback) && Objects.equals(date, feedback1.date) && Objects.equals(hotelId, feedback1.hotelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, feedback, date, hotelId);
    }
}
