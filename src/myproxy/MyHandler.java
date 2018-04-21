package myproxy;

import java.lang.reflect.Method;

public class MyHandler implements MyInvocationHandler {
	
	private UserService userService;
	
	public MyHandler(UserService userSerivice) {
		this.userService = userSerivice;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		before();
		method.invoke(userService, args);
		after();
		return "���óɹ�";
	}
	
	private void before() {
		System.out.println("ִ��ҵ�����֮ǰ����Ҫ��������-->1 step");
	}
	
	private void after() {
		System.out.println("ִ��ҵ�����֮����Ҫ�ر�����-->3 step");
	}

}
