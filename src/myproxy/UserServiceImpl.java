package myproxy;

public class UserServiceImpl implements UserService {

	@Override
	public String execute() throws Throwable{
		System.out.println("ִ��UserServiceҵ������,�漰��DML");
		return "ִ��UserServiceҵ������";
	}

}
