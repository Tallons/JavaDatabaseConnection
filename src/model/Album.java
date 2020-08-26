package model;

public class Album {
    private int id;
    private String name;
    private int artistId;

    public void setID(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getArtistId() {
        return artistId;
    }
}
