package impl;
import java.io.Serializable;

public class Value implements Serializable  {
    MusicFile musicFile;

    public Value() {
    }

    public Value(MusicFile musicFile) {
        this.musicFile = musicFile;
    }

    public MusicFile getMusicFile() {
        return musicFile;
    }

    public void setMusicFile(MusicFile musicFile){
        this.musicFile = musicFile;
    }

    @Override
    public String toString() {
        return "Value{" +
                "musicFile=" + musicFile +
                '}';
    }


}
