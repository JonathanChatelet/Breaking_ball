package fr.jonybegood.breakingball.entities;

import android.graphics.Color;

public class Brick {

    public static int BONUS_LIFE = 9;//'+'
    public static int BONUS_FIRE_PADDLE = 8;//'P'
    public static int BONUS_FIRE_BALL = 7;//'B'
    public static int BONUS_SPEED = 6;//'Q'
    public static int BONUS_SLOW = 5;//'S'
    public static int BONUS_MULT = 4;//'M'
    public static int BONUS_SMALLER = 3;//'L'
    public static int BONUS_LARGER = 2;//'H'
    public static int BONUS_GLUE = 1;//'G'
    public static int NO_BONUS = 0;//' '
    private float x, y; // Position de la brique
    private float width, height; // Taille de la brique

    private int bonus;
    private boolean isBroken; // Indique si la brique est cassée

    private int color;

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setBroken(boolean broken) {
        isBroken = broken;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean getIsBroken() {
        return isBroken;
    }

    public int getColor() {
        return color;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int getBonus() {
        return bonus;
    }

    public void setIsBroken(boolean isBroken) {

        if(this.getColor()!=Color.WHITE) {
            if(this.getColor()==Color.LTGRAY)
                this.setColor(Color.GRAY);
            else if(this.getColor()==Color.GRAY)
                this.setColor(Color.DKGRAY);
            else
                this.isBroken = isBroken;
        }
    }

    public Brick(float x, float y, float width, float height, int color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isBroken = false; // Par défaut, la brique n'est pas cassée
        this.color = color;
        this.bonus = NO_BONUS;
    }
}

