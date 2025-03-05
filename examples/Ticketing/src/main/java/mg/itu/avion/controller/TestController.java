package mg.itu.avion.controller;

import com.ETU1792.annotation.Controller;
import com.ETU1792.annotation.GET;
import com.ETU1792.utils.ModelView;
import mg.itu.avion.entity.User;
import mg.itu.avion.repository.UserRepository;

@Controller
public class TestController {
    @GET(value = "test")
    public ModelView test() {
        ModelView mv = new ModelView("/test.jsp");
        UserRepository repo = new UserRepository();
        User user = repo.getUserById(1);
        if (user != null) {
            System.out.println("ID: " + user.getId() + ", Username: " + user.getUsername());
        } else {
            System.out.println("Utilisateur non trouvé.");
        }
        return mv;
    }
}
