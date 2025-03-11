package mg.itu.avion.user;

import mg.itu.avion.utils.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class UserRepository {
    private SqlSessionFactory sqlSessionFactory;

    public UserRepository() {
        sqlSessionFactory = MyBatisUtil.getSqlSessionFactory();
    }

    public User getUserById(int id) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            UserMapper mapper = session.getMapper(UserMapper.class);
            RoleMapper roleMapper = session.getMapper(RoleMapper.class);
            User user = mapper.getUserById(id);
            if (user != null) {
                user.setRoles(roleMapper.getRolesByUserId(user.getId()));
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    public User getUser(String email, String password) throws Exception {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            UserMapper mapper = session.getMapper(UserMapper.class);
            RoleMapper roleMapper = session.getMapper(RoleMapper.class);
            User user = mapper.findByEmailAndPassword(email, password);
            if (user != null) {
                user.setRoles(roleMapper.getRolesByUserId(user.getId()));
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error while getting user");
        } finally {
            session.close();
        }
    }


}
