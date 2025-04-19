package mg.itu.avion.flight.search;

import java.util.Map;

public class FlightSqlBuilder {

    public String buildSearchFlights(Map<String, Object> params) {
        FlightSearchCriteria c = (FlightSearchCriteria) params.get("criteria");

        StringBuilder sql = new StringBuilder();
        sql.append("""
            SELECT
            f.id, f.flight_number, f.departure_time, f.arrival_time,
            f.reservation_cutoff_hours, f.cancellation_cutoff_hours,
            f.airplane_id, f.departure_city_id, f.arrival_city_id, f.created_at,

            -- départ
            dc.id   AS dep_id,
            dc.name AS dep_name,
            dco.name AS dep_country_name,

            -- arrivée
            ac.id   AS arr_id,
            ac.name AS arr_name,
            aco.name AS arr_country_name,

            -- avion
            a.id AS ap_id,
            a.model AS ap_model,
            a.total_seats AS ap_total_seats
            FROM flights f
            JOIN cities dc ON dc.id = f.departure_city_id
            LEFT JOIN country dco ON dco.id = dc.country_id
            JOIN cities ac ON ac.id = f.arrival_city_id
            LEFT JOIN country aco ON aco.id = ac.country_id
            JOIN airplanes a ON a.id = f.airplane_id
            WHERE 1=1
        """);

        // Filtres simples sur flights
        if (c.getDepartureCityId() != null)   sql.append(" AND f.departure_city_id = #{criteria.departureCityId} ");
        if (c.getArrivalCityId() != null)     sql.append(" AND f.arrival_city_id = #{criteria.arrivalCityId} ");
        if (c.getDepartureFromDateTime() != null) sql.append(" AND f.departure_time >= #{criteria.departureFromDateTime} ");
        if (c.getDepartureToDateTime() != null)   sql.append(" AND f.departure_time <  #{criteria.departureToDateTime} ");

        boolean needsFcpExists =
                c.getClassId() != null ||
                c.getPassengerTypeId() != null ||
                c.getMinPrice() != null ||
                c.getMaxPrice() != null ||
                Boolean.TRUE.equals(c.getPromoOnly());

        // IMPORTANT : filtre avancé via EXISTS (évite tous les doublons)
        if (needsFcpExists) {
            sql.append(" AND EXISTS ( ");
            sql.append("""
            SELECT 1
            FROM flight_class_passenger fcp
            WHERE fcp.flight_id = f.id
            """);

            // Contraintes sur fcp
            if (c.getClassId() != null)         sql.append(" AND fcp.class_id = #{criteria.classId} ");
            if (c.getPassengerTypeId() != null) sql.append(" AND fcp.passenger_type_id = #{criteria.passengerTypeId} ");
            if (c.getMinPrice() != null)        sql.append(" AND fcp.base_price >= #{criteria.minPrice} ");
            if (c.getMaxPrice() != null)        sql.append(" AND fcp.base_price <= #{criteria.maxPrice} ");

            // Promo : places promo restantes dans la classe correspondante
            if (Boolean.TRUE.equals(c.getPromoOnly())) {
                sql.append("""
                    AND COALESCE((
                    SELECT COUNT(*)
                    FROM reservation_passengers rp
                    JOIN reservations r2 ON r2.id = rp.reservation_id
                    WHERE r2.status IN ('RESERVED','PAID')
                        AND r2.flight_id = f.id
                        AND rp.class_id = fcp.class_id
                    ), 0) < COALESCE(fcp.promotion_limit, 0)
                """);
            }

            sql.append(" ) "); // fin EXISTS
        }

        sql.append(" ORDER BY f.departure_time ");
        return sql.toString();
    }

}

