package vjvm.classloader.searchpath;

import vjvm.utils.UnimplementedError;

import java.io.Closeable;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Represents a path to search class files in.
 * A search path may hold resources, such as jar files, so it must implement the Closeable interface.
 * If a subclass doesn't hold any resources, then just do nothing.
 */
public abstract class ClassSearchPath implements Closeable {
  /**
   * Construct search path objects with a given path.
   */
  //                                              此处path实际上是paths
  public static ClassSearchPath[] constructSearchPath(String path) {
    //path.separator指的是多个路径的分隔符(一般是';') file.separator是一个路径中目录和文件的分隔符(Windows是'\')
    String sep = System.getProperty("path.separator");
    //将每个路径分类到Dir和Jar子类中并返回ClassSearchPath[]数组
    ArrayList<ClassSearchPath> searchPaths = new ArrayList<ClassSearchPath>();
    for (String searchPath : path.split(sep)) {
      if (searchPath.endsWith(".jar") || searchPath.endsWith(".JAR"))
        searchPaths.add(new JarSearchPath(searchPath));
      else searchPaths.add(new DirSearchPath(searchPath));
    }
    return searchPaths.toArray(new ClassSearchPath[searchPaths.size()]);
  }
//流的方式
//    return Arrays.stream(path.split(sep)).map(searchPath -> {
//    if (searchPath.endsWith(".jar") || searchPath.endsWith(".JAR"))
//      return new JarSearchPath(searchPath);
//    return new DirSearchPath(searchPath);
//  }).toArray(ClassSearchPath[]::new);

  /**
   * Find a class with specified name.
   *
   * @param name name of the class to find.
   * @return Returns a stream containing the binary data if such class is found, or null if not.
   */
  public abstract InputStream findClass(String name);
}
