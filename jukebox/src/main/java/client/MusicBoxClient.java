package client;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class MusicBoxClient {

    static HashMap<String, Integer> notes = new HashMap<>();

    private static void initNotes() {
        notes.put("C",60);
        notes.put("C#",61);
        notes.put("Db",61);
        notes.put("D",62);
        notes.put("D#",63);
        notes.put("Eb",63);
        notes.put("E",64);
        notes.put("F",65);
        notes.put("F#",66);
        notes.put("Gb",66);
        notes.put("G",67);
        notes.put("G#",68);
        notes.put("Ab",68);
        notes.put("A",69);
        notes.put("A#",70);
        notes.put("Bb",70);
        notes.put("B",71);
    }

    public static void main(String[] args) {
        String hostname = "127.0.0.1";
        int port = 40000;
        LinkedList<String> messageFromServer = new LinkedList<>();
        //C(60) C#/Db(61) D(62) D#/Eb(63) E(64) F(65) F#/Gb(66) G(67) G#/Ab(68) A(69) A#/Bb(70) B(71)   /+-1 => +-12

        initNotes();

        System.out.println("Connecting to server at " + hostname + ":" + port);
        try(Socket socket = new Socket(hostname,port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            Scanner in = new Scanner(socket.getInputStream());
            Scanner stdin = new Scanner(System.in)) {

            System.out.println("Connected.");
            Thread toServer = new Thread(()-> {
                out.println("Hello server!");
                while (stdin.hasNext()) {
                    out.print(stdin.next());
                }
            });
            toServer.start();

            Thread fromServer = new Thread(()-> {
                while(true) {
                    if(in.hasNext()) {
                        String message = in.next();
                        synchronized (messageFromServer) {
                            messageFromServer.add(message);
                            if(message.equals("FIN")) messageFromServer.notifyAll();
                        }
                    }
                }
            });

            Thread play = new Thread(()->{
                try {
                    messageFromServer.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try(Synthesizer synth = MidiSystem.getSynthesizer()) {
                    MidiChannel piano = synth.getChannels()[0];
                    while(!messageFromServer.isEmpty() && socket.isConnected()) {
                        String message;
                        synchronized (messageFromServer) {
                            message = messageFromServer.pop();
                        }
                        String[] cmd = message.split(" ");
                        playSound(piano,cmd[0],Integer.parseInt(cmd[1]));
                    }
                } catch (MidiUnavailableException e) {
                    e.printStackTrace();
                }
            });
            play.join();
            fromServer.start();
            toServer.join();
            fromServer.join();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void playSound(MidiChannel channel, String note, int length) {

    }
}
