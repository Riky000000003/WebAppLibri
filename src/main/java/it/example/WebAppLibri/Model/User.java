package it.example.WebAppLibri.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Utenti")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Size(min = 2, max = 20, message = "nome non conforme min = 2, max = 20")
    private String nome;
    @Size(min = 2, max = 20, message = "cognome non conforme min = 2, max = 20")
    private String cognome;
    @NotNull
    @Size(min = 5, max = 20, message = "username non conforme min = 5, max = 20")
    private String username;
    @NotNull
    @Size(min = 5, max = 20, message = "password non conforme min = 5, max = 20")
    private String password;
    @OneToMany(mappedBy = "utente")
    private Set<Libro> libri = new HashSet<>();
    public User() {}
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
