/**
 * 
 */
package com.sohu.thrift.generator;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaMethod;

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
	
	private List<ThriftStruct> exceptions;
	
	/**
	 * method define is source code
	 */
	private JavaMethod javaMethod;

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

	public JavaMethod getJavaMethod() {
		return javaMethod;
	}

	public void setJavaMethod(JavaMethod javaMethod) {
		this.javaMethod = javaMethod;
	}
	
	public boolean isComments() {
		if (this.javaMethod == null) {
			return false;
		}
		if (this.javaMethod.getComment() == null && javaMethod.getTags().isEmpty()) {
			return false;
		}
		return true;
	}
	
	public String getComment() {
		if (this.javaMethod == null || this.javaMethod.getComment() == null) {
			return "";
		}
		return this.javaMethod.getComment();
	}
	
	public List<DocletTag> getDocTags() {
		if (this.javaMethod == null || this.javaMethod.getTags().isEmpty()) {
			return new ArrayList<DocletTag>();
		}
		return this.javaMethod.getTags();
	}

	public List<ThriftStruct> getExceptions() {
		return exceptions;
	}

	public void setExceptions(List<ThriftStruct> exceptions) {
		this.exceptions = exceptions;
	}
}
