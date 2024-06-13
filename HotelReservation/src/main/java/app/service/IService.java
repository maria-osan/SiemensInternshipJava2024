package app.service;

import app.entity.Hotel;
import app.entity.Room;

import java.time.LocalDate;
import java.util.List;

public interface IService {
    List<Hotel> findHotelsNearby(double latitude, double longitude, double radius);

    List<Room> findRoomsByHotelId(Long hotelId);

    void handleBooking(Long roomNumber, String startDate, String endDate);

    void handleFeedback(String feedback, Long hotelId);
}
