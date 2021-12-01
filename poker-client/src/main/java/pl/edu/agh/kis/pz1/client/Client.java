package pl.edu.agh.kis.pz1.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Client class for the poker game
 */
public class Client {
    /**
     * socket
     */
    private Socket socket;
    /**
     * buffer to reading
     */
    private BufferedReader bufferedReader;
    /**
     * buffer to writing
     */
    private BufferedWriter bufferedWriter;
    /**
     * username
     */
    private String username;
    private static final Logger logger = Logger.getLogger( Client.class.getName() );

    /**
     * Creates client
     * @param socket   socket
     * @param username client username
     */
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

    /**
     * Sends message on bufferWriter.
     */
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

    /**
     * In loop waiting for message from server
     */
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

    /**
     * Closes bufferReader, bufferWriter, socket
     */
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

    /**
     * Starts client
     * @param args parameters
     * @throws IOException
     */
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
