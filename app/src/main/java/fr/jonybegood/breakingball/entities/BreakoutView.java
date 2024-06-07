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

public class BreakoutView extends View {
    private List<Ball> balls;
    private List<Bonus> bonuss;

    private List<Ball> fireJet;

    private Paddle paddle;
    private List<Brick> bricks;
    private Paint paint, paintText;

    private boolean start_flag=false,fire_paddle=false,fire_ball=false,glue=false;

    private static final int BALL_RADIUS = 10;
    private static final int BALL_SPEED = -6;

    private static final int FIRE_RADIUS = 3;
    private static final int FIRE_SPEED = -15;

    private static final int BONUS_RADIUS = 20;
    private static final int BONUS_SPEED = 1;
    private static final int PADDLE_SPEED = 20;
    private static final int PADDLE_HEIGHT = 20;
    private static final int PADDLE_WIDTH = 100;
    private static final int BRICK_HEIGHT = 30;
    private static final int BRICK_WIDTH = 50;

    private static int BRICK_START_X = 5;
    private static final int BRICK_START_Y = 20;

    private int nb_row, paddle_width;

    private static final int BOTTOM_LIMIT = 400;

    private static final int NB_COLUMNS = 20;

    private static int bottomLimit;

    private Levels levels;

    private static final int COLOR[] = {Color.GREEN,Color.RED,Color.BLUE,Color.CYAN,Color.MAGENTA,Color.YELLOW};
    private static final int NB_COLOR = 6;

    private static Context context;

    private DbHelper db;

    private BooleanWrapper runningThread = new BooleanWrapper(true);
    private Thread checCollisionThread;

    private MediaPlayer mediaPlayerBrick,mediaPlayerBoing,mediaPlayerStone, mediaPlayerWin, mediaPlayerLose, mediaPlayerGameOver;

    private boolean flagWin,flagLoose,flagEndGame,notDoubleRebound=true;
    private Game current_game;

    private TextView tvGameInfo, tvScore,tvHighscore;

    private int scoreMultiplier;

    private int blinkingText;

    private GestureDetector gestureDetector;

    private int tap_count;

