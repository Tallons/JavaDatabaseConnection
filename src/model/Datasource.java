package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Datasource {

    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUM_ARTIST = "artist";
    public static final int INDEX_ALBUM_ID = 1;
    public static final int INDEX_ALBUM_NAME = 2;
    public static final int INDEX_ALBUM_ARTIST = 3;

    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTIST_ID = "_id";
    public static final String COLUMN_ARTIST_NAME = "name";
    public static final int INDEX_ARTIST_ID = 1;
    public static final int INDEX_ARTIST_NAME = 2;

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONG_ID = "id";
    public static final String COLUMN_SONG_TRACK = "track";
    public static final String COLUMN_SONG_TITLE = "title";
    public static final String COLUMN_SONG_ALBUM = "album";
    public static final int INDEX_SONG_ID = 1;
    public static final int INDEX_SONG_TRACK = 2;
    public static final int INDEX_SONG_TITLE = 3;
    public static final int INDEX_SONG_ALBUM = 4;

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    public static final String QUERY_ALBUMS_BY_ARTIST_START = "SELECT " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " FROM " + TABLE_ALBUMS +
            " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." +
            COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + "COLUMN_ARTIST_ID" +
            " WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + " = ";

    public static final String QUERY_ALBUMS_BY_ARTIST_SORT = "ORDER_BY " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";

    public static final String QUERY_ARTIST_FOR_SONG_START = "SELECT " +
            TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
            TABLE_SONGS + "." + COLUMN_SONG_TRACK + " FROM " + TABLE_SONGS +
            " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + "." + COLUMN_SONG_ALBUM +
            " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID +
            " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST +
            " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
            " WHERE " + TABLE_SONGS + "." + COLUMN_SONG_TITLE + " = ";

    public static final String QUERY_ARTIST_FOR_SONG = "ORDER_BY " +
            TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";

    private static final String FILENAME = "resources/dbconfig";
    private ResourceBundle reader = null;
    private Connection conn;

    public boolean open() {

        try {
            reader = ResourceBundle.getBundle(FILENAME);
            conn = DriverManager.getConnection(reader.getString("db.url"),
                    reader.getString("db.username"), reader.getString("db.password"));
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Artist> queryArtist(int sortOrder) {
//      needed if the statement and ResultSet where not inside the try ()
//        Statement statement = null;
//        ResultSet results = null;
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_ARTISTS);
        if (sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(COLUMN_ARTIST_NAME);
            sb.append(" COLLATE NOCASE ");
            if (sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC"); // this is set a the default value
            }
        }

        try (Statement statement = conn.createStatement();
//             ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_ARTISTS)){
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<Artist> artists = new ArrayList<>();
            while (results.next()) {
                Artist artist = new Artist();
                artist.setId(results.getInt(INDEX_ARTIST_ID)); // INDEX is more efficient than COLUMN_ARTIST_ID
                artist.setName(results.getString(INDEX_ARTIST_NAME)); // COLUMN_ARTIST_NAME
                artists.add(artist);
            }

            return artists;

        } catch (SQLException e) {
            System.out.println("Query Failed: " + e.getMessage());
            return null;
        }

        // needed if the statement and ResultSet where not inside the try ().

//        finally {
//            try {
//                if (results != null) {
//                    results.close();
//                }
//            } catch (SQLException e) {
//                System.out.println("Error closing ResultSet: " + e.getMessage());
//            }
//            try {
//                if(statement != null) {
//                    statement.close();
//                }
//            } catch (SQLException e) {
//                System.out.println("Error closing Statement: " + e.getMessage());
//            }
//        }

    }

    public List<String> queryAlbumsForArtist(String artistName, int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_ALBUMS_BY_ARTIST_START);
        sb.append(" \"");
        sb.append(artistName);
        sb.append("\"");

        if (sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ALBUMS_BY_ARTIST_SORT);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        System.out.println("SQL STATEMENT = " + sb.toString());

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())
        ) {
            List<String> albums = new ArrayList<>();
            while (results.next()) {
                albums.add(results.getString(1)); // 1 represents column 1 of the results not table columns
            }

            return albums;

        } catch (SQLException e) {
            System.out.println("Query Failed: " + e.getMessage());
            return null;
        }

    }

    public List<SongArtist> queryArtistsForSong(String songName, int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_ARTIST_FOR_SONG_START);
        sb.append(" \"");
        sb.append(songName);
        sb.append("\"");

        if (sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ALBUMS_BY_ARTIST_SORT);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        System.out.println("SQL STATEMENT = " + sb.toString());

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())
        ) {
            List<SongArtist> songArtists = new ArrayList<>();
            ;
            while (results.next()) {
                SongArtist songArtist = new SongArtist();
                songArtist.setArtistName(results.getString(1));
                songArtist.setAlbumName(results.getString(2));
                songArtist.setTrack(results.getString(3));
                songArtists.add(songArtist);
            }

            return songArtists;

        } catch (SQLException e) {
            System.out.println("Query Failed: " + e.getMessage());
            return null;
        }

    }

    public void querySongsMetadata() {
        String sql = "SELECT * FROM " + TABLE_SONGS;
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sql)
        ) {
            ResultSetMetaData metaData = results.getMetaData();
            int numColumns = metaData.getColumnCount();
            for (int i = 0; i <= numColumns; i++) {
                System.out.format("column %d in the songs table is names %s\n",
                        i, metaData.getColumnName(i));
            }
            }catch(SQLException e){
                System.out.println("Query Failed: " + e.getMessage());
            }
    }

    public List<SongArtist> querySongInfoView (String title) {
        StringBuilder sb = new StringBuilder(QUERY_ARTIST_FOR_SONG_START);
        sb.append(" \"");
        sb.append(title);
        sb.append("\"");

        System.out.println("SQL STATEMENT = " + sb.toString());

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())
        ) {
            List<SongArtist> songArtists = new ArrayList<>();
            ;
            while (results.next()) {
                SongArtist songArtist = new SongArtist();
                songArtist.setArtistName(results.getString(1));
                songArtist.setAlbumName(results.getString(2));
                songArtist.setTrack(results.getString(3));
                songArtists.add(songArtist);
            }

            return songArtists;

        } catch (SQLException e) {
            System.out.println("Query Failed: " + e.getMessage());
            return null;
        }
    }

}



