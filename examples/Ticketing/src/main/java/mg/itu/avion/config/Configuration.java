package mg.itu.avion.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {
    private ConfigKey configKey;
    private String configValue;
    private String description;
}
