
package fr.jonybegood.breakingball.tools;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureTools extends GestureDetector.SimpleOnGestureListener {

    @Override
    public boolean onDown (MotionEvent e) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // Détecter un geste de haut en bas
        if (e2.getY() < e1.getY()) {
            // Geste de bas en haut détecté
            onSwipeUp();
            return true;
        }
        return false;
    }

    public void onSwipeUp() {
        // Gérer l'action de swipe down ici
    }
}


