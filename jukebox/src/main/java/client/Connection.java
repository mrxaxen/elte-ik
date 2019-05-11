package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class Connection implements AutoCloseable{

    private final int id;
    private boolean sendMsg;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private Thread commIn;
    private Thread commOut;
//    private LinkedList<String> messagesToSend = new LinkedList<>();
    private LinkedList<String> messagesGet = new LinkedList<>();

    public Connection(Socket socket) throws IOException {
        id = -1;
        this.socket = socket;
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
    }

    public Connection(Socket socket, int id) throws IOException {
        this.id = id;
        this.socket = socket;
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
    }

    public void run() {
        commIn = new Thread(()->{
            while (in.hasNextLine()) {
                String next = in.nextLine();
//                System.out.println("Got input from server: " + next);
                synchronized (messagesGet) {
                    messagesGet.add(next);
                    messagesGet.notifyAll();
                }
            }
        });
        commIn.start();

//        commOut = new Thread(()-> {
//            while (true) {
//                synchronized (messagesToSend) {
//                    try {
//                        System.out.println("Running in");
//                        messagesToSend.wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    while (!messagesToSend.isEmpty()) {
//                        out.println(messagesToSend.pop());
//                    }
//                    out.flush();
//                }
//                System.out.println("Running around");
//            }
//        });
//        commOut.start();
    }

    public void sendMessage(String message) {
        out.println(message);
        out.flush();
        //        synchronized (messagesToSend) {
//            messagesToSend.add(message);
//            messagesToSend.notify();
//        }
    }

    public String getMessage() throws InterruptedException {
        String msg = null;
        while (msg == null) {
            if(messagesGet.isEmpty()) {
                synchronized (messagesGet) {
                    messagesGet.wait();
                }
            }
            synchronized (messagesGet) {
                msg = messagesGet.pop();
            }
//            System.out.println("Popping message: " + msg);
        }
        return msg;
    }

    public boolean isAlive() {
        return !socket.isClosed();
    }

    @Override
    public void close() throws Exception {
        socket.close();
        in.close();
        out.close();
    }
}
