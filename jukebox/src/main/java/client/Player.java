package client;

import server.Note;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class Player implements Runnable{

    private Connection serverConn;
    private Synthesizer synthesizer;
    private static Object lock = new Object();
    private static int songsPlaying = 0;

    Player(Connection serverConn) {
        this.serverConn = serverConn;
    }

    public static void play() {
        synchronized (lock) {
            songsPlaying++;
            lock.notifyAll();
        }
    }

    @Override
    public void run() {
        while (true) {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Playing..");
            try {
                synthesizer = MidiSystem.getSynthesizer();
                synthesizer.open();
//                System.out.println(serverConn.getMessage()); // Server sends the played song's id
                MidiChannel piano = synthesizer.getChannels()[0];
                String message = serverConn.getMessage();
//                System.out.println("Message in player: " + message);
                String[] msg;
                String lyrics = "";

                int prevNote = 0;
                while (true) {
                    while(message.contains("playing")) {
                        message = serverConn.getMessage();
                    }
                    msg = message.split(" ");

                    int note;
                    System.out.println("Play iter: " + msg[0]);
                    if(msg[0].equals("FIN")) {
                        songsPlaying--;
                        if(songsPlaying == 0) {
                            break;
                        }
                    } else {
                        lyrics = msg[1];
                    }
                    if(msg[0].equals("R")) {
                        piano.noteOff(prevNote);
                        message = serverConn.getMessage();
                        continue;
                    }
                    if (msg[0].contains("/")) {
                        String[] split = msg[0].split("/");
                        note = Note.getNoteByStr(split[0]) + Integer.parseInt(split[1]) * 12;
                    } else {
                        note = Note.getNoteByStr(msg[0]);
                    }

                    piano.noteOn(note, 100);
                    System.out.println("Lyrics:" + lyrics);
                    prevNote = note;
                    message = serverConn.getMessage();
                }
                piano.allNotesOff();
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synthesizer.close();
        }
    }

}
