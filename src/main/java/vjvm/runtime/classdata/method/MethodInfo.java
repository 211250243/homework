package vjvm.runtime.classdata.method;

import lombok.Getter;
import lombok.SneakyThrows;
import vjvm.runtime.JClass;
import vjvm.runtime.classdata.Attributes;
import vjvm.runtime.classdata.ConstantPool;
import vjvm.runtime.classdata.attribute.Attribute;
import vjvm.runtime.classdata.constant.UTF8Constant;
import vjvm.utils.UnimplementedError;

import java.io.DataInput;

import static vjvm.classfiledefs.MethodAccessFlags.*;

public class MethodInfo {
  private final int accessFlags, nameIndex, descriptorIndex;
  private String name, descriptor;
  private final Attributes attributes;

  private JClass self;

  @SneakyThrows
  public MethodInfo(DataInput dataInput, JClass jClass, ConstantPool constantPool) {
    self = jClass;
    accessFlags = dataInput.readUnsignedShort();
    nameIndex = dataInput.readUnsignedShort();
    descriptorIndex = dataInput.readUnsignedShort();
    attributes = new Attributes(dataInput, constantPool);
  }

  public String name() {
    if (name == null)
      name = ((UTF8Constant) self.constantPool().constant(nameIndex)).value();
    return name;
  }

  public String type() {
    if (descriptor == null)
      descriptor = ((UTF8Constant) self.constantPool().constant(descriptorIndex)).value();
    return descriptor;
  }

  @Override
  public String toString() {
    return String.format("%s(0x%x): %s", name(), accessFlags, type());
  }

  public boolean public_() {
    return (accessFlags & ACC_PUBLIC) != 0;
  }

  public boolean private_() {
    return (accessFlags & ACC_PRIVATE) != 0;
  }

  public boolean protected_() {
    return (accessFlags & ACC_PROTECTED) != 0;
  }

  public boolean static_() {
    return (accessFlags & ACC_STATIC) != 0;
  }

  public boolean final_() {
    return (accessFlags & ACC_FINAL) != 0;
  }

  public boolean synchronized_() {
    return (accessFlags & ACC_SYNCHRONIZED) != 0;
  }

  public boolean bridge() {
    return (accessFlags & ACC_BRIDGE) != 0;
  }

  public boolean vaargs() {
    return (accessFlags & ACC_VARARGS) != 0;
  }

  public boolean native_() {
    return (accessFlags & ACC_NATIVE) != 0;
  }

  public boolean abstract_() {
    return (accessFlags & ACC_ABSTRACT) != 0;
  }

  public boolean strict() {
    return (accessFlags & ACC_STRICT) != 0;
  }

  public boolean synthetic() {
    return (accessFlags & ACC_SYNTHETIC) != 0;
  }
}
