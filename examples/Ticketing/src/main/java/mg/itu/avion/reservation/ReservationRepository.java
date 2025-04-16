package mg.itu.avion.reservation;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import java.util.List;

public class ReservationRepository {
    private SqlSessionFactory sqlSessionFactory;

    public ReservationRepository(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public Reservation getReservationById(Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ReservationMapper mapper = session.getMapper(ReservationMapper.class);
            return mapper.getReservationById(id);
        }
    }

    public List<Reservation> getAllReservations() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ReservationMapper mapper = session.getMapper(ReservationMapper.class);
            return mapper.getAllReservations();
        }
    }

    public void saveReservation(Reservation reservation) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ReservationMapper mapper = session.getMapper(ReservationMapper.class);
            mapper.insertReservation(reservation);
            session.commit();
        }
    }

    public void updateReservation(Reservation reservation) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ReservationMapper mapper = session.getMapper(ReservationMapper.class);
            mapper.updateReservation(reservation);
            session.commit();
        }
    }
}