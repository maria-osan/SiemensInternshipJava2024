package app.repository;

import app.entity.Hotel;
import app.utils.HibernateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HotelRepository implements IRepository<Hotel> {
    private static final Logger logger = LogManager.getLogger(HotelRepository.class);

    @Override
    public void save(Hotel entity) {
        logger.info("Saving hotel: {}", entity);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            logger.info("Hotel saved successfully: {}", entity);
        } catch (Exception e) {
            logger.error("Error saving hotel: {}", e.getMessage(), e);
        }
    }

    @Override
    public Hotel update(Hotel entity) {
        return null;
    }

    @Override
    public List<Hotel> findAll() {
        logger.info("Getting the hotels...");
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            List<Hotel> hotels = session.createQuery("from Hotel ", Hotel.class).getResultList();
            logger.info("Retrieved {} hotels", hotels.size());
            return hotels;
        } catch (Exception e) {
            logger.error("Error retrieving hotels: {}", e.getMessage(), e);
            return null;
        }
    }
}
