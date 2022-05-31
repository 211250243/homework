package vjvm.classloader;

import lombok.var;
import lombok.Getter;
import lombok.SneakyThrows;
import vjvm.classloader.searchpath.ClassSearchPath;
import vjvm.runtime.JClass;
import vjvm.vm.VMContext;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.HashMap;

public class JClassLoader implements Closeable {
  private final JClassLoader parent;
  private final ClassSearchPath[] searchPaths;
  private final HashMap<String, JClass> definedClass = new HashMap<>();
  @Getter
  private final VMContext context;

  public JClassLoader(JClassLoader parent, ClassSearchPath[] searchPaths, VMContext context) {
    this.context = context;
    this.parent = parent;
    this.searchPaths = searchPaths;
  }

  /**
   * Load class
   * <p>
   * If a class is found, construct it using the data returned by ClassSearchPath.findClass and return it.
   * <p>
   * Otherwise, return null.
   */
  public JClass loadClass(String descriptor) {
    //描述符为'L' + name.replace('.', '/') + ';'
    assert descriptor.charAt(descriptor.length() - 1) == ';';

    JClass jClass;
//最初都是 3 起作用，然后将类放入defined中
    //1.亲代加载
    if (parent != null && (jClass = parent.loadClass(descriptor)) != null)
      return jClass;

    //2.子代加载（即已加载defined）
    if ((jClass = definedClass.get(descriptor)) != null)
      return jClass;

    //3.未加载（需搜索路径）                  去'L'和';'
    var name = descriptor.substring(1, descriptor.length() - 1);
    //此处searchPaths可以看做系统所有路径
    for (var p : searchPaths) {
      //findClass返回class类文件的文件流
      var iStream = p.findClass(name);
      //如果发现类
      if (iStream != null)
        return defineNonarrayClass(descriptor, iStream);
    }

    return null;
  }

  @Override
  @SneakyThrows
  public void close() {
    for (var s : searchPaths)
      s.close();
  }

  private JClass defineNonarrayClass(String descriptor, InputStream data) {
    //构建新JClass类
    var ret = new JClass(new DataInputStream(data), this);
    //加入已加载类defined中
    definedClass.put(descriptor, ret);
    return ret;
  }
}
