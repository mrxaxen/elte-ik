package server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientConnection implements AutoCloseable {

    private Thread inThread;
    private Thread outThread;
    private Socket socket;
    private PrintWriter out;
    private Scanner in;

    ClientConnection(Socket socket) throws IOException {
        this.socket = socket;
        in = new Scanner(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(),true);
    }

    public void run() {
        inThread = new Thread(() -> {
            while (true) {
                if(in.hasNext()) {
                    String input = in.next();
                    System.out.println(input);
                    switch (input) {
                        case "add":
                            MusicBoxServer.add(in.next(),in);
                            break;
                        case "addlyrics":
                            MusicBoxServer.addLyrics(in.next(),in);
                            break;
                        case "play":
                            MusicBoxServer.play(in.next(),Integer.parseInt(in.next()),Integer.parseInt(in.next()), out);
                            break;
                        case "change":
                            MusicBoxServer.change(Integer.parseInt(in.next()),Integer.parseInt(in.next()),Integer.parseInt(in.next()));
                            break;
                        case "stop":
                            MusicBoxServer.stop(Integer.parseInt(in.next()));
                            break;
                        default:
                            out.println("Wrong command!");
                            break;
                    }
                }
            }
        });
        inThread.start();

        outThread = new Thread(() ->{
            out.println("Hello client!!");
            out.flush();
        });
        outThread.start();
    }

    @Override
    public void close() throws Exception {
        in.close();
        out.close();
        socket.close();
    }
}
