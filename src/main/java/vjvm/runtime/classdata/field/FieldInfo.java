package vjvm.runtime.classdata.field;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import vjvm.runtime.JClass;
import vjvm.runtime.classdata.Attributes;
import vjvm.runtime.classdata.ConstantPool;
import vjvm.runtime.classdata.attribute.Attribute;
import vjvm.runtime.classdata.attribute.UnknownAttribute;
import vjvm.runtime.classdata.constant.NameAndTypeConstant;
import vjvm.runtime.classdata.constant.UTF8Constant;
import vjvm.utils.UnimplementedError;

import java.io.DataInput;

import static vjvm.classfiledefs.FieldAccessFlags.*;

@RequiredArgsConstructor
public class FieldInfo {
  private final int accessFlags, nameIndex, descriptorIndex;
  private String name, descriptor;
  private final Attributes attributes;

  private JClass self;

  @SneakyThrows
  public FieldInfo(DataInput dataInput, JClass jClass, ConstantPool constantPool) {
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

  public int attributeCount() {
    return attributes.count();
  }

  public Attribute attribute(int index) {
    return attributes.attribute(index);
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

  public boolean transient_() {
    return (accessFlags & ACC_TRANSIENT) != 0;
  }

  public boolean synthetic() {
    return (accessFlags & ACC_SYNTHETIC) != 0;
  }

  public boolean enum_() {
    return (accessFlags & ACC_ENUM) != 0;
  }
}
