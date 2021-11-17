package testlib;

import com.sun.jna.Native;

public class Main {

    public static void main(String[] args) {
        BookLibrary bookLibrary = Native.load("/media/sf_Federico/Units/Quinto Anno/Tirocinio/Code/C/JNA_C_Library/libBooks.so", BookLibrary.class);
        BookLibrary.Books.ByReference[] books = (BookLibrary.Books.ByReference[]) new BookLibrary.Books.ByReference().toArray(3);
        books[0].title = "The Fellowship of the Ring";
        books[0].author = "J. R. R. Tolkien";
        books[0].series = "The Lord of the Rings";
        books[0].book_len = 425;
        bookLibrary.printBooks(books);
    }
}
