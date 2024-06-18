package fr.jonybegood.breakingball.entities;

import java.io.Serializable;
import java.util.Objects;

public class Profil implements Serializable {

    private String pseudo;

    private String password;

    private long highScore;

    private long tetrix_highscore;

    private int level;

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHighScore(long highScore) {
        this.highScore = highScore;
    }
    public void setTetrixHighScore(long tetrix_highscore) {
        this.tetrix_highscore = tetrix_highscore;
    }


    public void setLevel(int level) {
        this.level = level;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getPassword() {
        return password;
    }

    public long getHighScore() {
        return highScore;
    }

    public long getTetrixHighScore() {
        return tetrix_highscore;
    }

    public int getLevel() {
        return level;
    }

    public String getPhoto() {
        return photo;
    }

    private String photo;

    public Profil(String pseudo, String password, long highScore, int level,String photo,long tetrix_highscore) {
        this.pseudo = pseudo;
        this.password = password;
        this.highScore = highScore;
        this.level = level;
        this.photo = photo;
        this.tetrix_highscore=tetrix_highscore;
    }

    public Profil() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profil profil = (Profil) o;
        return highScore == profil.highScore && level == profil.level && Objects.equals(pseudo, profil.pseudo) && Objects.equals(password, profil.password) && Objects.equals(photo, profil.photo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pseudo, password, highScore, level, photo);
    }

    @Override
    public String toString() {
        return pseudo;
    }
}
