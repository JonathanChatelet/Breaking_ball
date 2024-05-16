package fr.jonybegood.breakingball.entities;

import android.graphics.Color;

public class Brick {
    private float x, y; // Position de la brique
    private float width, height; // Taille de la brique
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
    public void setIsBroken(boolean isBroken) {
        this.isBroken = isBroken;
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

    public Brick(float x, float y, float width, float height, int color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isBroken = false; // Par défaut, la brique n'est pas cassée
        this.color = color;
    }
}

