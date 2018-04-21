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

//�ӿ�����:�������п���ʵ�ֶ���ӿ�
public class MyProxy {
	public static String rt = "\r\n";

	/**
	 * �Զ��巵�ش���ʵ���ķ���
	 * 
	 * @param load
	 *            :�������
	 * @param intf
	 *            :�ӿ�����
	 * @param h
	 *            :MyInvocationHandler��ʵ����
	 * @return
	 */
	public static Object newProxyInstance(ClassLoader load, Class<?> intf, MyInvocationHandler h) {
		String path = "D:\\work\\myproxy\\src\\myproxy\\$Proxy0.java";
		// 1. ���ַ�������ʽƴ�ճ��ڴ�����Ĵ�����
		String proxyClass = get$Proxy0(intf);

		// 2. ���ַ������������һ���ļ�����
		outputfile(proxyClass,path);

		// 3.��.java�ļ�����
		complierJava(path);
		
		//4. �Ѷ�Ӧ����������ֽ����ļ����ص�jvm�ڴ�
		return loaderClassToJVM(h);
	}
	
	private static Object loaderClassToJVM(MyInvocationHandler h) {
		MyClassLoader loader = new MyClassLoader("D:\\\\work\\\\myproxy\\\\src\\\\myproxy");
		try {
			//���ص��Ǵ���ʵ���ķ������
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
			//�õ�java������
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			//�õ���׼�ļ�������
			StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(null, null, null);
			//�õ���Ӧ���ļ�����
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
