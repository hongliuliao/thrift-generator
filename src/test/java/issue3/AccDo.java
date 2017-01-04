package issue3;

import com.sohu.thrift.generator.test.User;

public class AccDo extends User {

	private String testExtends;

	public String getTestExtends() {
		return testExtends;
	}

	public void setTestExtends(String testExtends) {
	    this.testExtends = testExtends;
	}
}
