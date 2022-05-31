package vjvm.runtime.classdata;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.var;
import vjvm.runtime.JClass;
import vjvm.runtime.classdata.method.MethodInfo;

import java.io.DataInput;

public class Methods {
  private final MethodInfo[] methods;
  @Getter
  @Setter
  private JClass jClass;

  @SneakyThrows
  public Methods(DataInput dataInput, JClass jClass, ConstantPool constantPool) {
    this.jClass = jClass;
    //开头为计数器（二进制数，范围：0~n-1）
    var count = dataInput.readUnsignedShort();
    methods = new MethodInfo[count];
    for (int i = 0; i < count; i++)
      methods[i] = new MethodInfo(dataInput, jClass, constantPool);
  }

  //获取methods[index]
  public MethodInfo method(int index) {
    return methods[index];
  }

  //设置methods[index]
  public void method(int index, MethodInfo method) {
    methods[index] = method;
  }

  //获取方法数目
  public int count() {
    return methods.length;
  }
}
