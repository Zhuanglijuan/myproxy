package myproxy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

//接口数组:代理类有可能实现多个接口
public class MyProxy {
	public static String rt = "\r\n";

	/**
	 * 自定义返回代理实例的方法
	 * 
	 * @param load
	 *            :类加载器
	 * @param intf
	 *            :接口数组
	 * @param h
	 *            :MyInvocationHandler的实现类
	 * @return
	 */
	public static Object newProxyInstance(ClassLoader load, Class<?> intf, MyInvocationHandler h) {
		String path = "D:\\work\\myproxy\\src\\myproxy\\$Proxy0.java";
		// 1. 用字符串的形式拼凑出内存里面的代理类
		String proxyClass = get$Proxy0(intf);

		// 2. 把字符串的类输出到一个文件里面
		outputfile(proxyClass,path);

		// 3.把.java文件编译
		complierJava(path);
		
		//4. 把对应编译出来的字节码文件加载到jvm内存
		return loaderClassToJVM(h);
	}
	
	private static Object loaderClassToJVM(MyInvocationHandler h) {
		MyClassLoader loader = new MyClassLoader("D:\\\\work\\\\myproxy\\\\src\\\\myproxy");
		try {
			//返回的是代理实例的反射对象
			Class<?> proxy0Class = loader.findClass("$Proxy0");
			
			try {
				Constructor<?> constructor = proxy0Class.getConstructor(MyInvocationHandler.class);
				Object newInstance = constructor.newInstance(h);
				
				return newInstance;
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void complierJava(String fileName) {
		try {
			//得到java编译器
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			//得到标准文件管理器
			StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(null, null, null);
			//得到对应的文件对象
			Iterable<? extends JavaFileObject> javaFileObjects = standardFileManager.getJavaFileObjects(fileName);
			
			CompilationTask task = compiler.getTask(null, standardFileManager, null, null, null, javaFileObjects);
			task.call();
			
			standardFileManager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void outputfile(String proxyClass,String path) {
		
		File f = new File(path);
		try {
			FileWriter fw = new FileWriter(f);
			fw.write(proxyClass);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String get$Proxy0(Class<?> intf) {
		Method[] methods = intf.getMethods();
		String proxyClass = "package myproxy;" + rt + "import java.lang.reflect.Method;" + rt
				+ "public class $Proxy0 implements " + intf.getName() + "{" + rt + "MyInvocationHandler h;" + rt
				+ "public $Proxy0(MyInvocationHandler h) {" + rt 
				+ "this.h = h;" + rt + "}" + rt
				+ getMethodString(methods, intf) + rt 
				+ "}";
		return proxyClass;
	}

	public static String getMethodString(Method[] methods, Class<?> intf) {
		String proxyMe = "";
		for (Method method : methods) {
			proxyMe += "public String " + method.getName() + "() throws Throwable {" + rt 
					+ "Method md = "
					+ intf.getName() + ".class.getMethod(\"" + method.getName() + "\",new Class[]{});" + rt
					+ "return (String)this.h.invoke(this,md,null);" + rt + "}" + rt;
		}
		return proxyMe;
	}
}
