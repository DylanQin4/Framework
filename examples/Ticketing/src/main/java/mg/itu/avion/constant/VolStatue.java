package mg.itu.avion.constant;

public enum VolStatue {
    ACTIF("ACTIF"),
    ANNULE("ANNULE"),
    RETARDE("RETARDE"),
    TERMINE("TERMINE");

    private final String value;

    VolStatue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static VolStatue fromValue(String value) {
        for (VolStatue statut : values()) {
            if (statut.value.equalsIgnoreCase(value)) {
                return statut;
            }
        }
        throw new IllegalArgumentException("Valeur inconnue : " + value);
    }
}
