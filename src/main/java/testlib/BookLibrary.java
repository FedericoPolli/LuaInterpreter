package testlib;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public interface BookLibrary extends Library {

    @Structure.FieldOrder({"title", "author", "series", "book_len", "words_per_page"})
    class Book extends Structure {
        public static class ByReference extends Book implements Structure.ByReference {}
        public byte[] title = new byte[100];
        public byte[] author = new byte[100];
        public byte[] series = new byte[100];
        public int book_len;
        public Pointer words_per_page; // int
    }

    void printBook(Book book);
    void printBooks(Book[] books, int numOfBooks);
    double averageBookLen(Book[] books, int numOfBooks);
    int totalBooksLen(Book[] books, int numOfBooks);
    int totalWords(Book book);
}
