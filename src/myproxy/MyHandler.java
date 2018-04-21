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
		return "调用成功";
	}
	
	private void before() {
		System.out.println("执行业务代码之前，需要开启事务-->1 step");
	}
	
	private void after() {
		System.out.println("执行业务代码之后，需要关闭事务-->3 step");
	}

}
