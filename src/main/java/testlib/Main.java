package testlib;

import com.sun.jna.Native;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static testlib.BookLibrary.*;

public class Main {

    public static void main(String[] args) {
        final BookLibrary bookLibrary = Native.load("/media/sf_Federico/Units/Quinto Anno/Tirocinio/Code/C/JNA_C_Library/libBooks.so", BookLibrary.class);
        final Book.ByReference book = new Book.ByReference();
        book.title = "The Fellowship of the Ring".getBytes(StandardCharsets.UTF_8);
        book.author = "J. R. R. Tolkien".getBytes(StandardCharsets.UTF_8);
        book.series = "The Lord of the Rings".getBytes(StandardCharsets.UTF_8);
        book.book_len = 425;
        printBookJava(book);
        bookLibrary.printBook(book);

        /*Book.ByReference[] books = (Book.ByReference[]) new Book.ByReference().toArray(3);
        books[0].title = "The Fellowship of the Ring";
        books[0].author = "J. R. R. Tolkien";
        books[0].series = "The Lord of the Rings";
        books[0].book_len = 425;
        books[1].title = "The Two Towers";
        books[1].author = "J. R. R. Tolkien";
        books[1].series = "The Lord of the Rings";
        books[1].book_len = 440;
        books[2].title = "The Return of the King";
        books[2].author = "J. R. R. Tolkien";
        books[2].series = "The Lord of the Rings";
        books[2].book_len = 471;
        printBooksJava(books);
        bookLibrary.printBooks(books, 3);*/
    }

    private static void printBookJava(Book.ByReference book) {
        System.out.println("Book title : " + Native.toString(book.title));
        System.out.println( "Book author : " + Native.toString(book.author));
        System.out.println( "Book series : " + Native.toString(book.series));
        System.out.println( "Book book_len : " + book.book_len);
    }

    private static void printBooksJava(Book.ByReference[] books){
        for (Book.ByReference book : books) {
            printBookJava(book);
        }
    }
}
