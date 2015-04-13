thrift-generator
================

Creating a thrift file via a Java interface

## example
```java
public interface ICommonUserService {

	public User login(int id, String name);
	
	public User getUserById(long id);
	
	public boolean saveUser(User user);
	
	public List<User> getUserIds(long id); 
	
	public Map<Long, User> getUserByIds(List<User> ids);
	
	public Map<String, List<User>> getUsersByName(List<String> names);

	public Map<Long, List<Long>> getGroupUsers(List<String> names, List<User> userList, List<Long> lns, long ll);
	
	public List<String> testCase1(Map<Integer,String> num1, List<User> num2, List<String> num3, long num4, String num5);
}
```
```java

public class ThriftStructBuilderTest {

	private ThriftFileBuilder fileBuilder = new ThriftFileBuilder();
	
	@Test
	public void toOutputstream() throws Exception {
		this.fileBuilder.buildToOutputStream(ICommonUserService.class, System.out);
	}
	
}
```
### 执行之后会在控制台输入如下: 
```mvn test -Dtest=com.sohu.thrift.generator.builder.ThriftStructBuilderTest```
```thrift

	namespace java com.sohu.thrift.generator.test.thrift

	enum Status {
			NORMAL = 0,
			BLOCKED = 1,
			$VALUES = 2
	}

	struct Account {
			1:i32 id,
			2:string name
	}
	struct User {
			1:i32 id,
			2:string name,
			3:bool sex,
			4:Status status,
			5:list<i64> ids,
			6:Account account
	}

	service ICommonUserService {
		 	list<User> getUserIds(1:i64 id),
		 	User login(1:i32 id,2:string name),
		 	map<string, list<User>> getUsersByName(1:list<string> names),
		 	map<i64, list<i64>> getGroupUsers(1:list<string> names,2:list<User> userList,3:list<i64> lns,4:i64 ll),
		 	map<i64, User> getUserByIds(1:list<User> ids),
		 	list<string> testCase1(1:map<i32, string> num1,2:list<User> num2,3:list<string> num3,4:i64 num4,5:string num5),
		 	User getUserById(1:i64 id),
		 	bool saveUser(1:User user)
	}


```
