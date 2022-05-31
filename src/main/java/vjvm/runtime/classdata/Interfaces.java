package vjvm.runtime.classdata;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.var;
import vjvm.runtime.JClass;
import vjvm.runtime.classdata.Interface.Interface;

import java.io.DataInput;

public class Interfaces {
  private final Interface[] interfaces;
  @Getter
  @Setter
  private JClass jClass;

  @SneakyThrows
  public Interfaces(DataInput dataInput, JClass jClass) {
    this.jClass = jClass;
    //开头为计数器（二进制数，范围：0~n-1）
    var count = dataInput.readUnsignedShort();
    interfaces = new Interface[count];
    for (int i = 0; i < count; i++)
      interfaces[i] = new Interface(dataInput, jClass);
  }

  //获取interfaces[index]
  public Interface interf(int index) {
    return interfaces[index];
  }

  //设置interfaces[index]
  public void interf(int index, Interface interf) {
    interfaces[index] = interf;
  }

  //获取属性数目
  public int count() {
    return interfaces.length;
  }
}
