package mg.itu.avion.config;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class ConfigurationRepository {
    private SqlSessionFactory sqlSessionFactory;
    
    public ConfigurationRepository(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
    
    public Configuration getConfigurationByKey(String configKey) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ConfigurationMapper mapper = session.getMapper(ConfigurationMapper.class);
            return mapper.getConfigurationByKey(configKey);
        }
    }    
}