package it.example.WebAppLibri.Controller;

import it.example.WebAppLibri.Dao.LibroDao;
import it.example.WebAppLibri.Model.Libro;
import it.example.WebAppLibri.Model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ControllerLibreria {
    @Autowired
    private LibroDao libroRepository;
    @GetMapping("/libriUtente")
    public List<Libro> getLibriByid(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        List<Libro> libri = libroRepository.libriUtenti(user.getId());
        return libri;
    }

    @GetMapping("libriUtente/{idLibro}")
    public Libro getLibro(@PathVariable("idLibro") long id) {
        Libro libro = libroRepository.findById(id);
        return libro;
    }

}
