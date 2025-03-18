package mg.itu.avion.config;

/**
 * Enumeration des cles de configuration du systeme.
 */
public enum ConfigKey {
    RESERVATION_CUTOFF_HOURS("Délai en heures avant lequel une réservation doit être effectuée"),
    CANCELLATION_CUTOFF_HOURS("Délai en heures avant lequel une annulation peut être effectuée sans frais"),
    PROMOTION_LIMIT("Nombre maximum de promotions actives simultanément"),
    PROMOTION_DISCOUNT("Pourcentage de réduction standard pour les promotions");
    
    private final String description;

    ConfigKey(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getDatabaseKey() {
        return this.name().toLowerCase();
    }

        
    /**
     * Convertit une chaîne de caractères en ConfigKey.
     * 
     * @param value Valeur de la clé en chaîne de caractères
     * @return L'enum ConfigKey correspondant
     */
    public static ConfigKey fromString(String value) {
        if (value == null) {
            return null;
        }
        
        try {
            return ConfigKey.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Valeur de configuration invalide: " + value, e);
        }
    }
}
