/**
 * 
 */
package issue3;

import org.junit.Test;

import issue3.IAccService;
import com.sohu.thrift.generator.builder.ThriftFileBuilder;

/**
 * @author hongliuliao
 *
 * createTime:2012-12-6 下午3:24:05
 */
public class Issue3Test {

	private ThriftFileBuilder fileBuilder = new ThriftFileBuilder();
	
	@Test
	public void toOutputstream() throws Exception {
        //this.fileBuilder.setIncludeSuper(false);
		this.fileBuilder.buildToOutputStream(IAccService.class, System.out);
	}
	
}
