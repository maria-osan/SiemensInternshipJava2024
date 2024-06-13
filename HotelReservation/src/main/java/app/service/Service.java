package app.service;

import app.entity.*;
import app.repository.BookingRepository;
import app.repository.FeedbackRepository;
import app.repository.HotelRepository;
import app.repository.RoomRepository;
import app.utils.Exceptions.BookingConflictException;
import app.utils.Exceptions.RoomNotAvailableException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@org.springframework.stereotype.Service
public class Service implements IService {
    private static final Logger logger = LogManager.getLogger(Service.class);

    @Autowired
    private HotelRepository hotelRepo;

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private FeedbackRepository feedbackRepo;

    @Override
    public List<Hotel> findHotelsNearby(double userLatitude, double userLongitude, double radius) {
        logger.info("Finding hotels nearby for coordinates: {}, {}", userLatitude, userLongitude);

        List<Hotel> allHotels = hotelRepo.findAll();
        List<Hotel> nearbyHotels = new ArrayList<>();

        for (Hotel hotel : allHotels) {
            double hotelLatitude = hotel.getLatitude();
            double hotelLongitude = hotel.getLongitude();

            double distance = calculateDistance(userLatitude, userLongitude, hotelLatitude, hotelLongitude);

            // Check if the distance is within the specified radius
            if (distance <= radius) {
                nearbyHotels.add(hotel);
            }
        }

        logger.info("Found {} hotels nearby.", nearbyHotels.size());
        return nearbyHotels;
    }

    private double calculateDistance(double userLatitude, double userLongitude, double hotelLatitude, double hotelLongitude) {
        // calculate the mid latitude for a slightly more accurate estimate
        double latMid = (userLatitude + hotelLatitude) / 2.0;

        double m_per_deg_lat = 111132.954 - 559.822 * Math.cos( 2.0 * latMid ) + 1.175 * Math.cos( 4.0 * latMid);
        double m_per_deg_lon = (3.14159265359/180 ) * 6367449 * Math.cos ( latMid );

        double deltaLat = Math.abs(userLatitude - hotelLatitude);
        double deltaLon = Math.abs(userLongitude - hotelLongitude);

        // calculate the distance using the Haversine formula
        double distance = Math.sqrt(Math.pow(deltaLat * m_per_deg_lat,2) + Math.pow(deltaLon * m_per_deg_lon , 2));
        return distance/1000; // convert distance to kilometers
    }

    @Override
    public List<Room> findRoomsByHotelId(Long hotelId) {
        return roomRepo.findByHotelId(hotelId);
    }

    @Override
    public void handleBooking(Long roomNumber, String start, String end) {
        logger.info("Handling booking for room: {}", roomNumber);

        Room room = roomRepo.findOne(roomNumber);

        LocalDate startDate = parseStringToLocalDate(start);
        LocalDate endDate = parseStringToLocalDate(end);

        List<Booking> bookings = bookingRepo.findAll();
        if (bookings != null) {
            // Check if there are any existing bookings that overlap with the specified dates
            for (Booking existingBooking : bookings) {
                LocalDate existingStartDate = existingBooking.getStartDate();
                LocalDate existingEndDate = existingBooking.getEndDate();

                if (startDate.isBefore(existingEndDate) && endDate.isAfter(existingStartDate) && Objects.equals(existingBooking.getRoomNumber(), room.getRoomNumber()) && !room.isAvailable()) {
                    logger.error("Booking dates overlap with an existing booking: {}", existingBooking);
                    throw new BookingConflictException("Booking dates overlap with an existing booking");
                }
            }
        }

        if(!room.isAvailable()) {
            logger.error("The room {} in not available", room.getRoomNumber());
            throw new RoomNotAvailableException("The room is not available");
        }

        // If no overlap found, save the new booking
        Booking newBooking = new Booking(room.getRoomNumber(), startDate, endDate, calculatePrice(room.getPrice(), startDate, endDate), BookingStatus.CONFIRMED);
        bookingRepo.save(newBooking);
        room.setAvailable(false);
        roomRepo.update(room);
        logger.info("New booking saved successfully: {}", newBooking);
    }

    public LocalDate parseStringToLocalDate(String dateString) {
        LocalDateTime dateTime = LocalDateTime.parse(dateString);
        return dateTime.toLocalDate();
    }

    private double calculatePrice(double price, LocalDate startDate, LocalDate endDate) {
        long nrDays = ChronoUnit.DAYS.between(startDate, endDate);
        return price * nrDays;
    }

    @Override
    public void handleFeedback(String feedback, Long hotelId) {
        logger.info("Handling feedback: {}", feedback);
        Feedback newFeedback = new Feedback(feedback, LocalDate.now(), hotelId);
        feedbackRepo.save(newFeedback);
        logger.info("New feedback saved successfully: {}", newFeedback);
    }
}
