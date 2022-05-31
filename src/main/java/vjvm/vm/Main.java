package vjvm.vm;

import lombok.var;
import picocli.CommandLine;
import vjvm.classfiledefs.Descriptors;
import vjvm.runtime.JClass;
import vjvm.utils.UnimplementedError;

import java.util.concurrent.Callable;

import static picocli.CommandLine.*;

@Command(name = "vjvm", mixinStandardHelpOptions = true, version = "vjvm 0.0.1", description = "A toy JVM written in java", subcommands = {
  Run.class, Dump.class})
public class Main implements Callable<Integer> {
  @Option(names = {"-cp",
    "--classpath"}, paramLabel = "CLASSPATH", description = "the class path to search, multiple paths should be separated by ':'")
  String userClassPath = ".";

  public static void main(String[] args) {
    System.exit(new CommandLine(new Main()).execute(args));
  }

  @Override
  public Integer call() {
    CommandLine.usage(this, System.err);
    return -1;
  }
}

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@Command(name = "run", description = "Execute java program")
class Run implements Callable<Integer> {
  @ParentCommand
  private Main parent;

  @Parameters(index = "0", description = "Class to run, e.g. vjvm.vm.Main")
  private String entryClass = "";

  @Parameters(index = "1..*", description = "Arguments passed to java program")
  private String[] args = {};

  @Override
  public Integer call() {
    throw new UnimplementedError("You will implement this in lab 2");
  }

}

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@Command(name = "dump", description = "Dump class file")
class Dump implements Callable<Integer> {
  @ParentCommand
  private Main parent;

  @Parameters(index = "0", description = "Class to dump, e.g. java.lang.String")
  private String className = "";

  @Override
  public Integer call() {
    var ctx = new VMContext(parent.userClassPath);

    // the package vjvm.classfiledefs contains some constants and utility
    // functions that we provided for your convenience
    // please check the individual files for more information
    var descriptor = Descriptors.of(className);

    var clazz = ctx.userLoader().loadClass(descriptor);
    if (clazz == null) {
      // you can print anything to System.err; we won't check it
      System.err.printf("Can not find class %s\n", className);
      return -1;
    }

    dump(clazz);
    return 0;
  }

  private void dump(JClass clazz) {
    //%n 行分隔符(只能用于printf) == System.getProperty("line.separator")
    System.out.printf("class name: %s%n", clazz.thisClass());
    System.out.printf("minor version: %d%n", clazz.minorVersion());
    System.out.printf("major version: %d%n", clazz.majorVersion());
    System.out.printf("flags: 0x%x%n", clazz.accessFlags());
    System.out.printf("this class: %s%n", clazz.thisClass());
    System.out.printf("super class: %s%n%n%n", clazz.superClass());
    System.out.println("constant pool:");
    for (int i = 1; i < clazz.constantPool().size(); i++) {
      if (clazz.constantPool().constant(i) != null)
        System.out.printf("#%d = %s%n", i, clazz.constantPool().constant(i).toString());
    }
    System.out.printf("%ninterfaces:%n");
    for (int i = 0; i < clazz.interfaces().count(); i++)
      System.out.printf("%s%n", clazz.interfaces().interf(i).toString());
    System.out.printf("%nfields:%n");
    for (int i = 0; i < clazz.fields().count(); i++)
      System.out.printf("%s%n", clazz.fields().field(i).toString());
    System.out.printf("%nmethods:%n");
    for (int i = 0; i < clazz.methods().count(); i++)
      System.out.printf("%s%n", clazz.methods().method(i).toString());
  }
}
