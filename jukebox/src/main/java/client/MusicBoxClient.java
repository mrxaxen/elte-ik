package client;

import server.Song;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MusicBoxClient {

    private static Thread player;
    /*
    * Megalovania is added by default in ClientConnectionHandler, and being played on start
    * */
    public static void main(String[] args) {
        int port = 40000;
        String host = "127.0.0.1";
        Scanner stdIn = new Scanner(System.in);
        try(Connection serverConn = new Connection(new Socket(host,port))) {
            serverConn.run();
            player = new Thread(new Player(serverConn));
            player.start();
            boolean b = true;
            while (serverConn.isAlive()) {
                if(b) {
                    play("125","0",Song.SONG_MEGALOVANIA_MAIN_BASE.getTitle(),serverConn);
                    play("125","0",Song.SONG_MEGALOVANIA_BASE_BASS.getTitle(),serverConn);
                    play("125","0",Song.SONG_MEGALOVANIA_BASE_LOWER.getTitle(),serverConn);
                    play("125","0",Song.SONG_MEGALOVANIA_BASE_UPPER.getTitle(),serverConn);
                    play("125","0",Song.SONG_MEGALOVANIA_MAIN_RAISED.getTitle(),serverConn);
                    b = false;
                }
                if(stdIn.hasNextLine()) {
                    String userInput = stdIn.nextLine();
                    String[] splitInput = userInput.split(" ");
                    String cmd = splitInput[0];
                    switch (cmd) {
                        case "add":
                            if(stdIn.hasNextLine())
                                add(splitInput[1],stdIn.nextLine(),serverConn);
                            break;
                        case "addlyrics":
                            if(stdIn.hasNextLine())
                                addlyrics(splitInput[1],stdIn.nextLine(),serverConn);
                            break;
                        case "play":
                            play(splitInput[1],splitInput[2],splitInput[3],serverConn);
                            break;
                        case "change":
                            change(splitInput[1],splitInput[2],splitInput[3],serverConn);
                            break;
                        case "stop":
                            stop(splitInput[1],serverConn);
                            break;
                        default:
                            System.out.println("Wrong input!");
                    }
                }
//                play(120, 0, Song.SONG_C4.getTitle(),serverConn);
//                play(120, 0, Song.SONG_TEST1.getTitle(), serverConn);
//                play(120, 0, Song.SONG_TEST2.getTitle(), serverConn);

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

    private static void stop(String id, Connection serverConn) {
        serverConn.sendMessage(String.join(" ", "stop", id));
    }

    private static void change(String id, String tempo, String transpose, Connection serverConn) {
        String msg = String.join(" ", "change", id, tempo, transpose);
        serverConn.sendMessage(msg);
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

    private static void play(String tempo, String transpose, String title, Connection serverConn) {
        serverConn.sendMessage(String.join(" ","play",tempo,transpose,title));
        Player.play();
    }

}