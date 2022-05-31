package vjvm.runtime;

import vjvm.classloader.JClassLoader;
import vjvm.runtime.classdata.*;
import vjvm.runtime.classdata.field.FieldInfo;
import vjvm.runtime.classdata.constant.ClassConstant;
import vjvm.runtime.classdata.method.MethodInfo;

import java.io.DataInput;
import java.io.InvalidClassException;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.var;

import static vjvm.classfiledefs.ClassAccessFlags.*;

public class JClass {
  @Getter
  private final JClassLoader classLoader;
  @Getter
  private final int minorVersion;
  @Getter
  private final int majorVersion;
  @Getter
  private final ConstantPool constantPool;
  @Getter
  private final int accessFlags;
  @Getter
  private final String thisClass;
  @Getter
  private final String superClass;
  @Getter
  private final Interfaces interfaces;
  @Getter
  private final Fields fields;
  @Getter
  private final Methods methods;
  @Getter
  private final Attributes attributes;

  @SneakyThrows
  public JClass(DataInput dataInput, JClassLoader classLoader) {
    this.classLoader = classLoader;

    // check magic number
    var magic = dataInput.readInt();
    if (magic != 0xcafebabe) {
      throw new InvalidClassException(String.format(
        "Wrong magic number, expected: 0xcafebabe, got: 0x%x", magic));
    }
    //short,int,long分别占2,4,8个字节(u2,u4,u8)
    //read均为读取二进制数，转不同类型
    minorVersion = dataInput.readUnsignedShort();
    majorVersion = dataInput.readUnsignedShort();
    constantPool = new ConstantPool(dataInput, this);
    accessFlags = dataInput.readUnsignedShort();//类或接口的访问权限
    thisClass = ((ClassConstant) constantPool.constant(dataInput.readUnsignedShort())).name();
    superClass = ((ClassConstant) constantPool.constant(dataInput.readUnsignedShort())).name();
    interfaces = new Interfaces(dataInput, this);
    fields = new Fields(dataInput, this, constantPool);
    methods = new Methods(dataInput, this, constantPool);
    attributes = new Attributes(dataInput, constantPool);
  }

  public boolean public_() {
    return (accessFlags & ACC_PUBLIC) != 0;
  }

  public boolean final_() {
    return (accessFlags & ACC_FINAL) != 0;
  }

  public boolean super_() {
    return (accessFlags & ACC_SUPER) != 0;
  }

  public boolean interface_() {
    return (accessFlags & ACC_INTERFACE) != 0;
  }

  public boolean abstract_() {
    return (accessFlags & ACC_ABSTRACT) != 0;
  }

  public boolean synthetic() {
    return (accessFlags & ACC_SYNTHETIC) != 0;
  }

  public boolean annotation() {
    return (accessFlags & ACC_ANNOTATION) != 0;
  }

  public boolean enum_() {
    return (accessFlags & ACC_ENUM) != 0;
  }

  public boolean module() {
    return (accessFlags & ACC_MODULE) != 0;
  }

  public int fieldsCount() {
    return fields.count();
  }

  public FieldInfo field(int index) {
    return fields.field(index);
  }

  public int methodsCount() {
    return methods.count();
  }

  public MethodInfo method(int index) {
    return methods.method(index);
  }

}
