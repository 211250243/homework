package vjvm.runtime.classdata;

import lombok.var;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import vjvm.runtime.JClass;
import vjvm.runtime.classdata.constant.Constant;

import java.io.DataInput;

public class ConstantPool {
  // runtime constants are stored here
  private final Constant[] constants;
  @Getter
  @Setter
  private JClass jClass;

  /**
   * Constructs a runtime constant pool from binary data
   *
   * @param dataInput stream of data, contents of this constant pool will be read from stream
   * @param jClass    the class this pool belongs to
   */
  @SneakyThrows
  public ConstantPool(DataInput dataInput, JClass jClass) {
    this.jClass = jClass;
    //开头为常量池计数器（二进制数，范围：1~n-1）
    var count = dataInput.readUnsignedShort();
    constants = new Constant[count];
    for (int i = 1; i < count; ) {
      //constructFromData给此数据分类并返回
      var r = Constant.constructFromData(dataInput, jClass);
      constants[i] = r.getLeft();
      i += r.getRight();
    }
  }

  /**
   * Gets a constant at index
   *
   * @param index the index of the constant
   * @return the constant in the pool
   */
  //获取常量池constant[index]
  public Constant constant(int index) {
    return constants[index];
  }

  //设置常量池constant[index]
  public void constant(int index, Constant constant) {
    constants[index] = constant;
  }

  //获取常量池大小
  public int size() {
    return constants.length;
  }
}
