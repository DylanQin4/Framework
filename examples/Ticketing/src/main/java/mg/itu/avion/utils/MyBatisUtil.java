package mg.itu.avion.utils;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
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

        // Enregistrer tes mappers annotés ici (par exemple, UserMapper)
        configuration.addMapper(mg.itu.avion.mapper.UserMapper.class);

        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

}
