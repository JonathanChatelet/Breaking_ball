package fr.jonybegood.breakingball;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.jonybegood.breakingball.entities.Game;

public class BrekingBallRulesActivity extends AppCompatActivity {

    private Game game;
    private Spinner spLevel;
    private ArrayAdapter<String> adapter;
    private List<String> levels = new ArrayList<>();
    private ActivityResultLauncher<Intent> activityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_breking_ball_rules);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        spLevel = findViewById(R.id.sp_level);
        game = (Game) getIntent().getExtras().getSerializable("game");

        for(int i=0;i<game.getP().getLevel();i++)
            levels.add("Level "+(i+1));

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, levels);
        spLevel.setAdapter(adapter);

        spLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                game.setCurrent_level(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Le spinner ne contient d'élément vide
            }
        });

        activityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        setResult(RESULT_OK, data);
                        finish();
                    }
                }
        );
    }

    public void btnPlayGame(View view)
    {
        Intent intent = new Intent(this, GameActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("game", (Serializable) game);
        intent.putExtras(b);
        activityLauncher.launch(intent);
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
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}