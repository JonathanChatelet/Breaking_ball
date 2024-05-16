package fr.jonybegood.breakingball;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import fr.jonybegood.breakingball.entities.Game;

public class SettingsActivity extends AppCompatActivity {

    private Switch swMusic,swEffects;

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        swMusic = findViewById(R.id.sw_music);
        swEffects = findViewById(R.id.sw_effects);

        game =new Game();

        game = (Game) getIntent().getExtras().getSerializable("game");

        if(game.getSound_effect()){
            swEffects.setChecked(true);
        }
        else{
            swEffects.setChecked(false);
        }
        if(game.getMusic()){
            swMusic.setChecked(true);
        }
        else{
            swMusic.setChecked(false);
        }

        swMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    game.setMusic(true);
                }
                else{
                    game.setMusic(false);
                }
            }
        });

        swEffects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    game.setSound_effect(true);
                }
                else{
                    game.setSound_effect(false);
                }
            }
        });

    }

    public void btnBackSettings(View view) {

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}