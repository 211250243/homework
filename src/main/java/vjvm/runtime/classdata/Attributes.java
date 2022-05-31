package vjvm.runtime.classdata;

import lombok.SneakyThrows;
import lombok.var;
import vjvm.runtime.classdata.attribute.Attribute;

import java.io.DataInput;

public class Attributes {
  private final Attribute[] attributes;

  @SneakyThrows
  public Attributes(DataInput dataInput, ConstantPool constantPool) {
    //开头为计数器（二进制数，范围：0~n-1）
    var count = dataInput.readUnsignedShort();
    attributes = new Attribute[count];
    for (int i = 0; i < count; i++)
      attributes[i] = Attribute.constructFromData(dataInput, constantPool);
  }

  //获取attributes[index]
  public Attribute attribute(int index) {
    return attributes[index];
  }

  //设置attributes[index]
  public void attribute(int index, Attribute attribute) {
    attributes[index] = attribute;
  }

  //获取属性数目
  public int count() {
    return attributes.length;
  }
}
