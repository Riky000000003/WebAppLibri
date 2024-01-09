package it.example.WebAppLibri.Controller;

import it.example.WebAppLibri.Model.Libro;
import it.example.WebAppLibri.Model.User;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MioController {

    public List<User> utenti = new ArrayList<>();
    public  List<Libro> libri = new ArrayList<>();

    @GetMapping(value = "/")
    public String registraForm(User user) {
        return "formUtente";
    }

    @PostMapping(value = "/")
    public String postRegistraForm(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statoErrore", false);
            return "formUtente";
        }
        if (controllaUsername(user.getUsername())) {
            model.addAttribute("statoErrore", true);
            model.addAttribute("errore", "Utente non registrato, questo username è gia registrato");
            return "formUtente";
        }
        model.addAttribute("statoErrore", false);
        utenti.add(user);
        return "redirect:/login";
    }

    @GetMapping(value = "/login")
    public String loginForm(User user) {
        return "loginForm";
    }

    @PostMapping(value = "/login")
    public String postLoginForm(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasFieldErrors("username") || bindingResult.hasFieldErrors("password")) {
            model.addAttribute("statoErrore", false);
            return "loginForm";
        } else if (!login(user.getUsername(),user.getPassword())){
            model.addAttribute("statoErrore", true);
            model.addAttribute("errore", "Utente non Loggato");
            return "loginForm";
        }
        model.addAttribute("statoErrore", false);
        return "redirect:/home";
    }

    @GetMapping(value = "/home")
    public String homePage() {
        return "home";
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
