/**
 * 
 */
package com.sohu.thrift.generator;

import java.lang.reflect.Type;

/**
 * @author hongliuliao
 *
 * createTime:2012-11-23 下午2:45:25
 */
public class ThriftMethodArg {
	
	private Generic genericType;
	
	private String name;

	/**
	 * 
	 */
	public ThriftMethodArg() {
		
	}
	
	public ThriftMethodArg(Type type, String paramName) {
		this.name = paramName;
	}
	
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
	 * @return the generic
	 */
	public Generic getGenericType() {
		return genericType;
	}

	/**
	 * @param generic the generic to set
	 */
	public void setGenericType(Generic generic) {
		this.genericType = generic;
	}
	
	
	
}
