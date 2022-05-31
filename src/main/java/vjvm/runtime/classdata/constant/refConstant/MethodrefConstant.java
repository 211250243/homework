package vjvm.runtime.classdata.constant.refConstant;

import lombok.SneakyThrows;
import vjvm.runtime.JClass;

import java.io.DataInput;

public class MethodrefConstant extends refConstant {
  @SneakyThrows
  public MethodrefConstant(DataInput input, JClass jClass) {
    classIndex = input.readUnsignedShort();
    nameAndTypeIndex = input.readUnsignedShort();
    self = jClass;
  }

  @Override
  public String toString() {
    return String.format("Methodref: %s.%s:%s", className(), name(), type());
  }
}
