package com.example.lampemagique;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Handler;
import java.lang.Runnable;
import android.os.Looper;
import android.widget.Toast;

import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRP;  // bouton rouge plus ...
    private Button btnRM;
    private Button btnVP;
    private Button btnVM;
    private Button btnBP;
    private Button btnBM;
    private Button btnAll;

    private MyApplication app;
    private Model model;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Récupération du modèle

        app = (MyApplication) getApplication();
        model = app.getModel();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Récupération des boutons de l'interface

        btnRP = findViewById(R.id.brp);
        btnRP.setOnClickListener(this);

        btnRM = findViewById(R.id.brm);
        btnRM.setOnClickListener(this);

        btnVP = findViewById(R.id.bvp);
        btnVP.setOnClickListener(this);

        btnVM = findViewById(R.id.bvm);
        btnVM.setOnClickListener(this);

        btnBP = findViewById(R.id.bbp);
        btnBP.setOnClickListener(this);

        btnBM = findViewById(R.id.bbm);
        btnBM.setOnClickListener(this);

        btnAll = findViewById(R.id.brvb);
        btnAll.setOnClickListener(this);


        // Récupération de la couleur d'accueil

        String couleurAccueil = getIntent().getStringExtra("COULEUR");
        if(couleurAccueil.equals("Rouge")) {
            model.setRouge(255);
        }
        else if(couleurAccueil.equals("Vert")){
            model.setVert(255);
        }
        else if(couleurAccueil.equals("Bleu")){
            model.setBleu(255);
        }

        // Mise à jour de la couleur de l'interface
        model.setTexteCouleur(model.getRouge(), model.getVert(), model.getBleu());
        mettreAJourCouleur();



    }




    // Méthode pour mettre à jour la couleur de l'interface

    private void mettreAJourCouleur() {

        int couleur = Color.rgb(model.getRouge(), model.getVert(), model.getBleu());
        String text ="R: " + model.getRouge() + ", G: " + model.getVert() + ", B: " + model.getBleu();

        btnAll.setBackgroundColor(couleur);
        btnAll.setText(text);

        // Détermination de la couleur de texte en fonction de la luminance de la couleur de fond

        float luminance = Color.luminance(couleur);
        int textColor = luminance <= 0.5 ? Color.WHITE : Color.BLACK;
        btnAll.setTextColor(textColor);
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {


        // Vérifier si le bouton "Séquence de couleurs" a été cliqué

        if(v.getId() == R.id.brvb){
            executeColorSequence();
        }

        // Mettre à jour les valeurs de couleur en fonction du bouton cliqué

        switch (v.getId()) {
            case R.id.brp:
                int valRouge = Math.min(255, model.getRouge() + 16);
                model.setRouge(valRouge);
                break;
            case R.id.brm:
                valRouge = Math.max(0, model.getRouge() - 16);
                model.setRouge(valRouge);
                break;
            case R.id.bvp:
                int valVert = Math.min(255, model.getVert() + 16);
                model.setVert(valVert);
                break;
            case R.id.bvm:
                valVert = Math.max(0, model.getVert() - 16);
                model.setVert(valVert);
                break;
            case R.id.bbp:
                int valBleu = Math.min(255, model.getBleu() + 16);
                model.setBleu(valBleu);
                break;
            case R.id.bbm:
                valBleu = Math.max(0, model.getBleu() - 16);
                model.setBleu(valBleu);
                break;
            case R.id.brvb:
                model.setRouge(255);
                model.setVert(0);
                model.setBleu(0);
                break;
            default:
                break;
        }

        // Envoyer la couleur actuelle au serveur via une tâche asynchrone
        Log.i("",""+model.getHexa());
        new SocketAsyncTask( "06"+model.getHexa() ).execute();


        // Mettre à jour la couleur du bouton "Tout" et son texte
        model.setTexteCouleur(model.getRouge(), model.getVert(), model.getBleu());
        mettreAJourCouleur();
    }



    // Méthode pour lancer une séquence de couleurs sur un thread séparé

    private void executeColorSequence() {
        final int[] colors = {Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA};
        final int originalColor = model.getRGB();

        // Créer un handler associé au thread principal
        final Handler handler = new Handler(Looper.getMainLooper());

        // Créer un worker thread pour exécuter la séquence de couleurs
        Thread colorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int color : colors) {
                        model.setRGB(color);

                        // Utiliser le handler pour mettre à jour l'interface utilisateur depuis le worker thread
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mettreAJourCouleur();
                            }
                        });

                        Thread.sleep(2000);
                    }
                    model.setRGB(originalColor);

                    // Utiliser le handler pour mettre à jour l'interface utilisateur depuis le worker thread
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mettreAJourCouleur();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        colorThread.start(); // Lancer le worker thread pour exécuter la séquence de couleurs
    }



    // Méthode appelée lors de la rotation de l'écran pour sauvegarder les valeurs de couleur actuelles

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("rouge", model.getRouge());
        outState.putInt("vert", model.getVert());
        outState.putInt("bleu", model.getBleu());
    }

    // Méthode appelée après la rotation de l'écran pour restaurer les valeurs de couleur sauvegardées

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        model.setRouge(savedInstanceState.getInt("rouge"));
        model.setVert(savedInstanceState.getInt("vert"));
        model.setBleu(savedInstanceState.getInt("bleu"));
        mettreAJourCouleur();
    }





}




