package itu.framework.webservice.validations;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import itu.framework.webservice.entity.PassengerType;

public final class AgeRangeValidator {

    private AgeRangeValidator() {}

    public static void validate(List<PassengerType> items) {
        if (items == null || items.isEmpty()) return;

        // Étiquettes lisibles, stables par identité d'objet (même après tri)
        final Map<PassengerType, String> labels = new IdentityHashMap<>();
        for (int i = 0; i < items.size(); i++) {
            labels.put(items.get(i), buildLabel(items.get(i), i));
        }

        // 1) Contrôles de base
        for (PassengerType pt : items) {
            final String label = labels.get(pt);
            final Integer start = pt.getStartAge();
            final Integer end   = pt.getEndAge();

            if (start == null || end == null) {
                throw new IllegalArgumentException("L'âge de début et l'âge de fin doivent être renseignés pour " + label + ".");
            }
            if (start < 0 || end > 150) {
                throw new IllegalArgumentException("Les âges doivent être dans l'intervalle [0..150] pour " + label + ".");
            }
            if (start > end) {
                throw new IllegalArgumentException("L'âge de début doit être ≤ à l'âge de fin pour " + label + ".");
            }
        }

        // 2) Tri pour détecter les chevauchements
        items.sort((a, b) -> {
            int cmp = Integer.compare(val(a.getStartAge()), val(b.getStartAge()));
            if (cmp != 0) return cmp;
            return Integer.compare(val(a.getEndAge()), val(b.getEndAge()));
        });

        // 3) Chevauchements
        PassengerType prev = null;
        for (PassengerType cur : items) {
            if (prev != null) {
                int curStart = val(cur.getStartAge());
                int prevEnd  = val(prev.getEndAge());
                if (curStart <= prevEnd) {
                    throw new IllegalArgumentException(String.format(
                        "Plages d’âges qui se chevauchent entre %s [%d..%d] et %s [%d..%d].",
                        labels.get(prev), val(prev.getStartAge()), prevEnd,
                        labels.get(cur),  curStart,                 val(cur.getEndAge())
                    ));
                }
            }
            prev = cur;
        }
    }

    /** Construit un libellé lisible : typeName sinon 'élément #n' si vide/null. */
    private static String buildLabel(PassengerType pt, int index) {
        String name = (pt != null && pt.getTypeName() != null) ? pt.getTypeName().trim() : "";
        return name.isEmpty() ? ("élément #" + (index + 1)) : name;
    }

    /** Convertit un Integer potentiellement nul en 0 (sécurité). */
    private static int val(Integer i) {
        return (i != null) ? i : 0;
    }
}
