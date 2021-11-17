package testlib;

import com.sun.jna.Library;
import com.sun.jna.Structure;

public interface BookLibrary extends Library {

    @Structure.FieldOrder({"title", "author", "series", "book_len"})
    public static class Books extends Structure{
        public static class ByReference extends Books implements Structure.ByReference {}
        public String title, author, series;
        public int book_len;
    }

    void printBook(Books.ByReference book);
    void printBooks(Books.ByReference[] books);
    double averageBookLen(Books.ByReference[] books);
    int totalBooksLen(Books.ByReference[] books);
}
