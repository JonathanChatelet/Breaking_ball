package fr.jonybegood.breakingball.entities;

public class Ball {
    private float x, y; // Position de la balle
    private float dx, dy; // Vitesse de la balle
    private float radius; // Rayon de la balle

    private int speed, rebound;

    private char name;

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }
    public void setName(char name) {
        this.name = name;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
    public void setSpeed(int speed) { this.speed=speed; }
    public void setRebound(int rebound) { this.rebound=rebound; }

    public int getSpeed() { return speed; }
    public int getRebound() { return rebound; }
    public float getX() { return x; }

    public float getY() {
        return y;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    public float getRadius() {
        return radius;
    }
    public char getName() {
        return name;
    }

    public Ball(float x, float y, float dx, float dy, float radius) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.radius = radius;
        this.name=' ';
        this.speed = 0;
        this.rebound = 0;
    }
}

