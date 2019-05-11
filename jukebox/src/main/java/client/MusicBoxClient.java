package client;

import server.Song;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MusicBoxClient {

    private static Thread player;

    public static void main(String[] args) {
        int port = 40000;
        String host = "127.0.0.1";
        Scanner stdIn = new Scanner(System.in);
        try(Connection serverConn = new Connection(new Socket(host,port))) {
            serverConn.run();
            player = new Thread(new Player(serverConn));
            player.start();
            if(serverConn.isAlive()) {
//                play(120, 0, Song.SONG_C4.getTitle(),serverConn);
//                play(120, 0, Song.SONG_TEST1.getTitle(), serverConn);
//                play(120, 0, Song.SONG_TEST2.getTitle(), serverConn);
                play(120,0,Song.SONG_MEGALOVANIA_MAIN_BASE.getTitle(),serverConn);
                play(120,0,Song.SONG_MEGALOVANIA_BASE_BASS.getTitle(),serverConn);
                play(120,0,Song.SONG_MEGALOVANIA_BASE_LOWER.getTitle(),serverConn);
                play(120,0,Song.SONG_MEGALOVANIA_BASE_UPPER.getTitle(),serverConn);
                play(120,0,Song.SONG_MEGALOVANIA_MAIN_RAISED.getTitle(),serverConn);
//                String input[] = getInputFromUser(stdIn);
//                add("bociboci","C 4 E 4 C 4 E 4 G 8 G 8 REP 6;1 C/1 4 B 4 A 4 G 4 F 8 A 8 G 4 F 4 E 4 D 4 C 8 C 8",serverConn);
//                addlyrics("bociboci", "bo ci bo ci tar ka se fu le se far ka o da me gyunk lak ni a hol te jet kap ni", serverConn);
//                play(120,0,"bociboci", serverConn);
            }
            player.join();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void add(String title, String notes, Connection serverConn) {
        String msg = String.join(" ","add",title);
        serverConn.sendMessage(msg);
        msg = notes;
        serverConn.sendMessage(msg);
    }

    private static void addlyrics(String title, String lyrics, Connection serverConn) {
        String msg = String.join(" ", "addlyrics", title);
        serverConn.sendMessage(msg);
        serverConn.sendMessage(lyrics);
    }

    private static void play(int tempo, int transpose, String title, Connection serverConn) {
        serverConn.sendMessage(String.join(" ","play",tempo+"",transpose+"",title));
        Player.play();
    }

    private static String[] getInputFromUser(Scanner stdIn) {
        return null;
    }
}