package testlib;

import com.sun.jna.Native;

public class Main {

    public static void main(String[] args) {
        BookLibrary bookLibrary = Native.load("/media/sf_Federico/Units/Quinto Anno/Tirocinio/Code/C/JNA_C_Library/libBooks.so", BookLibrary.class);
        bookLibrary.printBook();
    }
}
