package server;

import client.Connection;

import java.util.*;

public class ClientConnectionHandler extends Thread {

    private Connection connection;
    private Timer timer = new Timer();
    private static final Vector<Song> songsPlaying = new Vector<>();
    private static final HashMap<String,Song> songs = new HashMap<>();

    public ClientConnectionHandler(Connection connection) {
        songs.put(Song.SONG_C4.getTitle(), Song.SONG_C4);
        songs.put(Song.SONG_TEST1.getTitle(), Song.SONG_TEST1);
        songs.put(Song.SONG_TEST2.getTitle(), Song.SONG_TEST2);
        songs.put(Song.SONG_MEGALOVANIA_MAIN_BASE.getTitle(),Song.SONG_MEGALOVANIA_MAIN_BASE);
        songs.put(Song.SONG_MEGALOVANIA_BASE_BASS.getTitle(),Song.SONG_MEGALOVANIA_BASE_BASS);
        songs.put(Song.SONG_MEGALOVANIA_BASE_LOWER.getTitle(),Song.SONG_MEGALOVANIA_BASE_LOWER);
        songs.put(Song.SONG_MEGALOVANIA_BASE_UPPER.getTitle(),Song.SONG_MEGALOVANIA_BASE_UPPER);
        songs.put(Song.SONG_MEGALOVANIA_MAIN_RAISED.getTitle(),Song.SONG_MEGALOVANIA_MAIN_RAISED);
        this.connection = connection;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String msg = connection.getMessage();
                String cmd = msg.split(" ")[0];
                msg = msg.substring(cmd.length()).trim();
                System.out.println("Substring: " + msg);
                switch (cmd) {
                    case "add":
                        add(msg,connection);
                        break;
                    case "addlyrics":
                        addlyrics(msg,connection);
                        break;
                    case "play":
                        final String m = msg;
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                play(m, connection);
                            }
                        },1000);
                        break;
                    case "change":
                        break;
                    case "stop":
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void add(String msg,Connection connection) {
        String title = msg;
        try {
            String notes = connection.getMessage();
            Song song = new Song(title,notes);
            songs.put(title,song);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void addlyrics(String msg, Connection connection) {
        String title = msg;
        try {
            String lyrics = connection.getMessage();
            Song song = songs.get(title);
            song.addLyrics(lyrics);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("No such song in the list!");
        }
    }

    private static void play(String msg, Connection connection) {
        String[] msgSplit = msg.split(" ");
        int tempo = Integer.parseInt(msgSplit[0]);
        int songTranspose = Integer.parseInt(msgSplit[1]);
        String title = msgSplit[2];

        new Thread(()->{
            Song song = songs.get(title);
            songsPlaying.add(song);
            int id = songsPlaying.indexOf(song);
            System.out.println("Server playing :" + song.getTitle());
            connection.sendMessage("playing " + id);

            ArrayList<Note> notes = song.transposeSong(songTranspose);
            LinkedList<String> lyrics = new LinkedList<>();
            lyrics.addAll(song.getLyrics());
//            notes.forEach((note)->{
//                System.out.println(note + " " + lyrics.get(notes.indexOf(note)));
//            });
            int count = 0;
            for (int i = 0; i < notes.size(); i++) {
                Note note = notes.get(i);
                if (!note.note.equals("REP")) {
                    String word;
                    if(note.note.equals("R")) {
                        word = "???";
                    } else {
                        word = lyrics.pop();
                    }
                    int delay = Integer.parseInt(note.length)*tempo;
                    System.out.println(note + " " + word);
                    connection.sendMessage(String.join(" ",note.note, word));
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    String[] split = note.length.split(";");
                    int repNotes = Integer.parseInt(split[0]);
                    int repCount = Integer.parseInt(split[1]);
                    if(count == repCount) {
                        count = 0;
                        continue;
                    } else {
                        i = i-repNotes-1;
                        count++;
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            connection.sendMessage("FIN");
            songsPlaying.remove(song);
        }).start();
    }

    public static void change() {

    }
}
