package vjvm.runtime.classdata.constant;

import lombok.var;
import lombok.SneakyThrows;

import java.io.DataInput;

public class UnknownConstant extends Constant {
  private final byte[] data;

  @SneakyThrows
  UnknownConstant(DataInput input, int length) {
    data = new byte[length];
    //从输入流中读取len字节并将它们存储到缓冲区阵列data
    input.readFully(data);
  }

  public byte[] value() {
    return data;
  }

  @Override
  public String toString() {
    return "Unknown:";
  }
}
