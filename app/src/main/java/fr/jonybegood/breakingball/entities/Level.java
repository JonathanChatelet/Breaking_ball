package fr.jonybegood.breakingball.entities;

import java.util.ArrayList;
import java.util.List;

public class Level {

    int nb_ligne;
    List<Character[]> lines = new ArrayList<>();

    public Level(int nb_ligne) {
        this.nb_ligne = nb_ligne;
    }
}
