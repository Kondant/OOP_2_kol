package org.myshop.music;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlaylistTest {
    @Test
    //Napisz test sprawdzający, czy nowo utworzona playlista jest pusta
    public void testNewPlaylistIsEmpty() {
        Playlist playlist = new Playlist();

        assertTrue(playlist.isEmpty());
    }
//Napisz test sprawdzający, czy po dodaniu jednego utworu playlista ma rozmiar 1.
    @Test
    public void testPlaylistSizeAfterAddingOneSong() {
        Playlist playlist = new Playlist();

        playlist.add(new Song("Abc", "title1", 300));

        assertEquals(1, playlist.size());
    }
//Napisz test sprawdzający, czy po dodaniu jednego utworu, jest w nim ten sam utwór.
    @Test
    public void testPlaylistContainsSongInstance() {
        Playlist playlist = new Playlist();
        Song song = new Song("art1", "title1", 100);

        playlist.add(song);

        assertEquals(song, playlist.getFirst());
    }
//Napisz test sprawdzający, czy po dodaniu jednego utworu, jest w nim taki sam utwór.
    @Test
    public void testPlaylistContainsEquivalentSong() {
        Playlist playlist = new Playlist();
        Song song = new Song("art1", "title1", 100);
        playlist.add(song);

        assertTrue(playlist.contains(new Song("art1", "title1", 100)));
    }
//W klasie Playlist napisz metodę atSecond, która przyjmie całkowitą liczbę sekund i zwróci obiekt Song,
// który jest odtwarzany po tylu sekundach odtwarzania playlisty. Napisz test sprawdzający działanie tej metody.
    @Test
    public void testAtSecondReturnsCorrectSong() {
        Playlist playlist = new Playlist();
        Song s1 = new Song("A", "First", 100);
        Song s2 = new Song("B", "Second", 100);
        Song s3 = new Song("C", "Third", 100);

        playlist.add(s1);
        playlist.add(s2);
        playlist.add(s3);

        assertEquals(s1, playlist.atSecond(0));
        assertEquals(s1, playlist.atSecond(99));
        assertEquals(s2, playlist.atSecond(100));
        assertEquals(s2, playlist.atSecond(199));
        assertEquals(s3, playlist.atSecond(200));
        assertEquals(s3, playlist.atSecond(299));
    }
//Zmodyfikuj metodę atSecond tak, aby rzucała wyjątek IndexOutOfBoundsException,
   // jeżeli zadany czas jest późniejszy niż czas odtwarzania playlisty. Napisz test sprawdzający rzucanie tego wyjątku.
    @Test
    public void testAtSecondTimeTooLongThrowsException(){
        Playlist playlist = new Playlist();
        playlist.add(new Song("A", "Title1", 100));

        IndexOutOfBoundsException e = assertThrows(
                IndexOutOfBoundsException.class,
                () -> playlist.atSecond(100)
        );

        assertEquals("Playlista jest krótsza niż 100 sekund", e.getMessage());
    }



}
