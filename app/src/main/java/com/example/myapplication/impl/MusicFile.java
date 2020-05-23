package com.example.myapplication.impl;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.mpatric.mp3agic.*;

import java.io.*;

import java.util.Base64;


public class MusicFile  {


        private String trackName;
        private String artistName;
        private String albumInfo;
        private String genre;
        private String musicFileExtract;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public MusicFile(File file, int k) throws InvalidDataException, IOException, UnsupportedTagException {
            Mp3File mp3 = new Mp3File(file);
            if(k==1){
                ID3v1 id3v1Tag = mp3.getId3v1Tag();
                this.trackName = id3v1Tag.getTitle();
                this.artistName = id3v1Tag.getArtist();
                this.genre = id3v1Tag.getGenreDescription();
                this.albumInfo = id3v1Tag.getAlbum();
            }else{
                ID3v2 id3v2Tag = mp3.getId3v2Tag();
                this.trackName = id3v2Tag.getTitle();
                this.artistName = id3v2Tag.getArtist();
                this.genre = id3v2Tag.getGenreDescription();
                this.albumInfo = id3v2Tag.getAlbum();
            }
            this.musicFileExtract = Chunks(file, trackName);
        }

    public MusicFile(String trackName,String artistName,String albumInfo,String genre, String musicFileExtract ){

        this.trackName=trackName;
        this.artistName=artistName;
        this.albumInfo=albumInfo;
        this.genre=genre;
        this.musicFileExtract=musicFileExtract;

    }

        public String getTrackName() {
            return trackName;
        }

        public void setTrackName(String newTrackName) {
            this.trackName = newTrackName;
        }

        public String getArtistName() {
            return artistName;
        }

        public void setArtistName(String newArtistName) {this.artistName = newArtistName; }

        public String getAlbumInfo() {
            return albumInfo;
        }

        public void setAlbumInfo(String newAlbumInfo) {
            this.albumInfo = newAlbumInfo;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String newGenre) {
            this.genre = newGenre;
        }

        public String getMusicFileExtract() {
            return musicFileExtract;
        }

        public void setMusicFileExtract(String newMusicFileExtract) {
            this.musicFileExtract = newMusicFileExtract;
        }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String Chunks(File file, String Name){
            int partCounterName = 1;//Name parts from 001, 002, 003, ...

            int sizeOfChunks = 512 * 1024;// 512KB
            byte[] buffer = new byte[sizeOfChunks];
            String fileName = Name;

            //try-with-resources to ensure closing stream
            try (FileInputStream fIs = new FileInputStream(file);
                 BufferedInputStream bIs = new BufferedInputStream(fIs)) {

                int NumberofBytes = 0;
                while ((NumberofBytes = bIs.read(buffer)) > 1) {
                    //Write splitted chunks in new files with a identifier name.
                    String filePartName = String.format("%s_%03d.mp3", fileName, partCounterName++);
                    File newFile = new File(file.getParent(), filePartName);
                    try (FileOutputStream out = new FileOutputStream(newFile)) {
                        out.write(buffer, 0, NumberofBytes);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String encoded = Base64.getEncoder().encodeToString(buffer);
            return encoded;
        }

    @Override
    public String toString() {

        return "MusicFile{" +
                "trackName='" + trackName + '\'' +
                ", artistName='" + artistName + '\'' +
                ", albumInfo='" + albumInfo + '\'' +
                ", genre='" + genre + '\'' +
                ", musicFileExtract=" + musicFileExtract +
                '}';
    }
//Arrays.toString(musicFileExtract)
    }

