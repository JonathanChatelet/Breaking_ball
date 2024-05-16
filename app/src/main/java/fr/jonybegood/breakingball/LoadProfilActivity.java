package fr.jonybegood.breakingball;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import fr.jonybegood.breakingball.database.DbHelper;
import fr.jonybegood.breakingball.entities.Game;
import fr.jonybegood.breakingball.entities.Profil;

public class LoadProfilActivity extends AppCompatActivity {

    private DbHelper db;

    private Game game;

    private Profil profil;

    private Spinner spPseudo;

    private TextView tvLoadProfil;

    private ArrayAdapter<String> adapter;

    private List<String> pseudos;

    private List<Profil> profils;

    private EditText edPasswordLd;

    private String selectedPseudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_load_profil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edPasswordLd = findViewById(R.id.ed_password_ld);
        spPseudo = findViewById(R.id.sp_pseudo);
        tvLoadProfil = findViewById(R.id.tv_load_profil);

        profils = new ArrayList<>();
        pseudos = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pseudos);
        spPseudo.setAdapter(adapter);

        db = new DbHelper(this);
        game = new Game();
        profil = new Profil();

        profils = db.getAllProfils();

        for (int i=0; i<profils.size();i++)
        {
            pseudos.add(profils.get(i).getPseudo());
        }
        adapter.notifyDataSetChanged();

        spPseudo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvLoadProfil.setTextColor(Color.GREEN);
                tvLoadProfil.setText("Enter your password :");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Le spinner ne contient d'élément vide
            }
        });

    }

    public void btnLoadProfil(View view) {
        Profil profil = db.getByPseudo(spPseudo.getSelectedItem().toString());
        if(profil.getPassword().equals(edPasswordLd.getText().toString())){
            Game game = new Game();
            game.setP(profil);
            Intent resultIntent = new Intent();
            Bundle b = new Bundle();
            b.putSerializable("game", game);
            resultIntent.putExtras(b);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
        else{
            tvLoadProfil.setTextColor(Color.RED);
            tvLoadProfil.setText("Enter your password : Wrong password");
        }

    }

    public void btnCancelLoad(View view) {
        Intent resultIntent = new Intent();
        setResult(RESULT_CANCELED, resultIntent);
        finish();
    }

    public void btnClearLdPassword(View view) {

        edPasswordLd.setText("");
    }

    public void btnClearProfil(View view) {
        //Afficher une boite de dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(LoadProfilActivity.this);
        builder.setTitle("Suppression")
                .setMessage("Etes-vous sûr de vouloir supprimer le profil?")
                .setPositiveButton("oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        adapter.remove(spPseudo.getSelectedItem().toString());

                        if(db.getProfilsCount()>1) {
                            db.removeByPseudo(spPseudo.getSelectedItem().toString());
                            adapter.notifyDataSetChanged();
                        }
                        else{
                            db.clearTable();
                            Intent resultIntent = new Intent();
                            setResult(RESULT_CANCELED, resultIntent);
                            finish();
                        }

                    }
                })
                .setNegativeButton("non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        builder.create().show();
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