package training;

import java.lang.classfile.ClassFile;
import java.lang.constant.MethodTypeDesc;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.classfile.ClassFile.ACC_PUBLIC;
import static java.lang.classfile.ClassFile.ACC_STATIC;
import static java.lang.constant.ClassDesc.of;
import static java.lang.constant.ConstantDescs.CD_String;
import static java.lang.constant.ConstantDescs.CD_void;

public class ClassFileApiDemo {

    static void main() throws Throwable {
        var system = of("java.lang", "System");
        var printStream = of("java.io", "PrintStream");

        ClassFile.of().buildTo(
                Path.of("Echo.class"),
                of("training", "Echo"),
                classBuilder -> classBuilder
                        .withMethodBody(
                                "main",
                                MethodTypeDesc.of(CD_void, CD_String.arrayType()),
                                ACC_PUBLIC | ACC_STATIC,
                                codeBuilder -> codeBuilder
                                        .getstatic(system, "out", printStream)
                                        .aload(codeBuilder.parameterSlot(0))
                                        .iconst_0()
                                        .aaload()
                                        .invokevirtual(printStream, "println", MethodTypeDesc.of(CD_void, CD_String))
                                        .return_()));

        // Reflection
        // Class.forName("training.Echo");

        byte[] bytes = Files.readAllBytes(Path.of("Echo.class"));
        var lookup = MethodHandles.lookup().defineHiddenClass(bytes, true);

        var main = lookup.findStatic(
                lookup.lookupClass(),
                "main",
                MethodType.methodType(void.class, String[].class));

        main.invokeExact(new String[] { "hello" });
    }
}
