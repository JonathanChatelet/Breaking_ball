package fr.jonybegood.breakingball.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import fr.jonybegood.breakingball.database.DbHelper;
import fr.jonybegood.breakingball.entities.Ball;
import fr.jonybegood.breakingball.entities.Paddle;
import fr.jonybegood.breakingball.entities.Brick;

import fr.jonybegood.breakball.tools.ScreenTools;
import fr.jonybegood.breakingball.R;
import fr.jonybegood.breakingball.tools.BooleanWrapper;
import fr.jonybegood.breakingball.tools.GestureTools;



public class TetrixView extends View {


    private DbHelper db;
    private Game current_game;
    private TextView tvGameInfo, tvScore,tvHighscore;
    private MediaPlayer mediaPlayerStone, mediaPlayerLose, mediaPlayerGameOver;
    private GestureDetector gestureDetector;
    private BooleanWrapper runningThread = new BooleanWrapper(true);
    private static Context context;

    private Paint paint, paintText;
    private int blinkingText;

    private static final int BRICK_HEIGHT = 30;
    private static final int BRICK_WIDTH = 50;
    private static final int BOTTOM_LIMIT = 400;
    private static final int NB_COLUMNS = 20;
    private boolean flagLoose;
    private static int bottomLimit;
    private List<Brick> bricks;
    public TetrixView(Context context, Game game, TextView tvGameInfo, TextView tvScore, TextView tvHighscore, BooleanWrapper runningThread) {
        super(context);
        this.context = context;
        this.tvGameInfo = tvGameInfo;
        this.tvScore = tvScore;
        this.tvHighscore = tvHighscore;
        this.runningThread = runningThread;

        db = new DbHelper(context);

        GestureTools gestureTools = new GestureTools(){
            @Override
            public void onSwipeUp(){

            }
        };

        gestureDetector = new GestureDetector(context, gestureTools);

        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        mediaPlayerLose= MediaPlayer.create(this.getContext(), R.raw.lose);
        mediaPlayerGameOver= MediaPlayer.create(this.getContext(), R.raw.game_over);
        mediaPlayerStone = MediaPlayer.create(this.getContext(), R.raw.stone);

        bottomLimit = ScreenTools.getScreenHeight(context) - BOTTOM_LIMIT;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!(flagLoose)) {
            if (!tvScore.getText().equals(String.valueOf(current_game.getScore())))
                tvScore.setText(String.valueOf(current_game.getScore()));
            if (!tvGameInfo.getText().equals("Level " + String.valueOf(current_game.getCurrent_level()) + " - Life : " + String.valueOf(current_game.getLife())))
                tvGameInfo.setText("Level " + String.valueOf(current_game.getCurrent_level()) + " - Life : " + String.valueOf(current_game.getLife()));
            if (current_game.getScore() > current_game.getP().getHighScore()) {
                current_game.getP().setHighScore(current_game.getScore());
                tvHighscore.setText(String.valueOf(current_game.getP().getHighScore()));
            }

            canvas.drawColor(Color.BLACK);
            paint.setColor(Color.BLACK);
            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

            // Dessiner la limite basse
            paint.setColor(Color.LTGRAY);
            canvas.drawRect(0, bottomLimit - 1, ScreenTools.getScreenWidth(context), bottomLimit + 1, paint);

            // Dessiner la limite haute
            paint.setColor(Color.LTGRAY);
            canvas.drawRect(0, 0, ScreenTools.getScreenWidth(context), 2, paint);

            // Dessiner les briques
            for (Brick brick : bricks) {
                if (!brick.getIsBroken()) {
                    paint.setColor(brick.getColor());
                    canvas.drawRect(brick.getX(), brick.getY(), brick.getX() + brick.getWidth(), brick.getY() + brick.getHeight(), paint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_POINTER_DOWN :
            {
                break;
            }
            case MotionEvent.ACTION_POINTER_UP : {
                break;
            }
            case MotionEvent.ACTION_UP : {
                break;
            }
        }
        return true;
    }
}
