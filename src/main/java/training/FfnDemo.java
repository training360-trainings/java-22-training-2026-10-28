package training;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;

public class FfnDemo {

    static void main() throws Throwable {
        Linker linker = Linker.nativeLinker();
        MethodHandle strlen = linker.downcallHandle(
                linker.defaultLookup().find("strlen").orElseThrow(),
                FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment s = arena.allocateFrom("Hello FFM");
            long len = (long) strlen.invokeExact(s);
            System.out.println("Length: " + len);
        }
    }
}
