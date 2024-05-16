package fr.jonybegood.breakingball.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import fr.jonybegood.breakingball.entities.Profil;

//Pour Gérer la base de donnée, il suffit d'hériter de la classe SqliteOpenHelper et d'implémenter 2 méthode create et upgrade
public class DbHelper extends SQLiteOpenHelper {

    //Version de la database
    private static final int DATABASE_VERSION = 1;
    //Database name
    private static final String DATABASE_NAME = "Breaking ball";

    //Table name
    private static final String TABLE_NAME = "profils";

    //Colonne name
    private static final String COLUMN_PSEUDO = "Pseudo";
    private static final String COLUMN_PASSWORD = "Password";
    private static final String COLUMN_HIGHSCORE = "Highscore";
    private static final String COLUMN_LEVEL = "Level";
    private static final String COLUMN_PHOTO = "Photo";


    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(">>>Database", "Created...");
        //creation de la table  Note
        String script = "CREATE TABLE "+TABLE_NAME+"("+COLUMN_PSEUDO+" TEXT PRIMARY KEY,"+COLUMN_PASSWORD+" TEXT,"+COLUMN_HIGHSCORE+" INTEGER,"+COLUMN_LEVEL+" INTEGER,"+COLUMN_PHOTO+" TEXT)";
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //methode d'insertion d'une note
    public void addProfil(Profil profil){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PSEUDO,profil.getPseudo());
        values.put(COLUMN_PASSWORD,profil.getPassword());
        values.put(COLUMN_HIGHSCORE,profil.getHighScore());
        values.put(COLUMN_LEVEL,profil.getLevel());
        values.put(COLUMN_PHOTO,profil.getPhoto());
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    //Methode de lecture de la table (liste des notes)
    public List<Profil> getAllProfils(){
        List<Profil> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String script = "SELECT * FROM "+ TABLE_NAME;
        Cursor cursor = db.rawQuery(script, null);
        if(cursor.moveToFirst()){
            do {
                Profil note = new Profil();
                note.setPseudo((cursor.getString(0)));
                note.setPassword((cursor.getString(1)));
                note.setHighScore(Long.parseLong((cursor.getString(2))));
                note.setLevel(Integer.parseInt((cursor.getString(3))));
                note.setPhoto((cursor.getString(4)));
                notes.add(note);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }

    //methode get by Id
    public Profil getByPseudo(String pseudo){
        Profil profil=null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{COLUMN_PSEUDO,COLUMN_PASSWORD,COLUMN_HIGHSCORE,COLUMN_LEVEL,COLUMN_PHOTO},
                COLUMN_PSEUDO+"=?", new String[]{String.valueOf(pseudo)},null,null,null);
        if(cursor!=null){
            cursor.moveToFirst();
            profil = new Profil(cursor.getString(0), cursor.getString(1), Long.parseLong(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), cursor.getString(4));
        }
        cursor.close();
        db.close();

        return profil;
    }

    public void modifyProfil(Profil p){
        Profil profil=null;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD,p.getPassword());
        values.put(COLUMN_HIGHSCORE,p.getHighScore());
        values.put(COLUMN_LEVEL,p.getLevel());
        values.put(COLUMN_PHOTO,p.getPhoto());
        db.update(TABLE_NAME,values,COLUMN_PSEUDO+"=?",new String[]{String.valueOf(p.getPseudo())});
    }

    public boolean removeByPseudo(String pseudo){
        int res;
        SQLiteDatabase db = this.getReadableDatabase();
        res = db.delete(TABLE_NAME, COLUMN_PSEUDO+"=?", new String[]{String.valueOf(pseudo)});
        db.close();
        if(res>0) return true;
        else return false;
    }
    public void clearTable(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }


    //Methode count
    public int getProfilsCount(){
        String script = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(script,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int getPseudoCount(String pseudo){
        String script = "SELECT * FROM "+TABLE_NAME+" WHERE " + COLUMN_PSEUDO + " = '" + pseudo + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(script,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

}

