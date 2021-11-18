package testlib;

import com.sun.jna.Library;
import com.sun.jna.Structure;

public interface BookLibrary extends Library {

    @Structure.FieldOrder({"title", "author", "series", "book_len"})
    class Book extends Structure {
        public static class ByReference extends Book implements Structure.ByReference {}
        public byte[] title = new byte[20];
        public byte[] author = new byte[20];
        public byte[] series = new byte[20];
        public int book_len;
    }

    void printBook(Book.ByReference book);
    void printBooks(Book.ByReference[] books, int numOfBooks);
    double averageBookLen(Book.ByReference[] books, int numOfBooks);
    int totalBooksLen(Book.ByReference[] books, int numOfBooks);
}
