package app.repository;

import app.entity.Booking;
import app.utils.HibernateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookingRepository implements IRepository<Booking> {
    private static final Logger logger = LogManager.getLogger(Booking.class);

    @Override
    public void save(Booking entity) {
        logger.info("Saving booking: {}", entity);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            logger.info("Booking saved successfully: {}", entity);
        } catch (Exception e) {
            logger.error("Error saving booking: {}", e.getMessage(), e);
        }
    }

    public Booking update(Booking entity) {
        logger.info("Updating booking: {}", entity);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
            logger.info("Booking updated successfully: {}", entity);
            return entity;
        } catch (Exception e) {
            logger.error("Error updating booking: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<Booking> findAll() {
        logger.info("Getting the bookings...");
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            List<Booking> bookings = session.createQuery("from Booking ", Booking.class).getResultList();
            logger.info("Retrieved {} bookings", bookings.size());
            return bookings;
        } catch (Exception e) {
            logger.error("Error retrieving bookings: {}", e.getMessage(), e);
            return null;
        }
    }
}
