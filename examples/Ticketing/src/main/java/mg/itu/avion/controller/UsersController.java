package mg.itu.avion.controller;

import com.ETU1792.annotation.*;
import com.ETU1792.utils.ModelView;
import com.ETU1792.utils.MySession;

import mg.itu.avion.user.User;
import mg.itu.avion.user.UserRepository;
import mg.itu.avion.user.UserRequest;

@Controller
public class UsersController {
    @GET("")
    public ModelView getViewLogin(MySession mySession){
        if (mySession.get("auth") != null) {
            ModelView mv = new ModelView("reservations/add");
            mv.setIsRedirect(true);
            return mv;
        }
        ModelView mv = new ModelView("login");
        mv.setIsRedirect(true);
        return mv;
    }

    @GET("login")
    public ModelView login() {
        ModelView mv = new ModelView("/auth/login.jsp");
        return mv;
    }

    @POST("login")
    public ModelView login(@Param(name = "email") String email, @Param(name = "pwd") String password, MySession session) throws Exception {
        System.out.println("Login: " + email + " - " + password);
        UserRepository repository = new UserRepository();
        User user = repository.getUser(email.trim(), password);
        if (user == null) {
            ModelView mv = new ModelView("/auth/login.jsp");
            mv.addObject("errors", "Email ou mot de passe incorrect");
            return mv;
        }

        System.out.println("User: " + user);
        
        session.add("auth", true);
        session.add("userId", user.getId());
        session.add("role", user.getRolesLabel());
        session.add("username", user.getUsername());
        session.add("email", user.getEmail());

        ModelView mv = new ModelView("");
        mv.setIsRedirect(true);
        return mv;
    }

    @GET("registration")
    public ModelView getViewRegister() {
        return new ModelView("/auth/register.jsp");
    }

    @POST("register")
    @FormView("registration")
    public ModelView handleRegister(@ParamObject UserRequest userRequest, MySession session) {
        // delete errors and inputValues in HttpSession
        session.delete("errors");
        session.delete("inputValues");

        UserRepository repository = new UserRepository();
        User existUser = repository.getUserByEmailOrUsername(userRequest.getEmail(), userRequest.getUsername());
        if (existUser != null) {
            ModelView mv = new ModelView("/auth/register.jsp");
            session.add("inputValues", userRequest);
            mv.addObject("error", "Email ou Nom d'utilisateur deja utilise");
            return mv;
        }
        User user = repository.saveUser(userRequest);
        if (user == null) {
            ModelView mv = new ModelView("/auth/register.jsp");
            mv.addObject("error", "Erreur lors de l'enregistrement");
            return mv;
        }
        
        try {
            return login(userRequest.getEmail(), userRequest.getPwd(), session);
        } catch (Exception e) {
            e.printStackTrace();
            ModelView mv = new ModelView("/auth/register.jsp");
            mv.addObject("error", "Erreur lors de l'enregistrement");
            return mv;
        }
    }

    @GET(value = "logout")
    public ModelView logout(MySession session) {
        session.delete("auth");
        session.delete("userId");
        session.delete("role");
        session.delete("username");
        session.delete("email");

        ModelView mv = new ModelView("");
        mv.setIsRedirect(true);
        return mv;
    }
}
