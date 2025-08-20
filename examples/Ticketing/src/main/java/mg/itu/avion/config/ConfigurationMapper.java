package mg.itu.avion.config;

import java.util.List;

import org.apache.ibatis.annotations.*;

public interface ConfigurationMapper {
@Select("SELECT * FROM configurations WHERE config_key = #{configKey}::key_type")
    @Results({
        @Result(property = "configKey",   column = "config_key", typeHandler = ConfigKeyTypeHandler.class),
        @Result(property = "configValue", column = "config_value"),
        @Result(property = "description", column = "description")
    })
    Configuration getConfigurationByKey(String configKey);

    @Select("SELECT config_key, config_value, description FROM configurations ORDER BY config_key")
    @Results({
        @Result(property = "configKey",   column = "config_key", typeHandler = ConfigKeyTypeHandler.class),
        @Result(property = "configValue", column = "config_value"),
        @Result(property = "description", column = "description")
    })
    List<Configuration> getAll();

    @Insert("""
        INSERT INTO configurations (config_key, config_value, description)
        VALUES (
            LOWER(#{config.configKey, jdbcType=VARCHAR})::key_type,
            #{config.configValue},
            COALESCE(#{config.description}, '')
        )
        ON CONFLICT (config_key) DO UPDATE
        SET config_value = EXCLUDED.config_value,
            description  = EXCLUDED.description
    """)
    void upsert(@Param("config") Configuration config);
}
