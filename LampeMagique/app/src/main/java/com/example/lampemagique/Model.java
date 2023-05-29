package com.example.lampemagique;

import android.graphics.Color;

public class Model {

    // Définition des attributs de la classe

    private int valRouge = 0;
    private int valVert = 0;
    private int valBleu = 0;

    // Définition d'une variable texteCouleur qui affichera les valeurs de rouge, vert et bleu

    private String texteCouleur = "R: " + valRouge + ", G: " + valVert + ", B: " + valBleu;


    public int getRouge() {
        return valRouge;
    }

    public void setRouge(int valRouge) {
        this.valRouge = valRouge;
    }

    public int getVert() {
        return valVert;
    }

    public void setVert(int valVert) {
        this.valVert = valVert;
    }

    public int getBleu() {
        return valBleu;
    }

    public void setBleu(int valBleu) {
        this.valBleu = valBleu;
    }

    public String getTexteCouleur() {
        return texteCouleur;
    }


    public void setTexteCouleur(int rouge, int vert, int bleu) {
        this.texteCouleur ="R: " + rouge + ", G: " + vert + ", B: " + bleu;
    }


    // Méthode pour récupérer la couleur en format RGB

    public int getRGB(){
        return Color.rgb(valRouge, valVert, valBleu);
    }


    // Méthode pour définir la couleur en format RGB

    public void setRGB(int rgb){
        this.valRouge= (rgb>>16)& 0xFF;
        this.valVert= (rgb>>8)& 0xFF;
        this.valBleu= rgb & 0xFF;
    }

    // Méthode pour récupérer la couleur en format hexadécimal

    public String getHexa() {
        return String.format("%02X%02X%02X",valRouge,valVert,valBleu);
    }


}