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

