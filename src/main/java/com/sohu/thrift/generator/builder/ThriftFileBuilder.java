/**
 * 
 */
package com.sohu.thrift.generator.builder;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sohu.thrift.generator.ThriftEnum;
import com.sohu.thrift.generator.ThriftService;
import com.sohu.thrift.generator.ThriftStruct;
import com.sohu.thrift.generator.utils.CommonUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author hongliuliao
 *
 * createTime:2012-11-23 上午11:33:35
 */
public class ThriftFileBuilder {
	
	public String getPackageName(Class<?> commonServiceClass) {
		String packageName = commonServiceClass.getPackage().getName();
		String thriftPackage = packageName + ".thrift";
		return thriftPackage;
	}
	
	public void buildToOutputStream(Class<?> commonServiceClass, OutputStream os) throws Exception {
		Configuration cfg = new Configuration();
		cfg.setClassForTemplateLoading(ThriftFileBuilder.class, "/");
		Template template = cfg.getTemplate("thrift.ftl");
		Writer out = new OutputStreamWriter(os);
		
		ThriftServiceBuilder serviceBuilder = new ThriftServiceBuilder(commonServiceClass);
		ThriftService service = serviceBuilder.buildThriftService();
		
		Map<String, Object> rootMap = new HashMap<String, Object>();
		rootMap.put("thriftServicePackage", this.getPackageName(commonServiceClass));
		List<ThriftStruct> structs = serviceBuilder.getStructs();
		List<ThriftEnum> enums = serviceBuilder.getEnums();
		CommonUtils.removeRepeat(structs);
		rootMap.put("structList", structs);
		rootMap.put("enumList", enums);
		CommonUtils.removeRepeat(enums);
		rootMap.put("serviceList", Arrays.asList(service));
		
		template.process(rootMap, out);
	}
	
}
