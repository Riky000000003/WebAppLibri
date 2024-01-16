package it.example.WebAppLibri.Dao;

import it.example.WebAppLibri.Model.Libro;
import it.example.WebAppLibri.Model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LibroDao extends CrudRepository<Libro, Long> {
    Libro findById(long id);
    @Query("select l from Libro l where titolo= :titolo")
    public Libro controllaLibro(String titolo);
    @Query("select l from Libro l where l.utente.id = :id_utente")
    public List<Libro> libriUtenti(long id_utente);
}
