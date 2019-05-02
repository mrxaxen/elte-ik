package server;

public class Note {
    String note;
    int length;

    Note(String note, int length) {
        this.note = note;
        this.length = length;
    }

    @Override
    public String toString() {
        return note + " " + length;
    }
}
