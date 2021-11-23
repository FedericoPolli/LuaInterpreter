package testlib;

import com.sun.jna.Native;

import static testlib.BookLibrary.Book;

public class Main {

    public static void main(String[] args) {
        exerciseLibrary();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void exerciseLibrary() {
        final BookLibrary bookLibrary = Native.load("/media/sf_Federico/Units/Quinto Anno/Tirocinio/Code/C/JNA_C_Library/libBooks.so", BookLibrary.class);
        Book book = new Book();
        /*book.title = getCorrectBytesFromString("The Hobbit");
        book.author = getCorrectBytesFromString("J. R. R. Tolkien");
        book.series = getCorrectBytesFromString("NA");
        book.book_len = 350;
        book.words_per_page = new Memory((long) book.book_len * Native.getNativeSize(Integer.TYPE));
        for (int i = 0; i < book.book_len; i++) {
            book.words_per_page.setInt((long) i *Native.getNativeSize(Integer.TYPE), 150+i%10);
        }
        bookLibrary.printBook(book);
        System.out.println(bookLibrary.totalWords(book));*/

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
        //bookLibrary.printBooks(books, 3);
        //System.out.println(bookLibrary.averageBookLen(books, 3));
        //System.out.println(bookLibrary.totalBooksLen(books, 3));

        bookLibrary.printBooksFromThread(books, 3);
        books[0].title = getCorrectBytesFromString("ND");
        //bookLibrary.printBooksFromThread(books, 3);
    }

    private static byte[] getCorrectBytesFromString(String s) {
        byte[] wholeSpace = new byte[100];
        byte[] content = Native.toByteArray(s);
        System.arraycopy(content, 0, wholeSpace, 0, content.length);
        return wholeSpace;
    }
}
