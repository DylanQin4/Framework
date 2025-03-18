package mg.itu.avion.config;

import org.apache.ibatis.annotations.*;

public interface ConfigurationMapper {
    @Select("SELECT * FROM configurations WHERE config_key = #{configKey}::key_type")
    @Results({
        @Result(property = "configKey", column = "config_key", typeHandler = ConfigKeyTypeHandler.class),
        @Result(property = "configValue", column = "config_value"),
        @Result(property = "description", column = "description")
    })
    Configuration getConfigurationByKey(String configKey);
}
