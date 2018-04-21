package myproxy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MyClassLoader extends ClassLoader {

	private File dir;
	
	public MyClassLoader(String path) {
		dir = new File(path);
		
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if(dir != null) {
			File clazzFile = new File(dir,name + ".class");
			if(clazzFile.exists()) {
				FileInputStream input;
				try {
					input = new FileInputStream(clazzFile);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int len;
					while((len = input.read(buffer)) != -1 ) {
						baos.write(buffer,0,len);
					}
					//把字节流里面的内容加载到内存
					return defineClass("myproxy." + name,baos.toByteArray(),0,baos.size());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return super.findClass(name);
	}

}
