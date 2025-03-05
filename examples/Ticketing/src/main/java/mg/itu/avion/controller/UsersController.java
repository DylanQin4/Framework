package mg.itu.avion.controller;

import com.ETU1792.annotation.*;
import com.ETU1792.utils.ModelView;

@Controller
public class UsersController {
    @GET("login")
    public ModelView getViewLogin(){
        return new ModelView("/login.jsp");
    }
}
