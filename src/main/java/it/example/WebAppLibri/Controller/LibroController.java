package it.example.WebAppLibri.Controller;

import com.google.gson.Gson;
import it.example.WebAppLibri.Dao.LibroDao;
import it.example.WebAppLibri.Dao.UserDao;
import it.example.WebAppLibri.Model.Libro;
import it.example.WebAppLibri.Model.User;
import it.example.WebAppLibri.Model.json.Item;
import it.example.WebAppLibri.Model.json.Root;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
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
    public String libroPage(@PathVariable("id")long id, Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Libro libro = libroRepository.findById(id);
        model.addAttribute("libro", libro);
        return "dettaglioLibro";
    }

    @GetMapping(value = "/aggiungi")
    public String aggiungiForm(Libro libro, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
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
    public String getNoleggio(@PathVariable("id") long idLibro, Model model,  HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("idLibro", idLibro);
        model.addAttribute("utenti", userRepository.findAll());
        return "noleggioLibro";
    }

    @PostMapping(value = "/noleggio/{id}")
    public String postNoleggio(@PathVariable("id") long idLibro, @RequestParam("utente") long idUtente) {
        User user = userRepository.findById(idUtente);
        if (user == null) {
            return "redirect:/noleggio/" + idLibro;
        }
        Libro libro = libroRepository.findById(idLibro);
        libro.setUtente(user);
        libroRepository.save(libro);
        return "redirect:/home";
    }

    @GetMapping("/sincronizza")
    @ResponseBody
    public String getLibriDatabase() {
        String url = "https://www.googleapis.com/books/v1/volumes?q=search+terms";
        RestTemplate restTemplate = new RestTemplate();
        Root root = restTemplate.getForObject(url, Root.class);
        if (root != null) {
            for (Item item: root.getItems()) {
                Libro libro = new Libro();
                if (item.getVolumeInfo() != null && item.getSaleInfo() != null) {
                    String frase = item.getVolumeInfo().getTitle().substring(0, 20);
                    item.getVolumeInfo().setTitle(frase);
                    libro.setTitolo(item.getVolumeInfo().getTitle());
                    if (item.getVolumeInfo().getAuthors() != null ) {
                        libro.setAutore(item.getVolumeInfo().getAuthors().get(0));
                    }
                    if (item.getSaleInfo().getListPrice() != null) {
                        libro.setPrezzo((int) item.getSaleInfo().getListPrice().getAmount());
                        String data = item.getVolumeInfo().getPublishedDate();
                        LocalDate localDate = LocalDate.parse(data);
                        int anno = localDate.getYear();
                        libro.setAnnoPubblicazione(anno);
                        libroRepository.save(libro);
                    }
                }
            }
        }
        Gson gson = new Gson();
        return gson.toJson(root);
    }

}
