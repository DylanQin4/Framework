package mg.itu.avion.promotion;

import java.time.LocalDateTime;
import java.util.List;

public class PromotionService {
    private final PromotionRepository repository;

    public PromotionService(PromotionRepository repository) {
        this.repository = repository;
    }

    public List<Promotion> makePromotionReport(LocalDateTime before) {
        List<Promotion> promos = repository.findPromotionsBefore(before);

        for (int i = 0; i < promos.size() - 1; i++) {
            Promotion current = promos.get(i);
            Promotion next = promos.get(i + 1);

            int nonPayes = repository.countUnpaidReservations(
                current.getFlightId(), current.getClassId()
            );

            next.setPromotionLimit(next.getPromotionLimit() + nonPayes);

            repository.updatePromotion(next);
        }

        return promos;
    }
}
