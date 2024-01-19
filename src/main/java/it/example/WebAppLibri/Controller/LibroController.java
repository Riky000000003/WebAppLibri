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
public class LibroController {
    @Autowired
    private LibroDao libroRepository;
    @Autowired
    private UserDao userRepository;

    @GetMapping(value = "/home")
    public String homePage(Model model) {
        model.addAttribute("libri", libroRepository.findAll());
        return "home";
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
        libroRepository.save(libro);
        return "redirect:/home";
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

    @GetMapping (value = "/elimina/{idLibro}")
    public String deleteLibro(@PathVariable("idLibro") long idLibro) {
        Libro libro = libroRepository.findById(idLibro);
        libroRepository.delete(libro);
        return "redirect:/libri";
    }

    @GetMapping(value = "/noleggio/{id}")
    public String getNoleggio(@PathVariable("id") long idLibro, Model model) {
        model.addAttribute("idLibro", idLibro);
        model.addAttribute("utenti", userRepository.findAll());
        return "noleggioLibro";
    }
}
