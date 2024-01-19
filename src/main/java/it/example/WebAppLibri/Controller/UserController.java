package it.example.WebAppLibri.Controller;

import it.example.WebAppLibri.Dao.LibroDao;
import it.example.WebAppLibri.Dao.UserDao;
import it.example.WebAppLibri.Model.Libro;
import it.example.WebAppLibri.Model.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserDao userRepository;
    @Autowired
    private LibroDao libroRepository;

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
        if (userRepository.registrazione(user.getUsername()) != null) {
            model.addAttribute("statoErrore", true);
            model.addAttribute("errore", "username dell'utente esiste già");
            return "formUtente";
        }
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping(value = "/login")
    public String loginForm(User user) {
        return "loginForm";
    }

    @PostMapping(value = "/login")
    public String postLoginForm(@Valid User user, BindingResult bindingResult, Model model, HttpSession httpSession) {
        if (bindingResult.hasFieldErrors("username") || bindingResult.hasFieldErrors("password")) {
            model.addAttribute("statoErrore", false);
            return "loginForm";
        }
        User utenteLoggato = userRepository.login(user.getUsername(), user.getPassword());
        if (utenteLoggato == null){
            model.addAttribute("statoErrore", true);
            model.addAttribute("errore", "Utente non Loggato");
            return "loginForm";
        } else {
            httpSession.setAttribute("user", utenteLoggato);
        }
        return "redirect:/home";
    }

    @GetMapping(value = "/profilo")
    public String profiloPage(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("utente", user);
        return "dettaglioUtente";
    }

    @GetMapping(value = "/utenti")
    public String getListaUtenti(Model model, HttpSession httpSession) {
        List<User> utenti = (List<User>) userRepository.findAll();
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("utenti", utenti);
        return "listaUtenti";
    }

    @GetMapping(value = "/utenti/{id}")
    public String eliminaUtente(@PathVariable("id") long idUtente) {
        User user =  userRepository.findById(idUtente);
        if (user == null) {
            return "redirect:/profilo";
        }
        for (Libro libro: libroRepository.findAll()) {
            if (libro.getUtente() != null) {
                if (libro.getUtente().equals(user)) {
                    libro.setUtente(null);
                    libroRepository.save(libro);
                }
            }
        }
        userRepository.delete(user);
        return "redirect:/";
    }
}
