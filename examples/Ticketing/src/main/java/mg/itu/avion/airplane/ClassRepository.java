package mg.itu.avion.airplane;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import java.util.List;

public class ClassRepository {
    private SqlSessionFactory sqlSessionFactory;

    public ClassRepository(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<Class> getAllClasses() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ClassMapper mapper = session.getMapper(ClassMapper.class);
            return mapper.getAllClasses();
        }
    }

    public Class getClassById(Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ClassMapper mapper = session.getMapper(ClassMapper.class);
            return mapper.getClassById(id);
        }
    }
}