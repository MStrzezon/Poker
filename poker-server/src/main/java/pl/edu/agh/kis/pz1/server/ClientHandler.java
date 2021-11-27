package pl.edu.agh.kis.pz1.server;

import pl.edu.agh.kis.pz1.protocol.GameProtocol;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {
    public static GameProtocol gp = new GameProtocol();
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    private int id;
    private static int counter = 0;
    private static final Logger logger = Logger.getLogger( ClientHandler.class.getName() );

    private static final String FRAME = "\n--------------------------------------------\n";

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
            logger.info(e.getMessage());
        }
    }
}
