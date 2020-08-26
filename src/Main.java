import model.Artist;
import model.Datasource;

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


        datasource.close();
    }
}
