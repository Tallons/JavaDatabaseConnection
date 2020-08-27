import model.Artist;
import model.Datasource;
import model.SongArtist;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Datasource datasource = new Datasource();
        if (!datasource.open()) {
            System.out.println("couldn't open datasource");
            return;
        }

        List<Artist> artists = datasource.queryArtist(datasource.ORDER_BY_ASC);
        if (artists == null) {
            System.out.println("No Artists found");
            return;
        }
        for (Artist artist : artists) {
            System.out.println("ID = " + artist.getId() + ", Name = " + artist.getName());
        }

        List<String> albumsForArtist = datasource.queryAlbumsForArtist("Iron Maiden", datasource.ORDER_BY_ASC);

        for (String album : albumsForArtist) {
            System.out.println(album);
        }

        List<SongArtist> songArtists = datasource.queryArtistsForSong("Jar of Hearts", datasource.ORDER_BY_ASC);
        if (songArtists == null) {
            System.out.println("Couldn't find the artist");
            return;
        }
        for (SongArtist artist : songArtists) {
            System.out.println("Artist Name = " + artist.getArtistName() + ", Album Name = " + artist.getAlbumName() +
                    " Track = " + artist.getTrack());
        }

        datasource.querySongsMetadata();

        datasource.close();
    }
}
