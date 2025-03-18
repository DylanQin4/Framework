package mg.itu.avion.config;

public class ConfigurationService {
    private ConfigurationRepository configurationRepository;

    public ConfigurationService(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    public Configuration getConfigurationByKey(String configKey) {
        return configurationRepository.getConfigurationByKey(configKey);
    }
}
