package com.example.lampemagique;

import android.app.Application;

// La classe MyApplication hérite de la classe Application d'Android.
// Elle est utilisée pour stocker une instance de la classe Model, qui permet de manipuler les couleurs
// et de les partager entre les différentes activités de l'application.

public class MyApplication extends Application {
    private Model model;

    // La méthode onCreate est appelée lors de la création de l'application.
    // Elle instancie un objet Model qui sera utilisé pour stocker les couleurs.

    @Override
    public void onCreate() {
        super.onCreate();
        model = new Model();
    }

    // La méthode getModel permet de récupérer l'objet Model créé dans la méthode onCreate.
    // Cela permet de pouvoir y accéder depuis les autres parties de l'application.

    public Model getModel() {
        return model;
    }
}
