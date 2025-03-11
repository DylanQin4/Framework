package mg.itu.avion;

import mg.itu.avion.user.User;
import mg.itu.avion.user.UserRepository;

public class AvionApplication {

    public static void main(String[] args) {
        UserRepository repo = new UserRepository();
        User user = repo.getUserById(1);
        if (user != null) {
            System.out.println("ID: " + user.getId() + ", Username: " + user.getUsername());
        } else {
            System.out.println("Utilisateur non trouvé.");
        }
    }
}
