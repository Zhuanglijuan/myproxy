package myproxy;

public class UserServiceImpl implements UserService {

	@Override
	public String execute() throws Throwable{
		System.out.println("执行UserService业务层代码,涉及到DML");
		return "执行UserService业务层代码";
	}

}
