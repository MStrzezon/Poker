package pl.edu.agh.kis.pz1.server;

import pl.edu.agh.kis.pz1.exceptions.TooManyClientsException;
import pl.edu.agh.kis.pz1.game.Game;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    public Game game;
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public int getNumberOfClients() { return clientHandlers.size(); }

    public ClientHandler(Socket socket) throws IOException {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientUsername + " has entered a game");
            bufferedWriter.write("Welcome " + clientUsername + ". A number of participants: " + clientHandlers.size() +
                    "\nEnter /help to see all commands.");
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                runCommand(messageFromClient);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left the game!");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String help() throws IOException {
        String help_message = """
                ------------------------------------------------
                IN MENU:
                    /help - print all commands.
                    /players - print number of players.
                    /start - start poker game (min. 2 players).
                IN GAME:          
                    /hand - print all your cards.   
                ------------------------------------------------
                """;
        bufferedWriter.write(help_message);
        bufferedWriter.flush();
        return help_message;
    }

    public int numberOfPlayers() throws IOException {
        int numberOfPlayers = clientHandlers.size();
        bufferedWriter.write(Integer.toString(numberOfPlayers));
        bufferedWriter.newLine();
        bufferedWriter.flush();
        return numberOfPlayers;
    }

    public String runCommand(String command) throws IOException {
        switch (command) {
            case "/help" -> {
                help();
                return "/help";
            }
            case "/players" -> {
                numberOfPlayers();
                return "/players";
            }
            case "/start" -> {
                if (getNumberOfClients() < 2) {
                    bufferedWriter.write("Too many clients to play. Wait for another player");
                    bufferedWriter.flush();
                } else {
                    game = new Game(1, 5);
                }
            }
        }
        return "Command not recognized. Enter /help to print all commands";
    }
}