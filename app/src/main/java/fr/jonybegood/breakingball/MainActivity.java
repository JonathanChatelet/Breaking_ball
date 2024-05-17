package fr.jonybegood.breakingball;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.jonybegood.breakingball.database.DbHelper;
import fr.jonybegood.breakingball.entities.Game;
import fr.jonybegood.breakingball.entities.Profil;

public class MainActivity extends AppCompatActivity {

    private Game game;

    private ImageView ivPlayer;

    private TextView tvPlayerInfo;

    private Button btnLoad;

    private ActivityResultLauncher<Intent> activityLauncher_SL,activityLauncher_Setting,activityLauncher_Game;

    private static DbHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnLoad = findViewById(R.id.btnLoad);

        game = new Game();
        db = new DbHelper(this);

        activityLauncher_SL = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Bundle b = new Bundle();
                        b = data.getExtras();
                        game = (Game) b.getSerializable("game");
                        tvPlayerInfo.setText(game.getP().getPseudo() + " / level : " + game.getP().getLevel() + " / highscore : " + game.getP().getHighScore());
                        if (game.getP().getPhoto() != "") {
                            File fichierImage = new File(MainActivity.this.getFilesDir(), game.getP().getPhoto());
                            if (fichierImage.exists())
                            {
                                Bitmap imageBitmap = BitmapFactory.decodeFile(fichierImage.getAbsolutePath());
                                ivPlayer.setImageBitmap(imageBitmap);
                            }
                            else Toast.makeText(this, "image non trouvée", Toast.LENGTH_LONG).show();
                        }
                        if (db.getProfilsCount()==0){
                            btnLoad.setClickable(false);
                            btnLoad.setTextColor(Color.GRAY);
                            btnLoad.setAlpha(0.5f);
                        }
                        else{
                            btnLoad.setClickable(true);
                            btnLoad.setTextColor(Color.WHITE);
                            btnLoad.setAlpha(1f);
                        }
                    }
                    if (result.getResultCode() == RESULT_CANCELED){
                        if (db.getProfilsCount()==0){
                            btnLoad.setClickable(false);
                            btnLoad.setTextColor(Color.GRAY);
                            btnLoad.setAlpha(0.5f);
                        }
                        else{
                            btnLoad.setClickable(true);
                            btnLoad.setTextColor(Color.WHITE);
                            btnLoad.setAlpha(1f);
                        }
                    }

                }
        );

        activityLauncher_Setting = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Bundle b = new Bundle();
                        b = data.getExtras();
                        Game copy_settings = new Game();
                        copy_settings = (Game) b.getSerializable("game");
                        game.setMusic(copy_settings.getMusic());
                        game.setSound_effect(copy_settings.getSound_effect());
                    }
                }
        );

        activityLauncher_Game = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Bundle b = new Bundle();
                        b = data.getExtras();
                        game = (Game) b.getSerializable("game");
                        tvPlayerInfo.setText(game.getP().getPseudo() + " / level : " + game.getP().getLevel() + " / highscore : " + game.getP().getHighScore());

                    }
                }
        );

        ivPlayer = findViewById(R.id.iv_player);
        tvPlayerInfo = findViewById(R.id.tv_player_info);

        if (db.getProfilsCount()==0){
            btnLoad.setClickable(false);
            btnLoad.setTextColor(Color.GRAY);
            btnLoad.setAlpha(0.5f);
        }
        else{
            btnLoad.setClickable(true);
            btnLoad.setTextColor(Color.WHITE);
            btnLoad.setAlpha(1f);
        }

    }

    public void btnLoadProfil(View view) {
        Intent intent = new Intent(this, LoadProfilActivity.class);
        activityLauncher_SL.launch(intent);
    }

    public void btnNewProfil(View view) {
        Intent intent = new Intent(this, NewProfilActivity.class);
        activityLauncher_SL.launch(intent);
    }

    public void btnStartGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("game", (Serializable) game);
        intent.putExtras(b);
        activityLauncher_Game.launch(intent);
    }

    public void btnSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("game", (Serializable) game);
        intent.putExtras(b);
        activityLauncher_Setting.launch(intent);
    }

    public void btnQuitter(View view) {
        System.exit(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

}