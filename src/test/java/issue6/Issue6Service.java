package issue6;

import java.util.List;

import com.sohu.thrift.generator.test.Status; 

public interface Issue6Service {

    public List<Status> getUserStatus(List<Status> us); 
}
