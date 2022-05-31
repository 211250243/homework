package vjvm.runtime.classdata.constant.refConstant;

import vjvm.runtime.JClass;
import vjvm.runtime.classdata.constant.ClassConstant;
import vjvm.runtime.classdata.constant.Constant;
import vjvm.runtime.classdata.constant.NameAndTypeConstant;

public abstract class refConstant extends Constant {
  protected int classIndex, nameAndTypeIndex;
  protected JClass self;
  protected String className, name, descriptor;

  public String className() {
    if (className == null) {
      className = ((ClassConstant) self.constantPool().constant(classIndex)).name();
    }
    return className;
  }

  public String name() {
    if (name == null)
      name = ((NameAndTypeConstant) self.constantPool().constant(nameAndTypeIndex)).name();
    return name;
  }

  public String type() {
    if (descriptor == null)
      descriptor = ((NameAndTypeConstant) self.constantPool().constant(nameAndTypeIndex)).type();
    return descriptor;
  }
}
