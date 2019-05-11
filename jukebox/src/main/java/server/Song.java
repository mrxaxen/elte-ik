package server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class Song implements Serializable {

    public static final Song SONG_C4 = new Song("c4", "C 4");
    public static final Song SONG_TEST1 = new Song("test1", "C 4 E 4 C 4 E 4 G 8 G 8 REP 6;1 C/1 4 B 4 A 4 G 4 F 8 A 8 G 4 F 4 E 4 D 4 C 8 C 8");
    public static final Song SONG_TEST2 = new Song("test2", "D 1 D 3 D/1 1 D/1 3 C/1 1 C/1 3 C/1 2 C/1 2 D/1 1 D/1 3 C/1 1 Bb 3 A 4 A 2 R 2 REP 15;1 " +
            "Bb 4 A 2 G 2 F 1 F 3 E 2 D 2 G 2 G 2 C/1 2 Bb 2 A 4 D/1 2 R 2 C/1 1 Bb 3 " +
            "A 2 G 2 G 1 A 3 G 2 F 2 A 1 G 3 F# 2 Eb 2 D 4 D 2 R 2");
    public static final Song SONG_MEGALOVANIA_MAIN_BASE = new Song("megalovania",
            "D 1 D 1 D/1 1 R 1 A 1 R 2 G# 1 R 1 G 1 R 1 F 2 D 1 F 1 G 1 " +
                    "C 1 C 1 D/1 1 R 1 A 1 R 2 G# 1 R 1 G 1 R 1 F 2 D 1 F 1 G 1 " +
                    "B/-1 1 B/-1 1 D/1 1 R 1 A 1 R 2 G# 1 R 1 G 1 R 1 F 2 D 1 F 1 G 1 " +
                    "A#/-1 1 A#/-1 1 D/1 1 R 1 A 1 R 2 G# 1 R 1 G 1 R 1 F 2 D 1 F 1 G 1 REP 56;3");
    public static final Song SONG_MEGALOVANIA_MAIN_RAISED = new Song("megalovania_raised","R 64 D/1 1 D/1 1 D/2 1 R 1 A/1 1 R 2 G#/1 1 R 1 G/1 1 R 1 F/1 2 D/1 1 F/1 1 G/1 1 " +
            "C/1 1 C/1 1 D/2 1 R 1 A/1 1 R 2 G#/1 1 R 1 G/1 1 R 1 F/1 2 D/1 1 F/1 1 G/1 1 " +
            "B 1 B 1 D/2 1 R 1 A/1 1 R 2 G#/1 1 R 1 G/1 1 R 1 F/1 2 D/1 1 F/1 1 G/1 1 " +
            "A# 1 A# 1 D/2 1 R 1 A/1 1 R 2 G#/1 1 R 1 G/1 1 R 1 F/1 2 D/1 1 F/1 1 G/1 1 REP 56;2");
    public static final Song SONG_MEGALOVANIA_BASE_BASS = new Song("megalovania_bass",
            "R 64 R 32 R 24 R 2 D#/-1 2 D#/-1 2 R 2 G/-2 2 E/-2 2 G/-2 2 E/-2 2 REP 4;6 " +
                    "E/-2 2 REP 1;3 G/-2 2 E/-2 2 G/-2 2 E/-2 2 REP 4;6 E/-2 2 REP 1;3");
    public static final Song SONG_MEGALOVANIA_BASE_UPPER = new Song("megalovania_base_upper",
            "R 64 D 4 D 4 D 4 D 4 C 4 C 4 C 4 C 4 B 4 B 4 B 4 B 4 A# 4 A# 4 C 4 C 4 REP 16;3");
    public static final Song SONG_MEGALOVANIA_BASE_LOWER = new Song("megalovania_base_lower",
            "R 64 D/-1 4 D/-1 4 D/-1 4 D/-1 4 C/-1 4 C/-1 4 C/-1 4 C/-1 4 " +
                    "B/-1 4 B/-1 4 B/-1 4 B/-1 4 A#/-1 4 A#/-1 4 C/-1 4 C/-1 4 REP 16;3");

    private static final long serialVersionUID = 2691211006487623404L;
    private String title;
    private ArrayList<Note> notes;
    private ArrayList<String> lyrics = new ArrayList<>();
    private ArrayList<Note> transposed;

    public Song(String title) {
        this.title = title;
    }

    public Song(String title, String notes) {
        this.title = title;
        this.notes = initNotes(notes);
        initLyrics();
    }
    private static int wordCount = 0;
    private void initLyrics() {
        notes.forEach((note)-> {
            if(note.note.equals("REP")) {
                wordCount += Integer.parseInt(note.length.split(";")[0])*
                Integer.parseInt(note.length.split(";")[1]);
            } else {
                wordCount++;
            }
        });

        while(wordCount != 0) {
            lyrics.add("???");
            wordCount--;
        }
    }

    private ArrayList<Note> initNotes(String notes) {
        ArrayList<Note> ns = new ArrayList<>();
        String[] msgSplit = notes.split(" ");
        String note = null;
        String length;

        try {
            for (int i = 0; i < msgSplit.length; i++) {
                if (i % 2 == 0) {
                    note = msgSplit[i];
                } else {
                    length = msgSplit[i];
                    if (note.equals("R")) {
                        ns.add(new Note(note, length));
                    } else {
                        ns.add(new Note(note, length));
                    }
                    if (note == null) {
                        throw new RuntimeException("NullNote!");
                    }
                }
            }
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
        return ns;
    }

    public void addLyrics(String msg) {
        String[] lyr = msg.split(" ");
        int i = 0;
        for(String s : lyr){
            lyrics.set(i,s);
            i++;
        }
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getLyrics() {
        return lyrics;
    }

    public ArrayList<Note> transposeSong(int transpose) {
        if(transpose != 0) {
            transposed = new ArrayList<>();
            notes.forEach((note)-> {
                Note newNote = new Note(note.note,note.length);
                transposed.add(newNote);
            });
            transposed.replaceAll((note)-> {
                Note newNote = new Note(note.note,note.length);
                String noteString = newNote.note;
                int transp = 0;
                if(newNote.note.contains("/")) {
                    noteString = newNote.note.split("/")[0];
                    transp = Integer.parseInt(newNote.note.split("/")[1]) + transpose;
                    if (transp != 0) {
                        newNote.note = noteString + "/" + transp;
                    } else {
                        return note;
                    }
                } else {
                    noteString = newNote.note + "/" + transpose;
                    newNote.note = noteString;
                }
                return newNote;
            });
            return transposed;
        } else {
            return notes;
        }
    }

    @Override
    public int hashCode() {
        return 31 * 17 + title.hashCode() + notes.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o instanceof Song) {
            Song s = (Song) o;
            return s.title.equals(this.title);
        }
        return false;
    }
}
