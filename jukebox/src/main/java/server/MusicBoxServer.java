package server;

import client.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
//C(60) C#/Db(61) D(62) D#/Eb(63) E(64) F(65) F#/Gb(66) G(67) G#/Ab(68) A(69) A#/Bb(70) B(71)   /+-1 => +-12
public class MusicBoxServer {

    private static ArrayList<ClientConnectionHandler> connectionHandlers = new ArrayList<>();

    public static void main(String[] args) {
        int port = 40000;

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            int clientId = 0;
            while (true) {
                System.out.println("Waiting for clients...");
                Connection connection = new Connection(serverSocket.accept(),clientId);
                System.out.println("Client connected with id: " + clientId);
                clientId++;
                connection.run();
                ClientConnectionHandler clientConnectionHandler = new ClientConnectionHandler(connection);
                clientConnectionHandler.start();
                connectionHandlers.add(clientConnectionHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
