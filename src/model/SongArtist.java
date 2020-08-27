package model;

public class SongArtist {

    private String artistName;
    private String albumName;
    private String track;

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getTrack() {
        return track;
    }
}
