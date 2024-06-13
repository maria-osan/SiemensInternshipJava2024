package app.utils;

import app.entity.Feedback;
import app.entity.Hotel;

import app.entity.Room;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataLoader {
    private static final Logger logger = LogManager.getLogger(DataLoader.class);

    public void loadData() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Hotel> hotels = objectMapper.readValue(new File("data/hotels.json"), new TypeReference<List<Hotel>>(){});

            SessionFactory sessionFactory = new Configuration()
                    .addAnnotatedClass(Hotel.class).addAnnotatedClass(Room.class).addAnnotatedClass(Boolean.class)
                    .addAnnotatedClass(Feedback.class)
                    .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                    .setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/hotel_reservation")
                    .setProperty("hibernate.connection.username", "postgres")
                    .setProperty("hibernate.connection.password", "postgres")
                    .buildSessionFactory();

            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            // Delete existing data from Hotel and Room tables
            //session.createQuery("DELETE FROM Room").executeUpdate();
            //session.createQuery("DELETE FROM Hotel").executeUpdate();

            for (Hotel hotel : hotels) {
                session.save(hotel); // save hotel
                for (Room room : hotel.getRooms()) {
                    room.setHotel(hotel); // set hotel reference for each room
                    session.save(room); // save room
                }
            }

            transaction.commit();
            session.close();
            sessionFactory.close();
        } catch (IOException e) {
            logger.error("An error occurred while loading data from JSON file", e);
        } catch (HibernateException e) {
            logger.error("An error occurred during Hibernate operation", e);
        }
    }
}
