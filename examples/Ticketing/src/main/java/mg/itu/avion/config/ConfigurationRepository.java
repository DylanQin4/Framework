package mg.itu.avion.config;

import java.util.List;

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
    
    public List<Configuration> getAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(ConfigurationMapper.class).getAll();
        }
    }

    public void upsert(Configuration cfg) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.getMapper(ConfigurationMapper.class).upsert(cfg);
            session.commit();
        }
    }
}