package fr.jonybegood.breakingball.entities;

import java.io.Serializable;

public class Game implements Serializable {
    private int life;
    private long score;

    private Profil p;

    private boolean sound_effect;
    private boolean music;

    public void setLife(int life) {
        this.life = life;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public void setP(Profil p) {
        this.p = p;
    }

    public void setSound_effect(boolean sound_effect) {
        this.sound_effect = sound_effect;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public int getLife() {
        return life;
    }

    public long getScore() {
        return score;
    }

    public Profil getP() {
        return p;
    }

    public boolean getSound_effect() {
        return sound_effect;
    }

    public boolean getMusic() {
        return music;
    }

    public Game(int life, long score, Profil p, boolean sound_effect, boolean music) {
        this.life = life;
        this.score = score;
        this.p = p;
        this.sound_effect = sound_effect;
        this.music=music;


    }

    public Game() {

        this.p = new Profil("", "", 0, 1, "");
        this.music=true;
        this.sound_effect=true;
        this.life=5;
        this.score=0;
    }
}
