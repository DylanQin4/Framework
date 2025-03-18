package mg.itu.avion.utils;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

public class MyBatisUtil {
    @Getter
    private static final SqlSessionFactory sqlSessionFactory;

    static {
         HikariDataSource dataSource = new HikariDataSource();
         dataSource.setDriverClassName("org.postgresql.Driver");
         dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/avion");
         dataSource.setUsername("postgres");
         dataSource.setPassword("postgres");

        JdbcTransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);

        configuration.addMapper(mg.itu.avion.user.UserMapper.class);
        configuration.addMapper(mg.itu.avion.user.RoleMapper.class);
        configuration.addMapper(mg.itu.avion.flight.FlightMapper.class);
        configuration.addMapper(mg.itu.avion.airplane.AirplaneMapper.class);
        configuration.addMapper(mg.itu.avion.city.CityMapper.class);
        configuration.addMapper(mg.itu.avion.city.CountryMapper.class);
        configuration.addMapper(mg.itu.avion.config.ConfigurationMapper.class);

        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

}
