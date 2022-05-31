package vjvm.runtime.classdata;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.var;
import vjvm.runtime.JClass;
import vjvm.runtime.classdata.field.FieldInfo;

import java.io.DataInput;

public class Fields {
  private final FieldInfo[] fields;
  @Getter
  @Setter
  private JClass jClass;

  @SneakyThrows
  public Fields(DataInput dataInput, JClass jClass, ConstantPool constantPool) {
    this.jClass = jClass;
    //开头为计数器（二进制数，范围：0~n-1）
    var count = dataInput.readUnsignedShort();
    fields = new FieldInfo[count];
    for (int i = 0; i < count; i++)
      fields[i] = new FieldInfo(dataInput, jClass, constantPool);
  }

  //获取fields[index]
  public FieldInfo field(int index) {
    return fields[index];
  }

  //设置fields[index]
  public void field(int index, FieldInfo field) {
    fields[index] = field;
  }

  //获取成员变量数目
  public int count() {
    return fields.length;
  }
}

