package mg.itu.avion.config;

import java.util.List;

public class ConfigurationService {
    private ConfigurationRepository configurationRepository;

    public ConfigurationService(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    public Configuration getConfigurationByKey(String configKey) {
        return configurationRepository.getConfigurationByKey(configKey);
    }

    public List<Configuration> getAll() {
        return configurationRepository.getAll();
    }

    public void upsert(Configuration cfg) {
        configurationRepository.upsert(cfg);
    }
}
