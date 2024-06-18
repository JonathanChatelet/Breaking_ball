package fr.jonybegood.breakingball.entities;

import java.io.Serializable;

public class Game implements Serializable {
    private int life;
    private long score;

    private long tetrix_score;

    private int current_level;

    private Profil p;

    private boolean sound_effect;
    private boolean music;

    public void setLife(int life) {
        this.life = life;
    }

    public void setCurrent_level(int current_level) {
        this.current_level = current_level;
    }

    public void setScore(long score) {
        this.score = score;
    }
    public void setTetrixScore(long tetrix_score) {
        this.tetrix_score = tetrix_score;
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

    public int getCurrent_level() {
        return current_level;
    }

    public long getScore() {
        return score;
    }

    public long getTetrixScoreScore() {
        return tetrix_score;
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
        current_level = p.getLevel();
        this.tetrix_score=0;
    }

    public Game() {

        this.p = new Profil("", "", 0, 1, "",0);
        this.music=true;
        this.sound_effect=true;
        this.life=5;
        this.tetrix_score=0;
        this.score=0;
    }
}
