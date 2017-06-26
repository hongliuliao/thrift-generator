/**
 * 
 */
package issue4;

import org.junit.Test;

import com.sohu.thrift.generator.builder.ThriftFileBuilder;

/**
 * @author liao
 *
 */
public class Issue4Test {

    private ThriftFileBuilder fileBuilder = new ThriftFileBuilder();
    
    @Test(expected = IllegalArgumentException.class)
    public void toOutputstream() throws Exception {
        this.fileBuilder.buildToOutputStream(Issue4Service.class, System.out);
    }
}
