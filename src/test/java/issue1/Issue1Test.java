/**
 * 
 */
package issue1;

import org.junit.Test;

import com.sohu.thrift.generator.builder.ThriftFileBuilder;

/**
 * @author liao
 *
 */
public class Issue1Test {

    private ThriftFileBuilder fileBuilder = new ThriftFileBuilder();
    
    @Test
    public void toOutputstream() throws Exception {
        this.fileBuilder.buildToOutputStream(IGroupMemberQueryService.class, System.out);
    }
}
