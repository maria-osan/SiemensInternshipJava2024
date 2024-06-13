package app.repository;

import app.entity.Room;
import app.utils.HibernateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class RoomRepository implements IRepository<Room> {
    private static final Logger logger = LogManager.getLogger(RoomRepository.class);

    @Override
    public void save(Room entity) {
        logger.info("Saving room: {}", entity);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            logger.info("Room saved successfully: {}", entity);
        } catch (Exception e) {
            logger.error("Error saving room: {}", e.getMessage(), e);
        }
    }

    @Override
    public Room update(Room entity) {
        logger.info("Updating room: {}", entity);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
            logger.info("Room updated successfully: {}", entity);
            return entity;
        } catch (Exception e) {
            logger.error("Error updating room: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<Room> findAll() {
        logger.info("Getting the rooms...");
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            List<Room> rooms = session.createQuery("from Room ", Room.class).getResultList();
            logger.info("Retrieved {} rooms", rooms.size());
            return rooms;
        } catch (Exception e) {
            logger.error("Error retrieving rooms: {}", e.getMessage(), e);
            return null;
        }
    }

    public Room findOne(Long roomNumber) {
        logger.info("Finding room with number: {}", roomNumber);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Room room = session.get(Room.class, roomNumber);
            if (room != null) {
                logger.info("Room found: {}", room);
            } else {
                logger.info("Room with number {} not found", roomNumber);
            }
            return room;
        } catch (Exception e) {
            logger.error("Error finding room with number {}: {}", roomNumber, e.getMessage(), e);
            return null;
        }
    }

    public List<Room> findByHotelId(Long hotelId) {
        logger.info("Getting rooms for hotel with ID: {}", hotelId);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String sql = "FROM Room R WHERE R.hotel.id=:hotelId";
            Query query = session.createQuery(sql, Room.class);
            query.setParameter("hotelId", hotelId);
            List<Room> rooms = query.getResultList();
            logger.info("Retrieved {} rooms for hotel with ID: {}", rooms.size(), hotelId);
            return rooms;
        } catch (Exception e) {
            logger.error("Error retrieving rooms for hotel with ID {}: {}", hotelId, e.getMessage(), e);
            return null;
        }
    }
}
