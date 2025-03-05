package mg.itu.avion;

import mg.itu.avion.service.VolDAO;

public class AvionApplication {

    public static void main(String[] args) {
        VolDAO volDAO = new VolDAO();

        volDAO.findAll();
    }
}
