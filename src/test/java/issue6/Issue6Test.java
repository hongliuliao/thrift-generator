/**
 * 
 */
package issue6;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sohu.thrift.generator.ThriftService;
import com.sohu.thrift.generator.builder.ThriftServiceBuilder;

/**
 * @author liao
 *
 */
public class Issue6Test {

    @Test
    public void testBuildThriftService() throws Exception {
        ThriftServiceBuilder tsb = new ThriftServiceBuilder();
        tsb.setServiceClass(Issue6Service.class);
        ThriftService ts = tsb.buildThriftService();
        String as = ts.getMethods().get(0).getMethodArgs().get(0).getGenericType().toThriftString();
        assertEquals("list enum args type fail!", "list<Status>", as);

        String ls = ts.getMethods().get(0).getReturnGenericType().toThriftString();
        assertEquals("list enum return type fail!", "list<Status>", ls);
        //this.fileBuilder.buildToOutputStream(Issue6Service.class, System.out);
    }
    
}
