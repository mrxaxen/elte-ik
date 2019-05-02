package server;

import java.io.*;
import java.net.ServerSocket;
import java.util.*;

public class MusicBoxServer {

    public static String songsFileName = "songs.txt";
    private final static ArrayList<ClientConnection> clients = new ArrayList<>();
    private final static ArrayList<Song> songs = new ArrayList<>();

    private static void initSongs() {
        try(Scanner in = new Scanner(new BufferedReader(new FileReader(new File(songsFileName))))) {
            String[] line = null;
            while (in.hasNextLine()) {
                line = in.nextLine().split(" ");
            }
            for (String s : line) {
                try(ObjectInputStream oin = new ObjectInputStream(new FileInputStream(s + ".ser"))) {
                    Song song = (Song) oin.readObject();
                    songs.add(song);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(songsFileName + " not found!\n");
        }
    }

    static boolean running = true;
    public static void main(String[] args) {
        initSongs();
        System.out.println("Starting server..");
        try(ServerSocket serverSocket = new ServerSocket(40000);
            Scanner stdin = new Scanner(System.in)) {
            System.out.println("Server online.");
            Thread stdinThread = new Thread(() -> {
                if(stdin.next().equals("close")) closeServer();
            });
            stdinThread.start();
            while(running) {
                System.out.println("Waiting for clients..");
                ClientConnection client = new ClientConnection(serverSocket.accept());
                System.out.println("Client connected.");
                client.run();
                clients.add(client);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void add(String title, Scanner in) {
        LinkedList<Note> notes = new LinkedList<>();
        String[] line = in.nextLine().split(" ");
        int m = 0;
        String note = null;
        int length = 0;
        for (String s : line) {
            if (m % 2 == 0) {
                note = s;
            }else {
                length = Integer.parseInt(s);
                notes.add(new Note(note,length));
            }
        }
        songs.add(new Song(title, notes));
    }

    public static void addLyrics(String title, Scanner in) {
        LinkedList<String> lyrics = new LinkedList<>();
        while(in.hasNext()) {
            lyrics.add(in.next());
        }
    }

    public static void play(String title,int tempo, int transpose, PrintWriter out) {
        int i = 0;
        Song song;
        while(!(song = songs.get(i)).getTitle().equals(title)) {
            i++;
        }
        LinkedList<Note> notes = song.getNotes();
        LinkedList<String> lyrics = song.getLyrics();
        Iterator itNotes = notes.iterator();
        Iterator itLyrics = lyrics.iterator();
        Timer timer = new Timer();
        while (itNotes.hasNext() && itLyrics.hasNext()) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Note note = (Note) itNotes.next();
                    String lyrics = (String) itLyrics.next();
                    out.print(note + " " + lyrics);
                }
            },tempo);
        }
    }

    public static void change(int id, int tempo,int transpose) {

    }

    public static void stop(int id) {

    }

    private static void closeServer() {
        File file = new File("/" + songsFileName);
        try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))){
            for (Song song : songs) {
                pw.println(song.getTitle()+ " ");
            }
            System.out.println("Printed song names into " + file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

}
