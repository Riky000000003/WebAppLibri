package it.example.WebAppLibri.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Libri")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "Titolo")
    @NotNull
    @Size(min = 3, max = 20, message = "titolo non conforme min = 3, max = 20")
    private String titolo;
    @Column(name = "autore")
    @NotNull
    @Size(min = 3, max = 20, message = "autore non conforme min = 3, max = 20")
    private String autore;
    @Column(name = "Anno di Pubblicazione")
    @Min(value = 1930, message = "anno deve superiore a 1930")
    @Max(value = 2024, message = "anno deve essere inferiore a 2024")
    private Integer annoPubblicazione;
    @Column(name = "Prezzo")
    @Min(value = 1, message = "prezzo deve essere superiore a 1")
    private Integer prezzo;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_utente")
    private User utente;

    public Libro() {}

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public Integer getAnnoPubblicazione() {
        return annoPubblicazione;
    }

    public void setAnnoPubblicazione(Integer annoPubblicazione) {
        this.annoPubblicazione = annoPubblicazione;
    }

    public Integer getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Integer prezzo) {
        this.prezzo = prezzo;
    }
    public User getUtente() {
        return utente;
    }
    public void setUtente(User utente) {
        this.utente = utente;
    }
}
