package fr.jonybegood.breakingball.entities;

public class Bonus
{
        private float x, y; // Position de la balle
        private float dx, dy; // Vitesse de la balle
        private float radius; // Rayon de la balle

        private int value;

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

        public void setRadius(float radius) {
            this.radius = radius;
        }
        public void setValue(int value) { this.value=value; }

        public int getValue() { return value; }
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

        public Bonus(float x, float y, float dx, float dy, float radius, int value) {
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
            this.radius = radius;
            this.value = value;
        }
}
