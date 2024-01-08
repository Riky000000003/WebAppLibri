package it.example.WebAppLibri.Model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class User {
    @Size(min = 2, max = 20)
    private String nome;
    @Size(min = 2, max = 20)
    private String cognome;
    @Size(min = 5, max = 20)
    private String username;
    @Size(min = 5, max = 20)
    private String password;
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
