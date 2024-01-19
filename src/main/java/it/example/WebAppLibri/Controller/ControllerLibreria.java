package it.example.WebAppLibri.Controller;

import com.google.gson.Gson;
import it.example.WebAppLibri.Dao.LibroDao;
import it.example.WebAppLibri.Model.Libro;
import it.example.WebAppLibri.Model.User;
import it.example.WebAppLibri.Model.json.Item;
import it.example.WebAppLibri.Model.json.Root;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
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

    @GetMapping("/sincronizza")
    public Root getLibriDatabase() {
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
        return root;
    }

}
