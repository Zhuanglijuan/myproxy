package myproxy;
import java.lang.reflect.Method;
public class $Proxy0 implements myproxy.UserService{
MyInvocationHandler h;
public $Proxy0(MyInvocationHandler h) {
this.h = h;
}
public String excute() throws Throwable {
Method md = myproxy.UserService.class.getMethod("excute",new Class[]{});
return (String)this.h.invoke(this,md,null);
}

}