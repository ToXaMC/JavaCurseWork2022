package ru.mirea.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mirea.Controllers.ModelController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsersImpl {
    private final UserRep userRep;
    public static final Map<String, User> map = new HashMap<>();

    @Autowired
    public UsersImpl(UserRep userRep) {
        this.userRep = userRep;
    }

    public void addorsave(User user) {
        if(ModelController.inet) {
            userRep.save(user);
        }
        map.put(user.getUsername(), user);
    }

    public List<User> getAll() {
        return ModelController.inet ? userRep.findAll() : map.values().stream().toList();
    }

    public void delete(int id) {
        if(ModelController.inet)
            userRep.delete(userRep.getById(id));
    }

    public void delete(String log) {
        if(ModelController.inet)
            userRep.delete(getuser(log));
    }

    public User getuser(String log)
    {
        User user = ModelController.inet ? userRep.findByUsername(log) : map.get(log);
        if(user != null) map.put(user.getUsername(), user);
        return user;
    }

    public boolean checkpar(String log, String par)
    {
        User user = getuser(log);
        if (user == null) return false;
        return user.conf_auth(par);
    }
}
