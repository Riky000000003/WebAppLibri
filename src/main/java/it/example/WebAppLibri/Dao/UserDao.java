package it.example.WebAppLibri.Dao;

import it.example.WebAppLibri.Model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Long> {
    User findById(long id);
    @Query("select u from User u where username= :username")
    public User registrazione(String username);
    @Query("select s from User s where username= :username and password = :password")
    public User login(String username, String password);
}
