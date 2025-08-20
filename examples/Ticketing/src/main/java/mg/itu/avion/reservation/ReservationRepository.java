package mg.itu.avion.reservation;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import java.util.List;

public class ReservationRepository {
    private final SqlSessionFactory sqlSessionFactory;

    public ReservationRepository(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public Reservation getReservationById(Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ReservationMapper rm = session.getMapper(ReservationMapper.class);
            ReservationPassengerMapper rpm = session.getMapper(ReservationPassengerMapper.class);
            Reservation r = rm.getReservationById(id);
            if (r != null) {
                r.setPassengers(rpm.findByReservationId(r.getId()));
            }
            return r;
        }
    }

    public List<Reservation> getAllReservations() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ReservationMapper rm = session.getMapper(ReservationMapper.class);
            ReservationPassengerMapper rpm = session.getMapper(ReservationPassengerMapper.class);
            List<Reservation> list = rm.getAllReservations();
            for (Reservation r : list) {
                r.setPassengers(rpm.findByReservationId(r.getId()));
            }
            return list;
        }
    }

    /** Insert entête + lignes (transaction MyBatis au niveau session). */
    public void saveReservationWithPassengers(Reservation reservation, List<ReservationPassenger> passengers) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ReservationMapper rm = session.getMapper(ReservationMapper.class);
            ReservationPassengerMapper rpm = session.getMapper(ReservationPassengerMapper.class);

            rm.insertReservation(reservation);
            for (ReservationPassenger p : passengers) {
                p.setReservationId(reservation.getId());
                rpm.insert(p);
            }
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

    public int countReservedSeatsByFlightAndClass(Integer flightId, Integer classId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ReservationPassengerMapper rpm = session.getMapper(ReservationPassengerMapper.class);
            return rpm.countReservedSeatsByFlightAndClass(flightId, classId);
        }
    }

    public void deletePassengersByReservationId(Integer reservationId) {
        try (var session = sqlSessionFactory.openSession()) {
            var rpm = session.getMapper(ReservationPassengerMapper.class);
            rpm.deleteByReservationId(reservationId);
            session.commit();
        }
    }

    public void savePassengers(Integer reservationId, List<ReservationPassenger> passengers) {
        try (var session = sqlSessionFactory.openSession()) {
            var rpm = session.getMapper(ReservationPassengerMapper.class);
            for (ReservationPassenger p : passengers) {
                p.setReservationId(reservationId);
                rpm.insert(p);
            }
            session.commit();
        }
    }

    public int countPaidSeatsByFlightAndClass(Integer flightId, Integer classId) {
        try (org.apache.ibatis.session.SqlSession session = sqlSessionFactory.openSession()) {
            ReservationPassengerMapper rpm = session.getMapper(ReservationPassengerMapper.class);
            return rpm.countPaidSeatsByFlightAndClass(flightId, classId);
        }
    }
}