    public BreakoutView(Context context,Game game, TextView tvGameInfo, TextView tvScore, TextView tvHighscore,BooleanWrapper runningThread) {
        super(context);
        this.context=context;
        this.tvGameInfo = tvGameInfo;
        this.tvScore =tvScore;
        this.tvHighscore=tvHighscore;
        this.runningThread=runningThread;
        balls = new ArrayList<>();
        bonuss = new ArrayList<>();
        fireJet = new ArrayList<>();

        current_game = game;
        levels = new Levels();
        db = new DbHelper(context);
        paddle_width = PADDLE_WIDTH;
        tap_count = 0;

        GestureTools gestureTools = new GestureTools(){
            @Override
            public void onSwipeUp(){
                if(flagWin||flagLoose)
                    initialiseScreenGame();
            }
        };

        checCollisionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(get_runningThread())
                {
                    try{
                        for(Ball ball : balls) {
                            // Gestion des collisions avec la raquette
                            if ((ball.getY() + BALL_RADIUS) > paddle.getY() && (ball.getY() + BALL_RADIUS) < paddle.getY() + paddle.getHeight() &&
                                    ball.getX() > paddle.getX() && ball.getX() < paddle.getX() + paddle.getWidth()) {
                                if (start_flag == false) start_flag = true;
                                else {
                                    if ((ball.getX() > paddle.getX() && ball.getX() < paddle.getX() + paddle.getWidth() / 4) || (ball.getX() > paddle.getX() + paddle.getWidth() * 3 / 4 && ball.getX() < paddle.getX() + paddle.getWidth())) {
                                        ball.setDy(ball.getSpeed() / 2);
                                    } else
                                        ball.setDy(ball.getSpeed());
                                    //ball.setDy(ball.getSpeed());
                                }
                                if (ball.getX() > paddle.getX() && ball.getX() < paddle.getX() + paddle.getWidth() / 4)
                                    ball.setDx(ball.getSpeed());
                                else if (ball.getX() > paddle.getX() + paddle.getWidth() * 3 / 4 && ball.getX() < paddle.getX() + paddle.getWidth())
                                    ball.setDx(-ball.getSpeed());
                                else if ((ball.getX() > paddle.getX() + paddle.getWidth() / 4) && (ball.getX() < paddle.getX() + paddle.getWidth() / 2))
                                    ball.setDx(ball.getSpeed());
                                else
                                    ball.setDx(-ball.getSpeed());
                                if (glue) {
                                    if(!ball.getBall_stop())
                                    {
                                        ball.setOffset(ball.getX()-paddle.getX());
                                        if (mediaPlayerBoing.isPlaying()) {
                                            mediaPlayerBoing.pause();
                                            mediaPlayerBoing.seekTo(0);
                                            mediaPlayerBoing.start();
                                        } else
                                            mediaPlayerBoing.start();
                                    }
                                    ball.setBall_stop(true);
                                }
                                else{
                                    if (mediaPlayerBoing.isPlaying()) {
                                        mediaPlayerBoing.pause();
                                        mediaPlayerBoing.seekTo(0);
                                        mediaPlayerBoing.start();
                                    } else
                                        mediaPlayerBoing.start();
                                }
                                if(notDoubleRebound){
                                    notDoubleRebound = false;
                                    ball.setRebound(ball.getRebound() + 1);
                                }
                            }
                        }
                    }
                    catch(Exception e){}
                }
            }
        });
        checCollisionThread.start();

        gestureDetector = new GestureDetector(context, gestureTools);

        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        initialiseScreenGame();

        mediaPlayerBrick = MediaPlayer.create(this.getContext(), R.raw.glass_hit);
        mediaPlayerWin = MediaPlayer.create(this.getContext(), R.raw.success);
        mediaPlayerLose= MediaPlayer.create(this.getContext(), R.raw.lose);
        mediaPlayerGameOver= MediaPlayer.create(this.getContext(), R.raw.game_over);
        mediaPlayerBoing = MediaPlayer.create(this.getContext(), R.raw.boing);
        mediaPlayerStone = MediaPlayer.create(this.getContext(), R.raw.stone);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boolean end_flag;

        if (!(flagWin||flagLoose))
        {
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

            // Dessiner la balle
            if(fire_ball)
                paint.setColor(Color.RED);
            else
                paint.setColor(Color.WHITE);
            for (Ball ball : balls)
            {
                canvas.drawCircle(ball.getX(), ball.getY(), ball.getRadius(), paint);
            }


            // Dessiner la raquette
            if(fire_paddle)
                paint.setColor(Color.YELLOW);
            else
                paint.setColor(Color.LTGRAY);
            canvas.drawRect(paddle.getX(), paddle.getY(), paddle.getX() + paddle.getWidth(), paddle.getY() + paddle.getHeight(), paint);

            // Dessiner les briques
            for (Brick brick : bricks) {
                if (!brick.getIsBroken()) {
                    paint.setColor(brick.getColor());
                    canvas.drawRect(brick.getX(), brick.getY(), brick.getX() + brick.getWidth(), brick.getY() + brick.getHeight(), paint);
                }
            }

            //Dessiner et deplacer les bonus
            List<Bonus> copy_bonuss = new ArrayList<>();
            for(Bonus bonus : bonuss){
                switch (bonus.getValue())
                {
                    case Brick.BONUS_LIFE : paint.setColor(Color.WHITE); break;
                    case Brick.BONUS_FIRE_PADDLE : paint.setColor(Color.YELLOW); break;
                    case Brick.BONUS_FIRE_BALL : paint.setColor(Color.RED); break;
                    case Brick.BONUS_SPEED : paint.setColor(Color.BLUE); break;
                    case Brick.BONUS_SLOW : paint.setColor(Color.DKGRAY); break;
                    case Brick.BONUS_MULT : paint.setColor(Color.LTGRAY); break;
                    case Brick.BONUS_SMALLER : paint.setColor(Color.GRAY); break;
                    case Brick.BONUS_LARGER : paint.setColor(Color.GREEN); break;
                    case Brick.BONUS_GLUE : paint.setColor(Color.CYAN); break;
                    default: paint.setColor(Color.BLACK);
                }
                canvas.drawCircle(bonus.getX(), bonus.getY(), bonus.getRadius(), paint);
                if(bonus.getY()+BONUS_RADIUS>=bottomLimit)
                    copy_bonuss.add(bonus);
                else
                    bonus.setY(bonus.getY() + bonus.getDy());
            }
            if(!copy_bonuss.isEmpty()&&copy_bonuss!=null)
                bonuss.removeAll(copy_bonuss);

            //Dessiner et deplacer les fireJet
            List<Ball> copy_fireJet = new ArrayList<>();
            for(Ball fire : fireJet){
                paint.setColor(Color.WHITE);
                canvas.drawCircle(fire.getX(), fire.getY(), fire.getRadius(), paint);
                if(fire.getY()<=0)
                    copy_fireJet.add(fire);
                else
                    fire.setY(fire.getY() + fire.getDy());
            }
            if(!copy_fireJet.isEmpty()&&copy_fireJet!=null)
                fireJet.removeAll(copy_fireJet);

            List<Ball> copy_balls = new ArrayList<>();
            for(Ball ball : balls)
            {
                // Déplacer la balle
                if (start_flag && !ball.getBall_stop()) {
                    ball.setX(ball.getX() + ball.getDx());
                    ball.setY(ball.getY() + ball.getDy());
                }

                // Gestion des collisions avec les bords de l'écran
                if (ball.getX() < 0 || ball.getX() > canvas.getWidth()) {
                    ball.setDx(-ball.getDx());
                }
                if (ball.getY() < 0) {
                    ball.setDy(-ball.getDy());
                }


                // Gestion des collisions avec les briques
                String dir = "";
                if (ball.getDy() > 0) dir += "D";
                else dir += "U";

                if (ball.getDx() > 0) dir += "R";
                else dir += "L";


                end_flag = true;
                for (Brick brick : bricks)
                {
                    if (!brick.getIsBroken()) {
                        if (brick.getColor() != Color.WHITE) end_flag = false;
                        if(!fire_ball||fire_ball&&(brick.getColor()==Color.WHITE||brick.getColor()==Color.DKGRAY||brick.getColor()==Color.GRAY||brick.getColor()==Color.LTGRAY))
                        {
                            switch (dir)
                            {
                                case "UR":
                                    if (ball.getX() + BALL_RADIUS > brick.getX() && ball.getX() - BALL_RADIUS < brick.getX() + brick.getWidth() &&
                                            ball.getY() + BALL_RADIUS > brick.getY() && ball.getY() - BALL_RADIUS < brick.getY() + brick.getHeight()) {
                                        //if ((ball.getY() - ball.getDy() > brick.getY() + brick.getHeight()) || (ball.getY() - ball.getDy() < brick.getY()))
                                        if (ball.getX() - brick.getX() > brick.getY() + brick.getHeight() - ball.getY())
                                            ball.setDy(-ball.getDy());
                                            //if ((ball.getX() - ball.getDx() < brick.getX()) || (ball.getX() - ball.getDx() > brick.getX() + brick.getWidth()))
                                        else
                                            ball.setDx(-ball.getDx());
                                        updateBrick(brick, ball);
                                    }
                                    break;
                                case "UL":
                                    if (ball.getX() + BALL_RADIUS > brick.getX() && ball.getX() - BALL_RADIUS < brick.getX() + brick.getWidth() &&
                                            ball.getY() + BALL_RADIUS > brick.getY() && ball.getY() - BALL_RADIUS < brick.getY() + brick.getHeight()) {
                                        //if ((ball.getY() - ball.getDy() > brick.getY() + brick.getHeight()) || (ball.getY() - ball.getDy() < brick.getY()))
                                        if (brick.getX() + brick.getWidth() - ball.getX() > brick.getY() + brick.getHeight() - ball.getY())
                                            ball.setDy(-ball.getDy());
                                            //if ((ball.getX() - ball.getDx() < brick.getX()) || (ball.getX() - ball.getDx() > brick.getX() + brick.getWidth()))
                                        else
                                            ball.setDx(-ball.getDx());
                                        updateBrick(brick, ball);
                                    }
                                    break;
                                case "DR":
                                    if (ball.getX() + BALL_RADIUS > brick.getX() && ball.getX() - BALL_RADIUS < brick.getX() + brick.getWidth() &&
                                            ball.getY() + BALL_RADIUS > brick.getY() && ball.getY() - BALL_RADIUS < brick.getY() + brick.getHeight()) {
                                        //if ((ball.getY() - ball.getDy() > brick.getY() + brick.getHeight()) || (ball.getY() - ball.getDy() < brick.getY()))
                                        if (ball.getX() - brick.getX() > ball.getY() - brick.getY())
                                            ball.setDy(-ball.getDy());
                                            //if ((ball.getX() - ball.getDx() < brick.getX()) || (ball.getX() - ball.getDx() > brick.getX() + brick.getWidth()))
                                        else
                                            ball.setDx(-ball.getDx());
                                        updateBrick(brick, ball);
                                    }
                                    break;
                                case "DL":
                                    if (ball.getX() + BALL_RADIUS > brick.getX() && ball.getX() - BALL_RADIUS < brick.getX() + brick.getWidth() &&
                                            ball.getY() + BALL_RADIUS > brick.getY() && ball.getY() - BALL_RADIUS < brick.getY() + brick.getHeight()) {
                                        //if ((ball.getY() - ball.getDy() > brick.getY() + brick.getHeight()) || (ball.getY() - ball.getDy() < brick.getY()))
                                        if (brick.getX() + brick.getWidth() - ball.getX() > ball.getY() - brick.getY())
                                            ball.setDy(-ball.getDy());
                                            //if ((ball.getX() - ball.getDx() < brick.getX()) || (ball.getX() - ball.getDx() > brick.getX() + brick.getWidth()))
                                        else
                                            ball.setDx(-ball.getDx());
                                        updateBrick(brick, ball);
                                    }
                                    break;
                                }
                        }
                        else
                        {
                            if (ball.getX() - BALL_RADIUS > brick.getX() && ball.getX() - BALL_RADIUS < brick.getX() + brick.getWidth() &&
                                    ball.getY() + BALL_RADIUS > brick.getY() && ball.getY() + BALL_RADIUS < brick.getY() + brick.getHeight())
                                updateBrick(brick, ball);
                        }
                    }
                }

                if (end_flag) {
                    flagWin = true;
                    flagEndGame = true;
                }

                if (flagWin) {
                    if (current_game.getSound_effect())
                        mediaPlayerWin.start();
                }

                if(ball.getY()>bottomLimit)
                    copy_balls.add(ball);
            }
            if(!copy_balls.isEmpty()&&copy_balls!=null)
                balls.removeAll(copy_balls);

            List<Bonus> copy_bonus = new ArrayList<>();
            for(Bonus bonus : bonuss)
            {
                // Gestion des collisions des bonus avec la raquette
                if ((bonus.getY() + BONUS_RADIUS) > paddle.getY() && (bonus.getY() + BONUS_RADIUS) < paddle.getY() + paddle.getHeight() &&
                        bonus.getX() > paddle.getX() && bonus.getX() < paddle.getX() + paddle.getWidth()) {
                    switch (bonus.getValue())
                    {
                        case Brick.BONUS_LIFE :
                            current_game.setLife(current_game.getLife()+1);
                            break;
                        case Brick.BONUS_FIRE_PADDLE :
                            fire_paddle = true;
                            break;
                        case Brick.BONUS_FIRE_BALL :
                            fire_ball = true;
                            break;
                        case Brick.BONUS_SPEED :
                            for (Ball ball:balls)
                            {
                                ball.setSpeed(ball.getSpeed()*2);
                            }
                            break;
                        case Brick.BONUS_SLOW :
                            for (Ball ball:balls)
                            {
                                ball.setSpeed(ball.getSpeed()/2);
                            }
                            break;
                        case Brick.BONUS_MULT :
                            List <Ball> balls_add = new ArrayList<>();
                            for (Ball ball:balls)
                            {
                                Ball ball1 = new Ball(ball.getX(),ball.getY(),ball.getDx(),ball.getDy()+2,BALL_RADIUS);
                                ball1.setSpeed(BALL_SPEED);
                                Ball ball2 = new Ball(ball.getX(),ball.getY(),ball.getDx(),ball.getDy()-2,BALL_RADIUS);
                                ball2.setSpeed(BALL_SPEED);
                                balls_add.add(ball1);
                                balls_add.add(ball2);
                            }
                            balls.addAll(balls_add);
                            break;
                        case Brick.BONUS_SMALLER :
                            paddle.setWidth(paddle.getWidth()/2);
                            break;
                        case Brick.BONUS_LARGER :
                            paddle.setWidth(paddle.getWidth()*2);
                            break;
                        case Brick.BONUS_GLUE :
                            glue = true;
                            break;
                    }
                    copy_bonus.add(bonus);
                }
            }
            if(!copy_bonus.isEmpty()&&copy_bonus!=null)
                bonuss.removeAll(copy_bonus);
            //gestion de la défaite
            if (balls.isEmpty())
            {
                current_game.setLife(current_game.getLife() - 1);
                if (current_game.getSound_effect()) {
                    if (current_game.getLife() == 0)
                        mediaPlayerGameOver.start();
                    else
                        mediaPlayerLose.start();
                }
                if (current_game.getLife() == 0) {
                    flagLoose = true;
                    flagEndGame = true;
                }

                scoreMultiplier = 1;
                start_flag = false;
                fire_paddle = false;
                fire_ball = false;
                glue=false;
                paddle.setWidth(PADDLE_WIDTH);
                bonuss.clear();
                fireJet.clear();
                balls.clear();
                Ball bal = new Ball(ScreenTools.getScreenWidth(context) / 2,bottomLimit - BALL_RADIUS - 100,BALL_SPEED,BONUS_SPEED,BALL_RADIUS);
                bal.setRebound(0);
                bal.setSpeed(BALL_SPEED);
                balls.add(bal);
            }

            List<Ball> copy_fire = new ArrayList<>();
            for(Ball fire : fireJet)
            {
                for (Brick brick : bricks) {
                    if (!brick.getIsBroken()) {
                        if (fire.getX() + FIRE_RADIUS > brick.getX() && fire.getX() - FIRE_RADIUS < brick.getX() + brick.getWidth() &&
                                fire.getY() + FIRE_RADIUS > brick.getY() && fire.getY() - FIRE_RADIUS < brick.getY() + brick.getHeight())
                        {
                                updateBrickFire(brick, fire);
                                copy_fire.add(fire);
                        }
                    }
                }
            }
            if(!copy_fire.isEmpty()&&copy_fire!=null)
                fireJet.removeAll(copy_fire);
        }
        else{
            if (!tvScore.getText().equals(String.valueOf(current_game.getScore())))
                tvScore.setText(String.valueOf(current_game.getScore()));
            if (!tvGameInfo.getText().equals("Level " + String.valueOf(current_game.getCurrent_level()) + " - Life : " + String.valueOf(current_game.getLife())))
                tvGameInfo.setText("Level " + String.valueOf(current_game.getCurrent_level()) + " - Life : " + String.valueOf(current_game.getLife()));
            if (current_game.getScore() > current_game.getP().getHighScore()) {
                current_game.getP().setHighScore(current_game.getScore());
                tvHighscore.setText(String.valueOf(current_game.getP().getHighScore()));
            }
            canvas.drawColor(Color.BLACK);
            paint.setStrokeWidth(10);
            paint.setStyle(Paint.Style.STROKE); // Style pour le contour de la flèche
            Path path = new Path();
            createArrowPath(path);
            blinkingText++;
            if(blinkingText<50) {
                if(flagLoose){
                    paint.setColor(Color.RED);
                    paintText.setColor(Color.RED);
                }
                if(flagWin){
                    paint.setColor(Color.GREEN);
                    paintText.setColor(Color.GREEN);
                }
            }
            else{
                paint.setColor(Color.BLUE);
                paintText.setColor(Color.BLUE);
            }

            if(blinkingText>100)
                blinkingText=0;

            canvas.drawPath(path, paint);

            paintText.setTextSize(48);
            if(flagWin)
                canvas.drawText("Swipe up to change level", ScreenTools.getScreenWidth(context)/2-250, ScreenTools.getScreenHeight(context)/2 + 50, paintText);
            if(flagLoose)
                canvas.drawText("Swipe up to restart level", ScreenTools.getScreenWidth(context)/2-250, ScreenTools.getScreenHeight(context)/2 + 50, paintText);

            paintText.setTextSize(100);
            if(flagWin)
                canvas.drawText("You Win", ScreenTools.getScreenWidth(context)/2-200, ScreenTools.getScreenHeight(context)/2 + 150, paintText);
            if(flagLoose)
                canvas.drawText("Game Over", ScreenTools.getScreenWidth(context)/2-250, ScreenTools.getScreenHeight(context)/2 + 150, paintText);
        }

        invalidate();

        if(flagEndGame)
        {
            if(flagWin){
                nextLevel();
            }
            else if(flagLoose){
                restartGame();
            }
        }

    }

    private void nextLevel() {
        current_game.setCurrent_level(current_game.getCurrent_level()+1);
        if(current_game.getCurrent_level()>current_game.getP().getLevel()) {
            current_game.getP().setLevel(current_game.getCurrent_level());
            db.modifyProfil(current_game.getP());
        }
        fireJet.clear();
        bonuss.clear();
        balls.clear();
        flagEndGame = false;
    }

    private boolean get_runningThread(){
        return runningThread.value;
    }

    private void restartGame() {
        db.modifyProfil(current_game.getP());
        current_game.setScore(0);
        current_game.setLife(5);
        fireJet.clear();
        bonuss.clear();
        balls.clear();
        flagEndGame = false;
    }

    private void createArrowPath(Path path) {
        // Définir les points de la flèche
        float middleX = ScreenTools.getScreenWidth(context)/2;
        float middleY = ScreenTools.getScreenHeight(context)/2;

        path.moveTo(middleX, middleY);
        path.lineTo(middleX, middleY-250);
        path.lineTo(middleX-100, middleY-150);
        path.moveTo(middleX, middleY-250);
        path.lineTo(middleX+100, middleY-150);
    }

    private void updateBrick(Brick brick,Ball ball){
        notDoubleRebound=true;
        brick.setIsBroken(true);
        if(brick.getBonus()!=Brick.NO_BONUS)
        {
            Bonus bonus = new Bonus(brick.getX()+brick.getWidth()/2,brick.getY()-BONUS_RADIUS,0,BONUS_SPEED,BONUS_RADIUS,brick.getBonus());
            bonuss.add(bonus);
        }
        if (current_game.getSound_effect()) {
            if(brick.getColor()==Color.DKGRAY||brick.getColor()==Color.GRAY||brick.getColor()==Color.LTGRAY) {
                if (mediaPlayerStone.isPlaying()) {
                    mediaPlayerStone.pause();
                    mediaPlayerStone.seekTo(0);
                    mediaPlayerStone.start();
                } else {
                    mediaPlayerStone.start();
                }
            }
            else if (brick.getColor()==Color.WHITE)
            {
                if (mediaPlayerBoing.isPlaying()) {
                    mediaPlayerBoing.pause();
                    mediaPlayerBoing.seekTo(0);
                    mediaPlayerBoing.start();
                } else {
                    mediaPlayerBoing.start();
                }
            } else
            {
                if (mediaPlayerBrick.isPlaying()) {
                    mediaPlayerBrick.pause();
                    mediaPlayerBrick.seekTo(0);
                    mediaPlayerBrick.start();
                } else {
                    mediaPlayerBrick.start();
                }
            }

        }
        ball.setRebound(ball.getRebound() + 1);

        if (ball.getRebound() > 30)
        {
            ball.setRebound(0);
            if (ball.getDy() < 0) {
                if (ball.getDy() > ball.getSpeed()) ball.setDy(ball.getDy() - 1);
                else ball.setDy(ball.getDy() - 2);
            } else {
                if (ball.getDy() < ball.getSpeed()) ball.setDy(ball.getDy() + 1);
                else ball.setDy(ball.getDy() + 2);
            }

            if (ball.getDx() < 0) {
                ball.setDx(ball.getDx()-2);
            } else {
                ball.setDx(ball.getDx()+2);
            }

            ball.setSpeed(ball.getSpeed() - 2);
        }
        current_game.setScore(current_game.getScore() + scoreMultiplier);
        scoreMultiplier++;

    }

    private void updateBrickFire(Brick brick,Ball fire){
        if(brick.getColor()!=Color.WHITE) brick.setIsBroken(true);
        if(brick.getBonus()!=Brick.NO_BONUS)
        {
            Bonus bonus = new Bonus(brick.getX()-BONUS_RADIUS,brick.getY()-BONUS_RADIUS,0,BONUS_SPEED,BONUS_RADIUS,brick.getBonus());
            bonuss.add(bonus);
        }
        if (current_game.getSound_effect()) {
            if (mediaPlayerBrick.isPlaying()) {
                mediaPlayerBrick.pause();
                mediaPlayerBrick.seekTo(0);
                mediaPlayerBrick.start();
            } else {
                mediaPlayerBrick.start();
            }
        }
    }


    private void initialiseScreenGame(){

        flagLoose = false;
        flagWin = false;
        fire_paddle = false;
        fire_ball = false;
        glue=false;
        bottomLimit = ScreenTools.getScreenHeight(context) - BOTTOM_LIMIT;

        // Initialisation de la raquette
        paddle = new Paddle((ScreenTools.getScreenWidth(context)-paddle_width)/2, bottomLimit-100, paddle_width, PADDLE_HEIGHT);
        paddle.setWidth(PADDLE_WIDTH);

        // Initialisation de la balle
        if(balls.size()!=0) balls.clear();
        Ball ball = new Ball(paddle.getX()+(paddle_width/2), paddle.getY()-BALL_RADIUS-1, -BALL_SPEED, BALL_SPEED, BALL_RADIUS);
        ball.setSpeed(BALL_SPEED);
        ball.setRebound(0);
        balls.add(ball);

        // Initialisation des briques
        bricks = new ArrayList<>();
        int brickWidth;
        int brickHeight=BRICK_HEIGHT;
        int startY = BRICK_START_Y;
        int nbCol = NB_COLUMNS;
        int startX = BRICK_START_X;

        brickWidth = (ScreenTools.getScreenWidth(context)-2*startX)/20;
        startX = (ScreenTools.getScreenWidth(context)-brickWidth*20)/2;
        
        scoreMultiplier=1;
        blinkingText=0;


        Random random = new Random();
        Level level = levels.levels.get(current_game.getCurrent_level()-1);
        nb_row = level.nb_ligne;
        int bonus;

        for(int row = 0; row < level.nb_ligne; row++){
            for(int col = 0; col<levels.NB_COLUMNS;col++){
                if(random.nextInt(10)==5)
                    bonus = random.nextInt(Brick.NB_BONUS+1);
                else
                    bonus = 0;
                if(level.lines.get(row)[col]=='R')
                {
                    bricks.add(new Brick(startX + col * (brickWidth), startY + row * (brickHeight), brickWidth, brickHeight,COLOR[random.nextInt(NB_COLOR)],bonus));
                }
                else if(level.lines.get(row)[col]=='W')
                {
                    bricks.add(new Brick(startX + col * (brickWidth), startY + row * (brickHeight), brickWidth, brickHeight,Color.WHITE,Brick.NO_BONUS));
                }
                else if(level.lines.get(row)[col]=='G')
                {
                    bricks.add(new Brick(startX + col * (brickWidth), startY + row * (brickHeight), brickWidth, brickHeight,Color.LTGRAY,Brick.NO_BONUS));
                }
                else
                {
                    Brick brick = new Brick(startX + col * (brickWidth), startY + row * (brickHeight), brickWidth, brickHeight,COLOR[random.nextInt(NB_COLOR)],Brick.NO_BONUS);
                    brick.setIsBroken(true);
                    bricks.add(brick);
                }
            }
        }

        paint = new Paint();
        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float ptX = event.getX() - paddle.getWidth() / 2;
        float ptY = event.getY() - 150 - (paddle.getHeight() / 2);
        float paddleX;
        float paddleY;

        int action = event.getAction() & MotionEvent.ACTION_MASK;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if(paddle.getX()>ptX){
                    if(paddle.getX()-ptX<PADDLE_SPEED)
                        paddleX=ptX;
                    else
                        paddleX=paddle.getX()-PADDLE_SPEED;
                }
                else{
                    if(ptX-paddle.getX()<PADDLE_SPEED)
                        paddleX=ptX;
                    else
                        paddleX=paddle.getX()+PADDLE_SPEED;
                }
                if(paddle.getY()>ptY){
                    if(paddle.getY()-ptY<PADDLE_SPEED)
                        paddleY=ptY;
                    else
                        paddleY=paddle.getY()-PADDLE_SPEED;
                }
                else{
                    if(ptY-paddle.getY()<PADDLE_SPEED)

                        paddleY=ptY;
                    else
                        paddleY=paddle.getY()+PADDLE_SPEED;
                }
                if (paddleY>bottomLimit-PADDLE_HEIGHT)
                    paddleY=bottomLimit-PADDLE_HEIGHT;

                if(paddleY<(nb_row+1)*(BRICK_HEIGHT+1)+BRICK_START_Y)
                    paddleY=(nb_row+1)*(BRICK_HEIGHT+1)+BRICK_START_Y;

                if(paddleX>ScreenTools.getScreenWidth(context)-paddle.getWidth())
                    paddleX=ScreenTools.getScreenWidth(context)-paddle.getWidth();
                if(paddleX<0)
                    paddleX=0;

                paddle.setX(paddleX);
                paddle.setY(paddleY);
                for(Ball ball : balls){
                    if(ball.getBall_stop())
                    {
                        ball.setX(paddle.getX()+ball.getOffset());
                        ball.setY(paddle.getY()-BALL_RADIUS);
                    }
                }

                break;
            case MotionEvent.ACTION_POINTER_DOWN :
            {
                for(Ball ball : balls) ball.setBall_stop(false);
                if(fire_paddle) {
                    Ball fire = new Ball(paddle.getX() + (paddle.getWidth() / 2), paddle.getY() - FIRE_RADIUS - 1, 0, FIRE_SPEED, FIRE_RADIUS);
                    fireJet.add(fire);
                }
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


