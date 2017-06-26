package issue4;

import com.sohu.thrift.generator.test.User; 

public interface Issue4Service {

    public ReturnResult<User> getUser(int userId); 
}
