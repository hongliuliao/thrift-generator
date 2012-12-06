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
public class ThriftStructBuilderTest {

	private ThriftFileBuilder fileBuilder = new ThriftFileBuilder();
	
	@Test
	public void toOutputstream() throws Exception {
		fileBuilder.setCommonServiceClass(ICommonUserService.class);
		this.fileBuilder.buildToOutputStream(System.out);
	}
	
}
