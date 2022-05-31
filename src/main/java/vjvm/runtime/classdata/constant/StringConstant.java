package vjvm.runtime.classdata.constant;

import lombok.SneakyThrows;
import vjvm.runtime.JClass;

import java.io.DataInput;

public class StringConstant extends Constant {
  private final int nameIndex;
  private final JClass self;
  private String value;

  @SneakyThrows
  StringConstant(DataInput input, JClass self) {
    nameIndex = input.readUnsignedShort();
    this.self = self;
  }

  public String name() {
    if (value == null) {
      value = ((UTF8Constant) self.constantPool().constant(nameIndex)).value();
    }
    return value;
  }

  @Override
  public String toString() {
    return String.format("String: %s", name());
  }
}
