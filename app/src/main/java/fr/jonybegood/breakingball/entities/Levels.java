package fr.jonybegood.breakingball.entities;

import java.util.ArrayList;
import java.util.List;

public class Levels {


    List<Level> levels = new ArrayList<>();
    final int NB_COLUMNS = 20;

    private Level level01 = new Level(8);
    private Level level02 = new Level(8);
    private Level level03 = new Level(8);
    private Level level04 = new Level(8);
    private Level level05 = new Level(8);
    private Level level06 = new Level(8);
    private Level level07 = new Level(8);
    private Level level08 = new Level(8);
    private Level level09 = new Level(8);
    private Level level10 = new Level(8);
    private Level level11 = new Level(8);
    private Level level12 = new Level(8);
    private Level level13 = new Level(8);
    private Level level14 = new Level(8);
    private Level level15 = new Level(8);
    private Level level16 = new Level(8);
    private Level level17 = new Level(8);
    private Level level18 = new Level(8);
    private Level level19 = new Level(8);
    private Level level20 = new Level(8);

    public Levels() {
        // W = White (unbreakable) / G = Gray (breakable 3 times) / E = Empty / R = Random
        Character[] line = {'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R'};
        Character[] lineGray = {'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'};
        Character[] lineBorderWhite = {'W', 'W', 'W', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'W', 'W', 'W'};
        Character[] lineAlternateGray1 = {'R', 'G', 'R', 'G', 'R', 'G', 'R', 'G', 'R', 'G', 'R', 'G', 'R', 'G', 'R', 'G', 'R', 'G', 'R', 'G'};
        Character[] lineAlternateGray2 = {'G', 'R', 'G', 'R', 'G', 'R', 'G', 'R', 'G', 'R', 'G', 'R', 'G', 'R', 'G', 'R', 'G', 'R', 'G', 'R'};
        Character[] lineAlternateWhite = {'W', 'W', 'R', 'R', 'R', 'R', 'W', 'W', 'R', 'R', 'R', 'R', 'W', 'W', 'R', 'R', 'R', 'R', 'W', 'W'};
        Character[] lineWhiteEmpty = {'W', 'W', 'W', 'W','W', 'W', 'W', 'W', 'W', 'E','E', 'W', 'W', 'W', 'W', 'W','W', 'W', 'W', 'W'};
        Character[] lineWhiteGray = {'W', 'W', 'W', 'W','W', 'W', 'W', 'W', 'W', 'G','G', 'W', 'W', 'W', 'W', 'W','W', 'W', 'W', 'W'};
        Character[] lineChanging;

        //level1
        for (int i = 0; i < level01.nb_ligne; i++)
            level01.lines.add(line);
        levels.add(level01);

        //level2
        for (int i = 0; i < level02.nb_ligne; i++)
        {
            if(i==3)
                level02.lines.add(lineGray);
            else
                level02.lines.add(line);
        }
        levels.add(level02);

        //level3
        for (int i = 0; i < level03.nb_ligne; i++)
        {
            if(i==3||i==0)
                level03.lines.add(lineGray);
            else
                level03.lines.add(line);
        }
        levels.add(level03);

        //level4
        for (int i = 0; i < level04.nb_ligne; i++)
        {
            if(i==3)
                level04.lines.add(lineGray);
            else if(i==7)
                level04.lines.add(lineBorderWhite);
            else
                level04.lines.add(line);
        }
        levels.add(level04);

        //level5
        for (int i = 0; i < level05.nb_ligne; i++)
        {
            if(i==3||i==0)
                level05.lines.add(lineGray);
            else if(i==7)
                level05.lines.add(lineBorderWhite);
            else
                level05.lines.add(line);
        }
        levels.add(level05);

        //level6
        for (int i = 0; i < level06.nb_ligne; i++)
        {
            if(i==4||i==0)
                level06.lines.add(lineGray);
            else if(i==7||i==3)
                level06.lines.add(lineBorderWhite);
            else
                level06.lines.add(line);
        }
        levels.add(level06);

        //level7
        for (int i = 0; i < level07.nb_ligne; i++)
        {
            if(i%2==0)
                level07.lines.add(lineAlternateGray1);
            else
                level07.lines.add(line);
        }
        levels.add(level07);

        //level8
        for (int i = 0; i < level08.nb_ligne; i++)
        {
            if(i==0||i==2||i==6)
                level08.lines.add(lineAlternateGray1);
            else if(i==1||i==5||i==7)
                level08.lines.add(lineAlternateGray2);
            else
                level08.lines.add(line);
        }
        levels.add(level08);

        //level9
        for (int i = 0; i < level09.nb_ligne; i++)
        {
            if(i%2==0)
                level09.lines.add(lineAlternateGray1);
            else
                level09.lines.add(lineAlternateGray2);
        }
        levels.add(level09);

        //level10 Heart
        level10.nb_ligne=9;
        lineChanging = new Character[]{'R', 'R', 'R', 'R', 'R', 'R', 'W', 'R', 'R', 'R', 'R', 'R', 'R', 'W', 'R', 'R', 'R', 'R', 'R', 'R'};
        level10.lines.add(lineChanging);
        lineChanging = new Character[]{'R', 'R', 'R', 'R', 'R', 'W', 'R', 'W', 'R', 'R', 'R', 'R', 'W', 'R', 'W', 'R', 'R', 'R', 'R', 'R'};
        level10.lines.add(lineChanging);
        lineChanging = new Character[]{'R', 'R', 'R', 'R', 'W', 'R', 'R', 'R', 'W', 'R', 'R', 'W', 'R', 'R', 'R', 'W', 'R', 'R', 'R', 'R'};
        level10.lines.add(lineChanging);
        lineChanging = new Character[]{'R', 'R', 'R', 'R', 'W', 'R', 'R', 'R', 'R', 'W', 'W', 'R', 'R', 'R', 'R', 'W', 'R', 'R', 'R', 'R'};
        level10.lines.add(lineChanging);
        lineChanging = new Character[]{'R', 'R', 'R', 'R', 'W', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'W', 'R', 'R', 'R', 'R'};
        level10.lines.add(lineChanging);
        lineChanging = new Character[]{'R', 'R', 'R', 'R', 'R', 'W', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'W', 'R', 'R', 'R', 'R', 'R'};
        level10.lines.add(lineChanging);
        lineChanging = new Character[]{'R', 'R', 'R', 'R', 'R', 'R', 'W', 'R', 'R', 'R', 'R', 'R', 'R', 'W', 'R', 'R', 'R', 'R', 'R', 'R'};
        level10.lines.add(lineChanging);
        lineChanging = new Character[]{'R', 'R', 'R', 'R', 'R', 'R', 'R', 'W', 'R', 'R', 'R', 'R', 'W', 'R', 'R', 'R', 'R', 'R', 'R', 'R'};
        level10.lines.add(lineChanging);
        lineChanging = new Character[]{'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'W', 'R', 'R', 'W', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R'};
        level10.lines.add(lineChanging);
        levels.add(level10);

        //level11
        for (int i = 0; i < level11.nb_ligne; i++)
        {
            if((i+1)%4==0)
                level11.lines.add(lineAlternateWhite);
            else
                level11.lines.add(line);
        }
        levels.add(level11);

        //level12
        for (int i = 0; i < level12.nb_ligne; i++)
        {
            if((i+1)%4==0)
                level12.lines.add(lineAlternateWhite);
            else if((i+3)%4==0)
                level12.lines.add(lineAlternateGray1);
            else
                level12.lines.add(line);
        }
        levels.add(level12);

        //level13
        for (int i = 0; i < level13.nb_ligne; i++)
        {
            if(i%2==0)
                level13.lines.add(lineAlternateWhite);
            else
                level13.lines.add(line);
        }
        levels.add(level13);

        //level14
        for (int i = 0; i < level14.nb_ligne; i++)
        {
            if(i>5)
                level14.lines.add(lineAlternateWhite);
            else if(i==5||i==0)
                level14.lines.add(lineGray);
            else
                level14.lines.add(line);
        }
        levels.add(level14);

        //level15
        level15.nb_ligne = 9;
        for (int i = 0; i < level15.nb_ligne; i++)
        {
            if(i==8)
                level15.lines.add(lineWhiteEmpty);
            else if(i%4==0)
                level15.lines.add(lineGray);
            else
                level15.lines.add(line);
        }
        levels.add(level15);

        //level16
        level16.nb_ligne = 9;
        for (int i = 0; i < level16.nb_ligne; i++)
        {
            if(i==8)
                level16.lines.add(lineWhiteEmpty);
            else if(i%2==0)
                level16.lines.add(lineAlternateGray1);
            else
                level16.lines.add(line);
        }
        levels.add(level16);

        //level17
        level17.nb_ligne = 9;
        for (int i = 0; i < level17.nb_ligne; i++)
        {
            if(i==8)
                level17.lines.add(lineWhiteEmpty);
            else if(i%2==0)
                level17.lines.add(lineAlternateGray1);
            else
                level17.lines.add(lineAlternateGray2);
        }
        levels.add(level17);

        //level18
        level18.nb_ligne = 9;
        for (int i = 0; i < level18.nb_ligne; i++)
        {
            if(i==8)
                level18.lines.add(lineWhiteGray);
            else if(i==7||i==0)
                level18.lines.add(lineGray);
            else if(i%2==0)
                level18.lines.add(lineAlternateGray1);
            else
                level18.lines.add(lineAlternateGray2);
        }
        levels.add(level18);


        //level19 Heart mid Gray
        level19.nb_ligne=9;
        lineChanging = new Character[]{'G', 'G', 'G', 'G', 'G', 'G', 'W', 'G', 'G', 'G', 'G', 'G', 'G', 'W', 'G', 'G', 'G', 'G', 'G', 'G'};
        level19.lines.add(lineChanging);
        lineChanging = new Character[]{'G', 'G', 'G', 'G', 'G', 'W', 'R', 'W', 'G', 'G', 'G', 'G', 'W', 'R', 'W', 'G', 'G', 'G', 'G', 'G'};
        level19.lines.add(lineChanging);
        lineChanging = new Character[]{'G', 'G', 'G', 'G', 'W', 'R', 'R', 'R', 'W', 'G', 'G', 'W', 'R', 'R', 'R', 'W', 'G', 'G', 'G', 'G'};
        level19.lines.add(lineChanging);
        lineChanging = new Character[]{'G', 'G', 'G', 'G', 'W', 'R', 'R', 'R', 'R', 'W', 'W', 'R', 'R', 'R', 'R', 'W', 'G', 'G', 'G', 'G'};
        level19.lines.add(lineChanging);
        lineChanging = new Character[]{'G', 'G', 'G', 'G', 'W', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'W', 'G', 'G', 'G', 'G'};
        level19.lines.add(lineChanging);
        lineChanging = new Character[]{'G', 'G', 'G', 'G', 'G', 'W', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'W', 'G', 'G', 'G', 'G', 'G'};
        level19.lines.add(lineChanging);
        lineChanging = new Character[]{'G', 'G', 'G', 'G', 'G', 'G', 'W', 'R', 'R', 'R', 'R', 'R', 'R', 'W', 'G', 'G', 'G', 'G', 'G', 'G'};
        level19.lines.add(lineChanging);
        lineChanging = new Character[]{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'W', 'R', 'R', 'R', 'R', 'W', 'R', 'G', 'G', 'G', 'G', 'G', 'G'};
        level19.lines.add(lineChanging);
        lineChanging = new Character[]{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'W', 'G', 'G', 'W', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'};
        level19.lines.add(lineChanging);
        levels.add(level09);

        //level20 Heart complete Gray
        level19.nb_ligne=9;
        lineChanging = new Character[]{'G', 'G', 'G', 'G', 'G', 'G', 'W', 'G', 'G', 'G', 'G', 'G', 'G', 'W', 'G', 'G', 'G', 'G', 'G', 'G'};
        level19.lines.add(lineChanging);
        lineChanging = new Character[]{'G', 'G', 'G', 'G', 'G', 'W', 'G', 'W', 'G', 'G', 'G', 'G', 'W', 'G', 'W', 'G', 'G', 'G', 'G', 'G'};
        level19.lines.add(lineChanging);
        lineChanging = new Character[]{'G', 'G', 'G', 'G', 'W', 'G', 'G', 'G', 'W', 'G', 'G', 'W', 'G', 'G', 'G', 'W', 'G', 'G', 'G', 'G'};
        level19.lines.add(lineChanging);
        lineChanging = new Character[]{'G', 'G', 'G', 'G', 'W', 'G', 'G', 'G', 'G', 'W', 'W', 'G', 'G', 'G', 'G', 'W', 'G', 'G', 'G', 'G'};
        level19.lines.add(lineChanging);
        lineChanging = new Character[]{'G', 'G', 'G', 'G', 'W', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'W', 'G', 'G', 'G', 'G'};
        level19.lines.add(lineChanging);
        lineChanging = new Character[]{'G', 'G', 'G', 'G', 'G', 'W', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'W', 'G', 'G', 'G', 'G', 'G'};
        level19.lines.add(lineChanging);
        lineChanging = new Character[]{'G', 'G', 'G', 'G', 'G', 'G', 'W', 'G', 'G', 'G', 'G', 'G', 'G', 'W', 'G', 'G', 'G', 'G', 'G', 'G'};
        level19.lines.add(lineChanging);
        lineChanging = new Character[]{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'W', 'G', 'G', 'G', 'G', 'W', 'R', 'G', 'G', 'G', 'G', 'G', 'G'};
        level19.lines.add(lineChanging);
        lineChanging = new Character[]{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'W', 'G', 'G', 'W', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'};
        level19.lines.add(lineChanging);
        levels.add(level09);

    }
}
