package myproxy;

public class MyTest {
	public static void main(String[] args) throws Throwable {
		UserService newProxyInstance = (UserService) MyProxy.newProxyInstance(MyTest.class.getClassLoader(),
				UserService.class, new MyHandler(new UserServiceImpl()));
		newProxyInstance.execute();
	}
}
