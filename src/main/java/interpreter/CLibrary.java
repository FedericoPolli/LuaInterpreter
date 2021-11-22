package interpreter;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

public interface CLibrary extends Library {

    int luaopen_mylib(Pointer L);
}
