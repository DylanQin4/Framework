package mg.itu.avion.controller;

import com.ETU1792.annotation.Controller;
import com.ETU1792.annotation.GET;
import com.ETU1792.utils.ModelView;

import mg.itu.avion.user.User;
import mg.itu.avion.user.UserRepository;

@Controller
public class TestController {
    @GET(value = "test")
    public ModelView test() throws Exception {
        ModelView mv = new ModelView("/test.jsp");
        UserRepository repo = new UserRepository();
        User user = repo.getUser("admin@example.com", "admin123");
        if (user != null) {
            System.out.println("ID: " + user.getId() + ", Username: " + user.getUsername());
        } else {
            System.out.println("Utilisateur non trouvé.");
        }
        return mv;
    }
}
