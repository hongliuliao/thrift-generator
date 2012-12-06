/**
 * 
 */
package com.sohu.thrift.generator.builder;

import java.io.FileOutputStream;

import org.junit.Test;

import com.sohu.thrift.generator.test.ICommonUserService;
import com.sohu.thrift.generator.test.protobuf.IUserService;

/**
 * @author hongliuliao
 *
 * createTime:2012-12-6 ÏÂÎç3:24:05
 */
public class ThriftStructBuilderTest {

	private ThriftFileBuilder fileBuilder = new ThriftFileBuilder();
	
	@Test
	public void toOutputstream() throws Exception {
		fileBuilder.setCommonServiceClass(ICommonUserService.class);
		this.fileBuilder.buildToOutputStream(System.out);
	}
	
	@Test
	public void toOutputstream_protobuf() throws Exception {
		this.fileBuilder.setCommonServiceClass(IUserService.class);
		this.fileBuilder.buildToOutputStream(System.out);
		this.fileBuilder.buildToOutputStream(new FileOutputStream("d:/test.thrift"));
	}
}
