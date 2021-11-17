package testlib;

import com.sun.jna.Library;

public interface BookLibrary extends Library {
    void printBook();
    void printBooks();
    double averageBookLen();
    int totalBooksLen();
}
