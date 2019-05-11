package server;

import java.util.HashMap;

public class Note {

    private static final HashMap<String, Integer> noteConversionByStr = initConvStr();
    private static final HashMap<Integer, String> noteConversionByInt = initConvInt();
    String note;
    String length;

    Note(String note, String length) {
        this.note = note;
        this.length = length;
    }

    private static HashMap<Integer,String> initConvInt() {
        HashMap<Integer,String> hashMap = new HashMap<>();
        noteConversionByStr.forEach((key,value)->{
            hashMap.put(value, key);
        });
        return hashMap;
    }

    private static HashMap<String, Integer> initConvStr() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("FIN", -200);
        hashMap.put("R",-100);
        hashMap.put("C",60);
        hashMap.put("C#",61);
        hashMap.put("Db",61);
        hashMap.put("D",62);
        hashMap.put("D#",63);
        hashMap.put("Eb",63);
        hashMap.put("E",64);
        hashMap.put("F",65);
        hashMap.put("F#",66);
        hashMap.put("Gb",66);
        hashMap.put("G",67);
        hashMap.put("G#",68);
        hashMap.put("Ab",68);
        hashMap.put("A",69);
        hashMap.put("A#",70);
        hashMap.put("Bb",70);
        hashMap.put("B",71);
        return hashMap;
    }

    public static String getNoteByInt(int midiCode) {
        return noteConversionByInt.get(midiCode);
    }

    public static int getNoteByStr(String note) {
//        System.out.println("Getnote: "+ note);
        return noteConversionByStr.get(note);
    }

    @Override
    public int hashCode() {
        return 31 * 17 + note.hashCode() + length.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(o == this) return true;
        if(o instanceof Note) {
            Note n = (Note) o;
            return (n.note.equals(this.note) && n.length.equals(this.length));
        }
        return false;
    }

    @Override
    public String toString() {
        return note + " " + length;
    }
}
