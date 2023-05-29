package com.example.lampemagique;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class pageAccueil extends AppCompatActivity implements View.OnClickListener {

    // Déclaration des variables des boutons et de l'image bouton pour la langue

    private MyApplication app;
    private Model model;

    // Méthode appelée lors de la création de l'activité

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Récupération de l'objet MyApplication pour accéder à l'objet Model

        app = (MyApplication) getApplication();
        model = app.getModel();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        // Récupération des boutons et de l'image bouton par leur identifiant

        Button btnR = findViewById(R.id.btnRouge);
        Button btnV = findViewById(R.id.btnVert);
        Button btnB = findViewById(R.id.btnBleu);
        ImageButton btnLang = findViewById(R.id.btnLangue);

        // Ajout des listeners pour les boutons de couleur

        btnR.setOnClickListener(this);
        btnV.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnLang.setOnClickListener(new View.OnClickListener() {

            // Méthode appelée lors du clic sur l'image bouton de la langue

            @Override
            public void onClick(View v) {
                onLanguageButtonClick(v);
            }
        });
    }

    // Méthode appelée lors du clic sur un bouton de couleur

    @Override
    public void onClick(View v) {

        // Création d'une intention pour démarrer l'activité MainActivity

        Intent intent = new Intent(this, MainActivity.class);

        // Modification de l'intention en fonction du bouton de couleur cliqué

        switch (v.getId()) {
            case R.id.btnRouge:
                intent.putExtra("COULEUR", "Rouge");
                model.setRouge(255);
                break;
            case R.id.btnVert:
                intent.putExtra("COULEUR", "Vert");
                model.setVert(255);
                break;
            case R.id.btnBleu:
                intent.putExtra("COULEUR", "Bleu");
                model.setBleu(255);
                break;
            default:
                break;
        }

        // Envoi d'une commande au serveur pour afficher la couleur sur la lampe magique

        new SocketAsyncTask( "06"+model.getHexa() ).execute();

        // Démarrage de l'activité MainActivity

        startActivity(intent);
    }


    // Méthode pour récupérer la langue actuelle de l'application

    public String getCurrentLanguage() {
            return getResources().getConfiguration().getLocales().get(0).getLanguage();
    }

    // Méthode pour changer la langue de l'application

    public void setLanguage(String lang) {
        Locale locale = new Locale(lang);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        recreate();
    }


    // Méthode appelée lors du clic sur l'image bouton de la langue

        public void onLanguageButtonClick(View v) {

            // Récupération de la langue actuelle

            String currentLang = getCurrentLanguage();

            // Changement de la langue

            ImageButton btn = (ImageButton)v;
            if (currentLang.equals("fr")) {
                setLanguage("en");
                btn.setImageResource(R.drawable.flagen);

                btn.invalidate();
            } else {
                setLanguage("fr");
                btn.setImageResource(R.drawable.flagfr);

                btn.invalidate();
            }
        }



}