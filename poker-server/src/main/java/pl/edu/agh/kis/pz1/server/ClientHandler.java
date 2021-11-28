package pl.edu.agh.kis.pz1.server;

import pl.edu.agh.kis.pz1.protocol.GameProtocol;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * ClientHandler is the class that implements Runnable. The ClientHandler object communicates to the client by reading from and writing to the socket.
 * There is static list of the current ClientHandler objects, which allow broadcasting message to all clients on this server.
 */
public class ClientHandler implements Runnable {
    /**
     * Static GameProtocol that allow managing the game.
     */
    public static GameProtocol gp = new GameProtocol();
    /**
     * Stores the list of all currently active ClientHandler instances.
     */
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    /**
     * Stores the network socket used to interact with the client.
     */
    private Socket socket;
    /**
     * Reads text from the client.
     */
    private BufferedReader bufferedReader;
    /**
     * Writes text to the client.
     */
    private BufferedWriter bufferedWriter;
    /**
     * Stores client username.
     */
    private String clientUsername;
    /**
     * Store client id.
     */
    private int id;
    /**
     * Stores information about number of clients.
     */
    private static int counter = 0;
    /**
     * logger
     */
    private static final Logger logger = Logger.getLogger( ClientHandler.class.getName() );
    /**
     * Frame to highlight messages from the server
     */
    private static final String FRAME = "\n--------------------------------------------\n";

    /**
     * Creates client handler.
     * @param socket  The network socket used to interact with the client.
     * @throws IOException
     */
    public ClientHandler(Socket socket) throws IOException {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            id = counter++;
            clientHandlers.add(this);
            bufferedWriter.write(FRAME +"Welcome " + clientUsername + ". A number of participants: " + clientHandlers.size() +
                    "\nEnter /help to see all commands."+ FRAME);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    /**
     * Writes to the client on the basis of the GameProtocol. And reads message from the client
     * by forwarding it to the GameProtocol.
     */
    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                String[] tokens  = messageFromClient.split(" ");
                List<Integer> parameters = new ArrayList<>();
                if (tokens.length > 1) {
                    for (int i = 1; i < tokens.length; i++) parameters.add(Integer.parseInt(tokens[i]));
                }
                String[] action = gp.processInput(id, tokens[0], parameters);
                if (Objects.equals(action[0], "ONE")) {
                    bufferedWriter.write(FRAME +action[1]+ FRAME);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } else {
                    broadcastMessage(FRAME +action[1]+ FRAME);
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    /**
     * Broadcasts message to all clients on server.
     * @param messageToSend  message to be sent.
     */
    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                clientHandler.bufferedWriter.write(messageToSend);
                clientHandler.bufferedWriter.newLine();
                clientHandler.bufferedWriter.flush();
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    /**
     * Removes client handler.
     */
    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left the game!");
    }

    /**
     * Closes everything (socket, bufferReader, bufferWriter).
     * @param socket           socket to be closed.
     * @param bufferedReader   bufferReader to be closed.
     * @param bufferedWriter   bufferWriter to be closed.
     */
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
            logger.info(e.getMessage());
        }
    }
}
