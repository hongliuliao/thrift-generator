/**
 * 
 */
package com.sohu.thrift.generator;

import java.util.List;

import com.sohu.thrift.generator.utils.CommonUtils;

/**
 * @author hongliuliao
 *
 * createTime:2012-11-23 上午11:59:37
 */
public class ThriftMethod {
	
	private Object returnType;
	
	private String name;
	
	private List<ThriftMethodArg> methodArgs;
	
	private List<Class<?>> relationClasses;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the args
	 */
	public List<ThriftMethodArg> getMethodArgs() {
		return methodArgs;
	}

	/**
	 * @param args the args to set
	 */
	public void setMethodArgs(List<ThriftMethodArg> args) {
		this.methodArgs = args;
	}

	/**
	 * @return the returnType
	 */
	public Object getReturnType() {
		if(returnType instanceof ThriftType) {
			Object value = ((ThriftType) returnType).getValue();
			return value;
		}
		if(returnType instanceof Class) {
			ThriftType thriftType = ThriftType.fromJavaType((Class<?>) returnType);
			if(thriftType.isBasicType()) {
				return thriftType.getValue();
			}
		}
		String desc = CommonUtils.convertJavaToThrift(returnType.toString());
		for (Class<?> clazz : this.getRelationClasses()) {
			if(desc.indexOf(clazz.getName()) != -1) {
				desc = desc.replace(clazz.getName(), clazz.getSimpleName());
			}
		}
		return desc;
	}
	

	/**
	 * @param returnType the returnType to set
	 */
	public void setReturnType(Object returnType) {
		this.returnType = returnType;
	}

	/**
	 * @return the relationClasses
	 */
	public List<Class<?>> getRelationClasses() {
		return relationClasses;
	}

	/**
	 * @param relationClasses the relationClasses to set
	 */
	public void setRelationClasses(List<Class<?>> relationClasses) {
		this.relationClasses = relationClasses;
	}
	
}
