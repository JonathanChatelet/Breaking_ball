package fr.jonybegood.breakingball;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

public class NewProfilActivity extends AppCompatActivity {

    private Game game;

    private String url;

    private final int CAMERA_PERMISSION_CODE = 200;
    private final int STORAGE_PERMISSION_CODE = 300;

    private EditText etPassword1,etPassword2,etPseudo;
    private TextView tvNewPseudo, tvNewPw1, tvNewPw2;

    private ActivityResultLauncher<Intent> selectImageLauncher;

    private static final int REQUEST_IMAGE_CAPTURE=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_profil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        game = new Game();
        DbHelper db =new DbHelper(this);

        etPassword1 = findViewById(R.id.et_password1);
        etPassword2 = findViewById(R.id.et_password2);
        etPseudo = findViewById(R.id.et_pseudo);
        tvNewPseudo = findViewById(R.id.tv_new_Pseudo);
        tvNewPw1 = findViewById(R.id.tv_new_pw1);
        tvNewPw2 = findViewById(R.id.tv_new_pw2);

        etPseudo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etPseudo.getText().toString().matches("[a-zA-Z0-9-_]+")){
                    if(db.getPseudoCount(etPseudo.getText().toString())>0)
                    {
                        tvNewPseudo.setTextColor(Color.RED);
                        tvNewPseudo.setText("Pseudo already used...");
                    }
                    else
                    {
                        tvNewPseudo.setTextColor(Color.GREEN);
                        tvNewPseudo.setText("Enter your pseudo :");
                    }

                }
               else{
                   tvNewPseudo.setTextColor(Color.RED);
                    tvNewPseudo.setText("Enter your pseudo :");
               }

            }
        });

        etPassword1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etPassword1.getText().toString().matches("^[a-zA-Z0-9]{8,}$")){
                    tvNewPw1.setTextColor(Color.GREEN);
                }
                else{
                    tvNewPw1.setTextColor(Color.RED);
                }
            }
        });

        etPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etPassword1.getText().toString().equals(etPassword2.getText().toString())){
                    tvNewPw2.setTextColor(Color.GREEN);
                }
                else{
                    tvNewPw2.setTextColor(Color.RED);
                }
            }
        });


        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if(result.getResultCode() == RESULT_OK){
                            Intent data = result.getData();
                            Bitmap image = (Bitmap) data.getExtras().get("data");
                            if (image == null) Toast.makeText(NewProfilActivity.this, "Erreur lors de la capture de l'image", Toast.LENGTH_LONG).show();
                            try {

                                    File dossier = NewProfilActivity.this.getFilesDir();
                                    FileOutputStream out = new FileOutputStream(new File(dossier, game.getP().getPhoto()));
                                    image.compress(Bitmap.CompressFormat.PNG, 90, out);
                                    out.close();

                            }
                            catch(Exception ex){
                                Toast.makeText(NewProfilActivity.this, "erreur de sauvegarde", Toast.LENGTH_LONG).show();
                            }
                        }
                        db.addProfil(game.getP());
                        Intent resultIntent = new Intent();
                        Bundle b = new Bundle();
                        b.putSerializable("game", game);
                        resultIntent.putExtras(b);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                }
        );
        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
        checkPermission(Manifest.permission.READ_MEDIA_IMAGES, STORAGE_PERMISSION_CODE);
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);


    }

    public void btnSaveProfil(View view){
        DbHelper db =new DbHelper(this);

        if(!etPseudo.getText().toString().matches("[a-zA-Z0-9-_]+")||!etPassword1.getText().toString().matches("^[a-zA-Z0-9]{8,}$")){
            Toast.makeText(this, "Invalid caption...", Toast.LENGTH_LONG).show();
        }
        else if(etPassword1.getText().toString().equals(etPassword2.getText().toString()))
        {
           if(db.getPseudoCount(etPseudo.getText().toString())==0)
           {
                game.getP().setPseudo(etPseudo.getText().toString());
                game.getP().setPassword(etPassword1.getText().toString());
                game.getP().setHighScore(0);
                game.getP().setLevel(1);
                url = game.getP().getPseudo()+".png";
                game.getP().setPhoto(url);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                selectImageLauncher.launch(intent);
            }
        }
    }

    public void btnCancelProfil(View view) {
        Intent resultIntent = new Intent();
        setResult(RESULT_CANCELED, resultIntent);
        finish();
    }

    private void checkPermission(String permission, int permissionCode){
        if(ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED){
            //Demander la permission
            ActivityCompat.requestPermissions(this,new String[]{permission}, permissionCode);
        }else{
            //Toast.makeText(this, "Permission already granted....", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int permissionCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(permissionCode, permissions, grantResults);

    }

    public void btnClearPseudo(View view) {

        etPseudo.setText("");
    }

    public void btnClearPassword(View view) {

        etPassword1.setText("");
    }

    public void btnClearPassword2(View view) {

        etPassword2.setText("");
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