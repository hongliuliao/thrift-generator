/**
 * 
 */
package com.sohu.thrift.generator;

import java.util.List;

/**
 * @author hongliuliao
 *
 * createTime:2012-11-23 上午11:59:37
 */
public class ThriftMethod {
	
	private Generic returnGenericType;
	
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

	/**
	 * @return the returnGeneric
	 */
	public Generic getReturnGenericType() {
		return returnGenericType;
	}

	/**
	 * @param returnGeneric the returnGeneric to set
	 */
	public void setReturnGenericType(Generic returnGeneric) {
		this.returnGenericType = returnGeneric;
	}
	
}
