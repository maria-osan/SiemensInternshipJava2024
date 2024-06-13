package app.controller;

import app.entity.dto.BookingDTO;
import app.entity.dto.FeedbackDTO;
import app.entity.dto.HotelDTO;
import app.entity.dto.RoomDTO;
import app.service.Service;
import app.utils.DTOUtils;
import app.utils.Exceptions.BookingConflictException;
import app.utils.Exceptions.RoomNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AppController {
    private final Service service;

    @Autowired
    public AppController(Service service) {
        this.service = service;
    }

    @GetMapping("/hotels")
    public ResponseEntity<List<HotelDTO>> getHotelsNearby(@RequestParam double latitude, @RequestParam double longitude, @RequestParam double radius) {
        List<HotelDTO> hotelDTOs = service.findHotelsNearby(latitude, longitude, radius).stream().map(DTOUtils::convertHotelToDTO).toList();
        return new ResponseEntity<>(hotelDTOs, HttpStatus.OK);
    }

    @GetMapping("/hotels/{id}/rooms")
    public ResponseEntity<List<RoomDTO>> getRoomsByHotelId(@PathVariable Long id) {
        List<RoomDTO> roomDTOs= service.findRoomsByHotelId(id).stream().map(DTOUtils::convertRoomToDTO).toList();
        return new ResponseEntity<>(roomDTOs, HttpStatus.OK);
    }

    @PostMapping("/bookings")
    public ResponseEntity<Void> handleBooking(@RequestBody BookingDTO bookingDTO) {
        try{
            service.handleBooking(bookingDTO.getRoomNumber(), bookingDTO.getStartDate(), bookingDTO.getEndDate());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RoomNotAvailableException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (BookingConflictException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/feedbacks")
    public ResponseEntity<Void> handleFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        try {
            service.handleFeedback(feedbackDTO.getFeedback(), feedbackDTO.getHotelId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
