package server;

import java.io.Serializable;
import java.util.LinkedList;

public class Song implements Serializable {

    private String title;
    private LinkedList<Note> notes;
    private LinkedList<String> lyrics = new LinkedList<>();

    Song(String title, LinkedList<Note> notes) {
        this.title = title;
        this.notes = notes;
    }

    public LinkedList<String> getLyrics() {
        return lyrics;
    }

    public String getTitle() {
        return title;
    }

    public LinkedList<Note> getNotes() {
        return notes;
    }

    @Override
    public int hashCode() {
        return 31 * 17 + title.hashCode() + notes.hashCode() + lyrics.hashCode();
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
