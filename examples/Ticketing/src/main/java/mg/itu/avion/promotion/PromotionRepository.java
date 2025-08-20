package mg.itu.avion.promotion;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import java.time.LocalDateTime;
import java.util.List;

public class PromotionRepository {
    private final SqlSessionFactory sqlSessionFactory;

    public PromotionRepository(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<Promotion> findPromotionsBefore(LocalDateTime before) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PromotionMapper mapper = session.getMapper(PromotionMapper.class);
            return mapper.findPromotionsBefore(before);
        }
    }

    public int countUnpaidReservations(Integer flightId, Integer classId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PromotionMapper mapper = session.getMapper(PromotionMapper.class);
            return mapper.countUnpaidReservations(flightId, classId);
        }
    }

    public void updatePromotion(Promotion promotion) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PromotionMapper mapper = session.getMapper(PromotionMapper.class);
            mapper.updatePromotion(promotion);
            session.commit();
        }
    }
}