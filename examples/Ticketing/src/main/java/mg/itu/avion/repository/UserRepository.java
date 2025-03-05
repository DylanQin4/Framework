package mg.itu.avion.repository;

import mg.itu.avion.entity.User;
import mg.itu.avion.mapper.UserMapper;
import mg.itu.avion.utils.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

public class UserRepository {
    public User getUserById(int id) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            return mapper.getUserById(id);
        }
    }
}
