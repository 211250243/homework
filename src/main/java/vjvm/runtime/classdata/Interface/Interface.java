package vjvm.runtime.classdata.Interface;

import lombok.SneakyThrows;
import vjvm.runtime.JClass;
import vjvm.runtime.classdata.constant.ClassConstant;

import java.io.DataInput;

public class Interface {
  private final int index;
  private final ClassConstant interf;
  private final JClass self;
  private String name;

  @SneakyThrows
  public Interface(DataInput input, JClass jClass) {
    index = input.readUnsignedShort();
    self = jClass;
    interf = (ClassConstant) self.constantPool().constant(index);
  }

  public String name() {
    name = interf.name();
    return name;
  }

  @Override
  public String toString() {
    return String.format("%s", name());
  }
}
