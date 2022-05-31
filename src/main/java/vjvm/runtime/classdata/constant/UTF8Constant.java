package vjvm.runtime.classdata.constant;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.text.StringEscapeUtils;

import java.io.DataInput;

@Getter
public class UTF8Constant extends Constant {
  private final String value;

  @SneakyThrows
    //readUTF读取使用 modified UTF-8格式编码的字符串
  UTF8Constant(DataInput input) {
    value = input.readUTF();
  }

  @Override
  public String toString() {
    //StringEscapeUtils.escapeJava转义String使用 Java 字符串规则中的字符
    return String.format("Utf8: \"%s\"", StringEscapeUtils.escapeJava(value));
  }
}
