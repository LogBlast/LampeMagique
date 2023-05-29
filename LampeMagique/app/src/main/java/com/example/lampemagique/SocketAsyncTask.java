package com.example.lampemagique;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class SocketAsyncTask extends AsyncTask<Void, Void, String> {
    private static final String SERVER_ADDRESS = "chadok.info";
    private static final int SERVER_PORT = 9998;
    private final String message;

    public SocketAsyncTask(String message) {
        this.message = message;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            // Établir une connexion socket avec le serveur
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);

            // Envoyer le message au serveur
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);

            // Lire la réponse du serveur
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();

            // Fermer la connexion socket
            socket.close();

            // Retourner la réponse du serveur
            return response;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}