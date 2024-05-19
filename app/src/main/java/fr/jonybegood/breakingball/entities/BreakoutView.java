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

import fr.jonybegood.breakingball.database.DbHelper;
import fr.jonybegood.breakingball.entities.Ball;
import fr.jonybegood.breakingball.entities.Paddle;
import fr.jonybegood.breakingball.entities.Brick;

import fr.jonybegood.breakball.tools.ScreenTools;
import fr.jonybegood.breakingball.R;
import fr.jonybegood.breakingball.tools.BooleanWrapper;
import fr.jonybegood.breakingball.tools.GestureTools;

public class BreakoutView extends View {
    private Ball ball;
    private Paddle paddle;
    private List<Brick> bricks;
    private Paint paint, paintText;

    private boolean start_flag=false;

    private static final int BALL_RADIUS = 10;
    private static final int BALL_SPEED = -6;
    private static final int PADDLE_SPEED = 20;
    private static final int PADDLE_HEIGHT = 20;
    private static final int PADDLE_WIDTH = 100;
    private static final int BRICK_HEIGHT = 30;
    private static final int BRICK_WIDTH = 50;

    private static int BRICK_START_X = 5;
    private static final int BRICK_START_Y = 20;

    private static final int NB_ROW = 8;

    private static final int BOTTOM_LIMIT = 400;

    private static int bottomLimit;

    private int ballSpeed;

    private static final int COLOR[] = {Color.GREEN,Color.RED,Color.BLUE,Color.CYAN,Color.MAGENTA,Color.YELLOW};
    private static final int NB_COLOR = 6;

    private static Context context;

    private DbHelper db;

    private BooleanWrapper runningThread;

    private Thread checCollisionThread;

    private MediaPlayer mediaPlayerBrick, mediaPlayerWin, mediaPlayerLose, mediaPlayerGameOver;

    private boolean flagWin,flagLoose,flagEndGame;
    private Game current_game;

    private TextView tvGameInfo, tvScore,tvHighscore;

    private int scoreMultiplier;

    private int ballRebound;

    private int blinkingText;

    private GestureDetector gestureDetector;

