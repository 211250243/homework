package vjvm.classloader.searchpath;

import lombok.var;
import lombok.SneakyThrows;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.jar.JarFile;

public class JarSearchPath extends ClassSearchPath {
  //如果文件不存在，为空null
  private JarFile jarFile;

  @SneakyThrows //@SneakyThrows为代码生成一个try...catch，抛出异常
  public JarSearchPath(String name) {
    try {
      jarFile = new JarFile(name);
    } catch (FileNotFoundException e) {
      jarFile = null;
    }
  }

  @Override
  @SneakyThrows
  public InputStream findClass(String name) {
    if (jarFile == null)
      return null;
    //getEntry返回ZipEntry表示ZIP文件条目(即文件列表)
    var entry = jarFile.getEntry(name + ".class");
    return entry == null ? null : jarFile.getInputStream(entry);
  } //JarInputStream类用于从任何输入流读取JAR文件的内容

  @Override
  @SneakyThrows
  public void close() {
    if (jarFile != null)
      jarFile.close();
  }
}
