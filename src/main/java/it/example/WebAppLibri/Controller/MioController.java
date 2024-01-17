package it.example.WebAppLibri.Controller;

import it.example.WebAppLibri.Dao.LibroDao;
import it.example.WebAppLibri.Dao.UserDao;
import it.example.WebAppLibri.Model.Libro;
import it.example.WebAppLibri.Model.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MioController {
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

    @GetMapping(value = "/home")
    public String homePage(Model model) {
        model.addAttribute("libri", libroRepository.findAll());
        return "home";
    }

    @GetMapping(value = "/profilo")
    public String profiloPage(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        model.addAttribute("utente", user);
        return "dettaglioUtente";
    }

    @GetMapping(value = "/libro/{id}")
    public String libroPage(@PathVariable("id")long id, Model model) {
        Libro libro = libroRepository.findById(id);
        model.addAttribute("libro", libro);
        return "dettaglioLibro";
    }

    @GetMapping(value = "/aggiungi")
    public String aggiungiForm(Libro libro) {
        return "libroForm";
    }

    @PostMapping(value = "/aggiungi")
    public String postAggiungiForm(@Valid Libro libro, BindingResult bindingResult, Model model, HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statoErrore", false);
            return "libroForm";
        }
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("statoErrore", false);
            model.addAttribute("errore", "Errore non sei loggato");
            return "redirect:/login";
        }
        libro.setUtente(user);
        libroRepository.save(libro);
        return "redirect:/home";
    }

    @GetMapping("/modifica/{idLibro}")
    public String modifica(@PathVariable("idLibro") long idLibro, Model model, HttpSession httpSession, Libro libro) {
        User user = (User)  httpSession.getAttribute("user");
        if(user == null) {
            return "redirect:/login";
        }
        libro = libroRepository.findById(idLibro);
        model.addAttribute("libro", libro);
        return "modificaLibro";
    }

    @PostMapping("/modifica/{idLibro}")
    public String modificaLibro(@Valid Libro libro, @PathVariable("idLibro") long idLibro, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statoErrore", false);
            return "modificaLibro";
        }
        Libro libroModificato = libroRepository.findById(idLibro);
        libroModificato.setTitolo(libro.getTitolo());
        libroModificato.setPrezzo(libro.getPrezzo());
        libroModificato.setAnnoPubblicazione(libro.getAnnoPubblicazione());
        libroModificato.setAutore(libro.getAutore());
        libroRepository.save(libroModificato);
        return "redirect:/libro/" + libroModificato.getId();
    }

    @GetMapping(value = "/libri")
    public String getLibriUtente(HttpSession httpSession, Model model) {
        User user = (User)  httpSession.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<Libro> libri = libroRepository.libriUtenti(user.getId());
        model.addAttribute("libri", libri);
        return "libriUtente";
    }

    @GetMapping (value = "/elimina/{idLibro}")
    public String deleteLibro(@PathVariable("idLibro") long idLibro) {
        Libro libro = libroRepository.findById(idLibro);
        libroRepository.delete(libro);
        return "redirect:/libri";
    }

    @GetMapping(value = "/utenti")
    public String getListaUtenti(Model model) {
        List<User> utenti = (List<User>) userRepository.findAll();
        model.addAttribute("utenti", utenti);
        return "listaUtenti";
    }
}
