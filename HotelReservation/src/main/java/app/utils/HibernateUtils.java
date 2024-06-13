package app.utils;

import app.entity.Booking;
import app.entity.Feedback;
import app.entity.Hotel;
import app.entity.Room;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(){
        if ((sessionFactory == null) || (sessionFactory.isClosed()))
            sessionFactory = createNewSessionFactory();
        return sessionFactory;
    }

    private static  SessionFactory createNewSessionFactory(){
        sessionFactory = new Configuration()
                .addAnnotatedClass(Hotel.class).addAnnotatedClass(Room.class).addAnnotatedClass(Booking.class)
                .addAnnotatedClass(Feedback.class)
                .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                .setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/hotel_reservation")
                .setProperty("hibernate.connection.username", "postgres")
                .setProperty("hibernate.connection.password", "postgres")
                .buildSessionFactory();
        return sessionFactory;
    }

    public static  void closeSessionFactory(){
        if (sessionFactory != null)
            sessionFactory.close();
    }
}
