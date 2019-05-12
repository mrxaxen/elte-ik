package server;

import client.Connection;

import java.io.*;
import java.util.*;

public class ClientConnectionHandler extends Thread {

    private Connection connection;
    private Timer timer = new Timer();
    private final Vector<Song> songsPlaying = new Vector<>();
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
//                System.out.println("Substring: " + msg);
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
                                play(m, songsPlaying, connection);
                            }
                        },1000);
                        break;
                    case "change":
                        change(msg,songsPlaying);
                        break;
                    case "stop":
                        stopSong(Integer.parseInt(msg),songsPlaying);
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                System.err.println("Something mishapd' (o.o) Try again!");
            }
        }
    }

    private static void add(String msg,Connection connection) {
        String title = msg;
        try {
            String notes = connection.getMessage();
            Song song = new Song(title,notes);
            songs.put(title,song);
            saveSongs();
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
            saveSongs();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("No such song in the list!");
        }
    }

    private static void play(String msg,Vector<Song> songsPlaying, Connection connection) {
        String[] msgSplit = msg.split(" ");
        int tempo = Integer.parseInt(msgSplit[0]);
        int songTranspose = Integer.parseInt(msgSplit[1]);
        String title = msgSplit[2];

        new Thread(()->{
            Song song = songs.get(title);
            song.setTempo(tempo);
            song.isPlaying = true;
            songsPlaying.add(song);

            int id = songsPlaying.indexOf(song);
            System.out.println("Server playing :" + song.getTitle());
            connection.sendMessage("playing " + id);

            ArrayList<Note> notes = song.transposeSong(songTranspose);
            LinkedList<String> lyrics = new LinkedList<>();
            lyrics.addAll(song.getLyrics());
            int count = 0;
            int size = notes.size();
            for (int i = 0; i < size; i++) {
                if(!song.isPlaying) {
                    break;
                }
                    notes = song.getTransposed();
                    Note note = notes.get(i);
                    if (!note.note.equals("REP")) {
                        String word;
                        if(note.note.equals("R")) {
                            word = "???";
                        } else {
                            word = lyrics.pop();
                        }
                        int delay = Integer.parseInt(note.length)*song.getTempo();
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

    public static void change(String msg, Vector<Song> songsPlaying) throws RuntimeException {
        String[] split = msg.split(" ");
        int id = Integer.parseInt(split[0]);
        int tempo = Integer.parseInt(split[1]);
        int transpose = Integer.parseInt(split[2]);

        Song song = songsPlaying.get(id);
        synchronized (song) {
            song.setTempo(tempo);
            song.transposeSong(transpose);
        }
    }

    public static void stopSong(int id, Vector<Song> songsPlaying) {
        songsPlaying.get(id).isPlaying = false;
    }

    public static void saveSongs() {
        System.out.println("Saving songs...");
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(MusicBoxServer.songsFile),false)))) {
            songs.forEach((title,song) -> {
                pw.println(title);
                String filename = title;
                try(ObjectOutputStream oss = new ObjectOutputStream(new FileOutputStream(new File(filename + ".ser")))) {
                    oss.writeObject(song);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadSongs() {
        System.out.println("Loading songs from file...");
        try(BufferedReader br = new BufferedReader(new FileReader(new File(MusicBoxServer.songsFile)))) {
            String line;
            StringBuilder songsNotFound = new StringBuilder();
            while((line = br.readLine()) != null) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(line.trim()+ ".ser")))){
                    Song song = (Song)ois.readObject();
                    songs.put(song.getTitle(), song);
                } catch (ClassNotFoundException e) {
                    songsNotFound.append(line);
                    songsNotFound.append(",");
                }
            }
            System.out.println("Loading successful");
            System.out.println("Songs not found: " + songsNotFound.toString());
        } catch (FileNotFoundException e) {
            System.err.println("File " + MusicBoxServer.songsFile + " not found!\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
