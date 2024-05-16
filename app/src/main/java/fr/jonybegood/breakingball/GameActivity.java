package fr.jonybegood.breakingball;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.Serializable;

import fr.jonybegood.breakingball.entities.BreakoutView;
import fr.jonybegood.breakingball.entities.Game;

public class GameActivity extends AppCompatActivity {

    private Game game;
    private MediaPlayer mediaPlayer;

    private boolean runningThread;

    TextView tvGameInfo,tvScore,tvHighscore,tvPseudo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvGameInfo = findViewById(R.id.tv_game_info);
        tvScore = findViewById(R.id.tv_current_score);
        tvPseudo = findViewById(R.id.tv_current_pseudo);
        tvHighscore = findViewById(R.id.tv_highscore);
        game = (Game) getIntent().getExtras().getSerializable("game");
        tvHighscore.setText(String.valueOf(game.getP().getHighScore()));
        tvScore.setText(String.valueOf(game.getScore()));
        tvPseudo.setText(game.getP().getPseudo()+" highscore :");
        tvGameInfo.setText("Level "+String.valueOf(game.getP().getLevel()));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentLayout, new GameFragment(game,tvGameInfo,tvScore,tvHighscore,runningThread))
                .commit();
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        if(game.getMusic()){
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
    }

    public void btnQuitGame(View view) {
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
        Intent resultIntent = new Intent();
        Bundle b = new Bundle();
        b.putSerializable("game", game);
        resultIntent.putExtras(b);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
        runningThread = false;
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        runningThread = false;
    }

}