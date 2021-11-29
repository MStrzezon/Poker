package pl.edu.agh.kis.pz1.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private static final Logger logger = Logger.getLogger( Client.class.getName() );

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
        } catch (IOException e) {
            closeEverything();
        }
    }

    public void sendMessage() {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner s = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = s.nextLine();
                bufferedWriter.write(messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything();
        }
    }

    public void listenForMessage() {
        new Thread(() -> {
            String messageFromServer;

            while (socket.isConnected()) {
                try {
                    messageFromServer = bufferedReader.readLine();
                    logger.info(messageFromServer);
                } catch (IOException e) {
                    closeEverything();
                }
            }
        }).start();
    }

    public void closeEverything() {
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
            logger.info(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configure("./poker-client/src/main/resources/log4j.properties");
        Scanner s = new Scanner(System.in);
        logger.info( "WELCOME IN POKER GAME!\nEnter your username for the game: ");
        String username = s.nextLine();
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();
    }
}
