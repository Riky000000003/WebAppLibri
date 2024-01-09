package it.example.WebAppLibri.Model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class Libro {
    @NotNull
    @Size(min = 3, max = 20, message = "titolo non conforme min = 3, max = 20")
    private String titolo;
    @NotNull
    @Size(min = 3, max = 20, message = "autore non conforme min = 3, max = 20")
    private String autore;
    @Min(value = 1930, message = "anno deve superiore a 1930")
    @Max(value = 2024, message = "anno deve essere inferiore a 2024")
    private Integer annoPubblicazione;
    @Min(value = 1, message = "prezzo deve essere superiore a 1")
    private Integer prezzo;
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
}
