package testlib;

import com.sun.jna.Native;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static testlib.BookLibrary.*;

public class Main {

    public static void main(String[] args) {
        final BookLibrary bookLibrary = Native.load("/media/sf_Federico/Units/Quinto Anno/Tirocinio/Code/C/JNA_C_Library/libBooks.so", BookLibrary.class);
        Book book = new Book();
        /*book.title = getCorrectBytesFromString("The Fellowship of the Ring");
        book.author = getCorrectBytesFromString("J. R. R. Tolkien");
        book.series = getCorrectBytesFromString("The Lord of the Rings");
        book.book_len = 425;
        bookLibrary.printBook(book);*/

        Book[] books = (Book[]) book.toArray(3);
        books[0].title = getCorrectBytesFromString("The Fellowship of the Ring");
        books[0].author = getCorrectBytesFromString("J. R. R. Tolkien");
        books[0].series = getCorrectBytesFromString("The Lord of the Rings");
        books[0].book_len = 425;
        books[1].title = getCorrectBytesFromString("The Two Towers");
        books[1].author = getCorrectBytesFromString("J. R. R. Tolkien");
        books[1].series = getCorrectBytesFromString("The Lord of the Rings");
        books[1].book_len = 440;
        books[2].title = getCorrectBytesFromString("The Return of the King");
        books[2].author = getCorrectBytesFromString("J. R. R. Tolkien");
        books[2].series = getCorrectBytesFromString("The Lord of the Rings");
        books[2].book_len = 471;
        bookLibrary.printBooks(books, 3);
        System.out.flush();
        System.out.println(bookLibrary.averageBookLen(books, 3));
        System.out.println(bookLibrary.totalBooksLen(books, 3));
    }

    private static byte[] getCorrectBytesFromString(String s) {
        byte[] wholeSpace = new byte[100];
        byte[] content = Native.toByteArray(s);
        System.arraycopy(content, 0, wholeSpace, 0, content.length);
        return wholeSpace;
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
