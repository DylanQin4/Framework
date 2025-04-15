package mg.itu.avion.airplane;

import java.util.List;

public class ClassService {
    private ClassRepository repository;

    public ClassService(ClassRepository repository) {
        this.repository = repository;
    }

    public List<Class> getAllClasses() {
        return repository.getAllClasses();
    }

    public Class getClassById(Integer id) {
        return repository.getClassById(id);
    }
}