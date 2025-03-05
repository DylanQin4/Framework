package mg.itu.avion.controller;

import com.ETU1792.annotation.*;
import com.ETU1792.utils.ModelView;
import com.ETU1792.utils.MySession;
import mg.itu.avion.dto.login.Login;
import mg.itu.avion.entity.users.Users;
import mg.itu.avion.service.UserDAO;

@Controller
public class UsersController {

    public static final UserDAO userDAO = new UserDAO();

    @GET("login")
    public ModelView getViewLogin(){
        return new ModelView("login.jsp");
    }

    @POST(value = "login")
    public ModelView login(@ParamObject Login login, MySession session){
        Users users = UserDAO.getUsersByLogin(login);
        if(users == null){
            ModelView modelViewY = new ModelView("index.jsp");
            modelViewY.addObject("message","Tsy nahitana");
            return  modelViewY;
        }
        ModelView modelViewY = new ModelView("template.jsp");
        modelViewY.addObject("page","landing.jsp");
        session.add("id",users.getId());
        session.add("role",users.getRole().getLabel());
        return modelViewY;
    }
}