    public BreakoutView(Context context,Game game, TextView tvGameInfo, TextView tvScore, TextView tvHighscore, BooleanWrapper runningThread) {
        super(context);
        this.context=context;
        this.tvGameInfo = tvGameInfo;
        this.tvScore =tvScore;
        this.tvHighscore=tvHighscore;
        this.runningThread=runningThread;
        current_game = game;
        db = new DbHelper(context);

        GestureTools gestureTools = new GestureTools(){
            @Override
            public void onSwipeUp(){
                if(flagWin||flagLoose)
                    initialiseScreenGame();
            }

        };

        gestureDetector = new GestureDetector(context, gestureTools);

        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        initialiseScreenGame();
        BooleanWrapper finalRunningThread = runningThread;
        checCollisionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(finalRunningThread.value){
                    // Gestion des collisions avec la raquette
                    if ((ball.getY()+BALL_RADIUS) > paddle.getY() && (ball.getY()+BALL_RADIUS) < paddle.getY() + paddle.getHeight() &&
                            ball.getX() > paddle.getX() && ball.getX() < paddle.getX() + paddle.getWidth()) {
                        if(start_flag==false) start_flag=true;
                        else {
                            ball.setDy(ballSpeed);
                            ballRebound++;
                        }
                        if(ball.getX() > paddle.getX() && ball.getX() < paddle.getX() + paddle.getWidth()/2)
                            ball.setDx(ballSpeed);
                        else
                            ball.setDx(-ballSpeed);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            Toast.makeText(context, "Thread problem", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        checCollisionThread.start();


        mediaPlayerBrick = MediaPlayer.create(this.getContext(), R.raw.glass_hit);
        mediaPlayerWin = MediaPlayer.create(this.getContext(), R.raw.success);
        mediaPlayerLose= MediaPlayer.create(this.getContext(), R.raw.lose);
        mediaPlayerGameOver= MediaPlayer.create(this.getContext(), R.raw.game_over);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boolean end_flag;

        if (!(flagWin||flagLoose)) {


            if (!tvScore.getText().equals(String.valueOf(current_game.getScore())))
                tvScore.setText(String.valueOf(current_game.getScore()));
            if (!tvGameInfo.getText().equals("Level " + String.valueOf(current_game.getP().getLevel()) + " - Life : " + String.valueOf(current_game.getLife())))
                tvGameInfo.setText("Level " + String.valueOf(current_game.getP().getLevel()) + " - Life : " + String.valueOf(current_game.getLife()));
            if (current_game.getScore() > current_game.getP().getHighScore()) {
                current_game.getP().setHighScore(current_game.getScore());
                tvHighscore.setText(String.valueOf(current_game.getP().getHighScore()));
            }

            if (ballRebound > 20) {
                ballRebound = 0;
                if (ballSpeed > 0) ballSpeed += 2;
                else ballSpeed -= 2;
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
            paint.setColor(Color.WHITE);
            canvas.drawCircle(ball.getX(), ball.getY(), ball.getRadius(), paint);

            // Dessiner la raquette
            paint.setColor(Color.LTGRAY);
            canvas.drawRect(paddle.getX(), paddle.getY(), paddle.getX() + paddle.getWidth(), paddle.getY() + paddle.getHeight(), paint);

            // Dessiner les briques
            for (Brick brick : bricks) {
                if (!brick.getIsBroken()) {
                    paint.setColor(brick.getColor());
                    canvas.drawRect(brick.getX(), brick.getY(), brick.getX() + brick.getWidth(), brick.getY() + brick.getHeight(), paint);
                }
            }

            // Déplacer la balle
            if (start_flag == true) {
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
            for (Brick brick : bricks) {
                if (!brick.getIsBroken()) {
                    end_flag = false;
                    switch (dir) {
                        case "UR":
                            if (ball.getX() + BALL_RADIUS > brick.getX() && ball.getX() + BALL_RADIUS < brick.getX() + brick.getWidth() &&
                                    ball.getY() - BALL_RADIUS > brick.getY() && ball.getY() - BALL_RADIUS < brick.getY() + brick.getHeight()) {
                                //if ((ball.getY() - ball.getDy() > brick.getY() + brick.getHeight()) || (ball.getY() - ball.getDy() < brick.getY()))
                                if(ball.getX()-brick.getX()>brick.getY()+brick.getHeight()-ball.getY())
                                    ball.setDy(-ball.getDy());
                                //if ((ball.getX() - ball.getDx() < brick.getX()) || (ball.getX() - ball.getDx() > brick.getX() + brick.getWidth()))
                                else
                                    ball.setDx(-ball.getDx());
                                brick.setIsBroken(true);
                                if (current_game.getSound_effect()) {
                                    if (mediaPlayerBrick.isPlaying()) {
                                        mediaPlayerBrick.pause();
                                        mediaPlayerBrick.seekTo(0);
                                        mediaPlayerBrick.start();
                                    } else {
                                        mediaPlayerBrick.start();
                                    }
                                }
                                ballRebound++;
                                current_game.setScore(current_game.getScore() + scoreMultiplier);
                                if (scoreMultiplier <= 50)
                                    scoreMultiplier++;
                            }
                            break;
                        case "UL":
                            if (ball.getX() - BALL_RADIUS > brick.getX() && ball.getX() - BALL_RADIUS < brick.getX() + brick.getWidth() &&
                                    ball.getY() - BALL_RADIUS > brick.getY() && ball.getY() - BALL_RADIUS < brick.getY() + brick.getHeight()) {
                                //if ((ball.getY() - ball.getDy() > brick.getY() + brick.getHeight()) || (ball.getY() - ball.getDy() < brick.getY()))
                                if(brick.getX()+brick.getWidth()-ball.getX()>brick.getY()+brick.getHeight()-ball.getY())
                                    ball.setDy(-ball.getDy());
                                //if ((ball.getX() - ball.getDx() < brick.getX()) || (ball.getX() - ball.getDx() > brick.getX() + brick.getWidth()))
                                else
                                    ball.setDx(-ball.getDx());
                                brick.setIsBroken(true);
                                if (current_game.getSound_effect()) {
                                    if (mediaPlayerBrick.isPlaying()) {
                                        mediaPlayerBrick.pause();
                                        mediaPlayerBrick.seekTo(0);
                                        mediaPlayerBrick.start();
                                    } else {
                                        mediaPlayerBrick.start();
                                    }
                                }
                                ballRebound++;
                                current_game.setScore(current_game.getScore() + scoreMultiplier);
                                if (scoreMultiplier <= 50)
                                    scoreMultiplier++;
                            }
                            break;
                        case "DR":
                            if (ball.getX() + BALL_RADIUS > brick.getX() && ball.getX() + BALL_RADIUS < brick.getX() + brick.getWidth() &&
                                    ball.getY() + BALL_RADIUS > brick.getY() && ball.getY() + BALL_RADIUS < brick.getY() + brick.getHeight()) {
                                //if ((ball.getY() - ball.getDy() > brick.getY() + brick.getHeight()) || (ball.getY() - ball.getDy() < brick.getY()))
                                if(ball.getX()-brick.getX()> ball.getY()-brick.getY())
                                    ball.setDy(-ball.getDy());
                                //if ((ball.getX() - ball.getDx() < brick.getX()) || (ball.getX() - ball.getDx() > brick.getX() + brick.getWidth()))
                                else
                                    ball.setDx(-ball.getDx());
                                brick.setIsBroken(true);
                                if (current_game.getSound_effect()) {
                                    if (mediaPlayerBrick.isPlaying()) {
                                        mediaPlayerBrick.pause();
                                        mediaPlayerBrick.seekTo(0);
                                        mediaPlayerBrick.start();
                                    } else {
                                        mediaPlayerBrick.start();
                                    }
                                }
                                ballRebound++;
                                current_game.setScore(current_game.getScore() + scoreMultiplier);
                                if (scoreMultiplier <= 50)
                                    scoreMultiplier++;
                            }
                            break;
                        case "DL":
                            if (ball.getX() - BALL_RADIUS > brick.getX() && ball.getX() - BALL_RADIUS < brick.getX() + brick.getWidth() &&
                                    ball.getY() + BALL_RADIUS > brick.getY() && ball.getY() + BALL_RADIUS < brick.getY() + brick.getHeight()) {
                                //if ((ball.getY() - ball.getDy() > brick.getY() + brick.getHeight()) || (ball.getY() - ball.getDy() < brick.getY()))
                                if(brick.getX()+brick.getWidth()-ball.getX()>ball.getY()-brick.getY())
                                    ball.setDy(-ball.getDy());
                                //if ((ball.getX() - ball.getDx() < brick.getX()) || (ball.getX() - ball.getDx() > brick.getX() + brick.getWidth()))
                                else
                                    ball.setDx(-ball.getDx());
                                brick.setIsBroken(true);
                                if (current_game.getSound_effect()) {
                                    if (mediaPlayerBrick.isPlaying()) {
                                        mediaPlayerBrick.pause();
                                        mediaPlayerBrick.seekTo(0);
                                        mediaPlayerBrick.start();
                                    } else {
                                        mediaPlayerBrick.start();
                                    }
                                }
                                ballRebound++;
                                current_game.setScore(current_game.getScore() + scoreMultiplier);
                                if (scoreMultiplier <= 50)
                                    scoreMultiplier++;
                            }
                            break;
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

            //gestion de la défaite
            if (ball.getY() > bottomLimit) {
                current_game.setLife(current_game.getLife() - 1);
                if (current_game.getSound_effect()) {
                    if (current_game.getLife() == 0)
                        mediaPlayerGameOver.start();
                    else
                        mediaPlayerLose.start();
                }
                if (current_game.getLife() == 0){
                    flagLoose = true;
                    flagEndGame = true;
                }

                scoreMultiplier = 1;
                ballRebound = 0;
                ballSpeed = BALL_SPEED;
                start_flag = false;
                ball.setX(ScreenTools.getScreenWidth(context) / 2);
                ball.setY(bottomLimit - BALL_RADIUS - 100);
                ball.setDy(ballSpeed);
            }
        }
        else{
            if (!tvScore.getText().equals(String.valueOf(current_game.getScore())))
                tvScore.setText(String.valueOf(current_game.getScore()));
            if (!tvGameInfo.getText().equals("Level " + String.valueOf(current_game.getP().getLevel()) + " - Life : " + String.valueOf(current_game.getLife())))
                tvGameInfo.setText("Level " + String.valueOf(current_game.getP().getLevel()) + " - Life : " + String.valueOf(current_game.getLife()));
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
                canvas.drawText("You Win", ScreenTools.getScreenWidth(context)/2-250, ScreenTools.getScreenHeight(context)/2 + 150, paintText);
            if(flagLoose)
                canvas.drawText("Game Over", ScreenTools.getScreenWidth(context)/2-250, ScreenTools.getScreenHeight(context)/2 + 150, paintText);
        }

        invalidate();

        if(flagEndGame)
        {
            if(flagWin){
                runningThread.value=false;
                nextLevel();
            }
            else if(flagLoose){
                restartGame();
                runningThread.value=false;
            }
        }

    }

    private void nextLevel() {
        current_game.getP().setLevel(current_game.getP().getLevel()+1);
        db.modifyProfil(current_game.getP());
        flagEndGame = false;
    }

    private void restartGame() {
        db.modifyProfil(current_game.getP());
        current_game.setScore(0);
        current_game.setLife(5);
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

    private void initialiseScreenGame(){
        runningThread = new BooleanWrapper(true);
        ballSpeed = BALL_SPEED;
        flagLoose = false;
        flagWin = false;
        bottomLimit = ScreenTools.getScreenHeight(context) - BOTTOM_LIMIT;

        // Initialisation de la raquette
        paddle = new Paddle((ScreenTools.getScreenWidth(context)-PADDLE_WIDTH)/2, bottomLimit-100, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Initialisation de la balle
        ball = new Ball(paddle.getX()+(PADDLE_WIDTH/2), paddle.getY()-BALL_RADIUS-1, -ballSpeed, ballSpeed, BALL_RADIUS);

        // Initialisation des briques
        bricks = new ArrayList<>();
        int brickWidth = BRICK_WIDTH;
        int brickHeight = BRICK_HEIGHT;
        int startY = BRICK_START_Y;
        int nbRow = NB_ROW;
        int nbCol = (ScreenTools.getScreenWidth(context)-BRICK_START_X*2)/(brickWidth+1);
        int startX = (ScreenTools.getScreenWidth(context)-(nbCol*(brickWidth+1)))/2;
        scoreMultiplier=1;
        ballRebound=0;
        blinkingText=0;


        Random random = new Random();
        // Créer plusieurs lignes de briques
        for (int row = 0; row < nbRow; row++) {
            for (int col = 0; col < nbCol; col++) {
                bricks.add(new Brick(startX + col * (brickWidth + 1), startY + row * (brickHeight + 1), brickWidth, brickHeight,COLOR[random.nextInt(NB_COLOR-1)]));
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

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
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

                if(paddleY<NB_ROW*(BRICK_HEIGHT+1)+BRICK_START_Y)
                    paddleY=NB_ROW*(BRICK_HEIGHT+1)+BRICK_START_Y;

                if(paddleX>ScreenTools.getScreenWidth(context)-paddle.getWidth())
                    paddleX=ScreenTools.getScreenWidth(context)-paddle.getWidth();
                if(paddleX<0)
                    paddleX=0;

                paddle.setX(paddleX);
                paddle.setY(paddleY);


                /* Déplacer la raquette avec le toucher
                paddle.setX(event.getX() - paddle.getWidth() / 2);
                paddle.setY(event.getY() - 150 - (paddle.getHeight() / 2));*/
                break;
        }
        return true;
    }
}


