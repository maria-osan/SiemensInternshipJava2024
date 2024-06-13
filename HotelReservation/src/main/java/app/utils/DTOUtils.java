package app.utils;

import app.entity.Hotel;
import app.entity.Room;
import app.entity.RoomType;
import app.entity.dto.HotelDTO;
import app.entity.dto.RoomDTO;

public class DTOUtils {
    public static HotelDTO convertHotelToDTO(Hotel hotel) {
        Long id = hotel.getId();
        String name = hotel.getName();
        Double latitude = hotel.getLatitude();
        Double longitude = hotel.getLongitude();
        return new HotelDTO(id, name, latitude, longitude);
    }

    public static RoomDTO convertRoomToDTO(Room room) {
        Long roomNumber = room.getRoomNumber();
        RoomType type = room.getType();
        double price = room.getPrice();
        boolean isAvailable = room.isAvailable();
        return new RoomDTO(roomNumber, type, price, isAvailable);
    }
}
