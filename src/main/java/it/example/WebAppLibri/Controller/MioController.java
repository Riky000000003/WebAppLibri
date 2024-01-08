package it.example.WebAppLibri.Controller;

import it.example.WebAppLibri.Model.Libro;
import it.example.WebAppLibri.Model.User;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MioController {

    private List<User> utenti = new ArrayList<>();
    public  List<Libro> libri = new ArrayList<>();

    @GetMapping(value = "/")
    public String registraForm(User user) {
        return "formUtente";
    }

    @PostMapping(value = "/")
    public String postRegistraForm(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "formUtente";
        }
        if (controllaUsername(user.getUsername())) {
            return "formUtente";
        }
        utenti.add(user);
        return "redirect:/login";
    }

    @GetMapping(value = "/login")
    public String loginForm() {
        return "loginForm";
    }

    @PostMapping(value = "/login")
    public String postLoginForm(@RequestParam("username") String username,
                                @RequestParam("password") String password) {

        if (!login(username,password)) {
            return "loginForm";
        }
        return "redirect:/aggiungi";
    }

    @GetMapping(value = "/aggiungi")
    public String aggiungiForm(Libro libro) {
        return "libroForm";
    }

    @PostMapping(value = "/aggiungi")
    public String postAggiungiForm(@Valid Libro libro, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "libroForm";
        }
        libri.add(libro);
        return "redirect:/risultato";
    }

    @GetMapping(value = "/risultato")
    public String getRisultato() {
        return "risultato";
    }

    public boolean controllaUsername(String username) {
        for (User user: utenti) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean login(String username, String password) {
        for (User user: utenti) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

}
