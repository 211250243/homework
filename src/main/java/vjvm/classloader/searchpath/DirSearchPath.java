package vjvm.classloader.searchpath;

import lombok.var;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class DirSearchPath extends ClassSearchPath {
  private final Path searchPath;

  public DirSearchPath(String path) {
    //getDefault返回默认文件系统，getPath接受一个或多个字符串，并用目录/文件分隔符连接起来构成一个路径Path。
    searchPath = FileSystems.getDefault().getPath(path);
    //isDirectory()判断是否是目录
    assert !searchPath.toFile().exists() || searchPath.toFile().isDirectory();
  }

  @Override
  public InputStream findClass(String name) {
    try {
      // 查路径名+类名+.class,  resolve将之拼接成文件名
      return new FileInputStream(searchPath.resolve(name + ".class").toFile());
    } catch (FileNotFoundException e) {
      return null;
    }
  }

  @Override
  public void close() {
    //无需关闭
  }
}
