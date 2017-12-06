/**
 * 
 */
package com.sohu.thrift.generator.builder;

import org.junit.Test;

import com.sohu.thrift.generator.test.ICommonUserService;

/**
 * @author hongliuliao
 *
 * createTime:2012-12-6 下午3:24:05
 */
public class ThriftFileBuilderTest {

	private ThriftFileBuilder fileBuilder = new ThriftFileBuilder();
	
	@Test
	public void testToOutputstream() throws Exception {
		this.fileBuilder.setSourceDir("src/test/java");
		this.fileBuilder.buildToOutputStream(ICommonUserService.class, System.out);
	}
	
}
