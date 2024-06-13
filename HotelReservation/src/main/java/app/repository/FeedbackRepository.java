package app.repository;

import app.entity.Feedback;
import app.utils.HibernateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FeedbackRepository implements IRepository<Feedback> {
    private static final Logger logger = LogManager.getLogger(Feedback.class);

    @Override
    public void save(Feedback entity) {
        logger.info("Saving feedback: {}", entity);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            logger.info("Feedback saved successfully: {}", entity);
        } catch (Exception e) {
            logger.error("Error saving feedback: {}", e.getMessage(), e);
        }
    }

    @Override
    public Feedback update(Feedback entity) {
        return null;
    }

    @Override
    public List<Feedback> findAll() {
        return null;
    }
}
