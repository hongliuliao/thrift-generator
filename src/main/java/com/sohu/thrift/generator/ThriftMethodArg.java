/**
 * 
 */
package com.sohu.thrift.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.collections.CollectionUtils;

import com.sohu.thrift.generator.utils.CommonUtils;

/**
 * @author hongliuliao
 *
 * createTime:2012-11-23 下午2:45:25
 */
public class ThriftMethodArg {
	
	private ThriftType thriftType;
	
	private Generic generic;
	
	private String name;

	/**
	 * 
	 */
	public ThriftMethodArg() {
		
	}
	
	public ThriftMethodArg(Type type, String paramName) {
		this.buildThriftType(type);
		this.buildGenerics(type);
		this.name = paramName;
	}
	
	public void buildThriftType(Type type) {
		if(!(type instanceof ParameterizedType)) {
			this.thriftType = ThriftType.fromJavaType((Class<?>)type);
		}else {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			this.thriftType = ThriftType.fromJavaType((Class<?>)parameterizedType.getRawType());
		}
	}
	
	public void buildGenerics(Type type) {
		Generic generic = CommonUtils.getGenericsByType(type);
		if(CollectionUtils.isNotEmpty(generic.getTypes())) {
			this.generic = generic;
		}
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
	 * @return the thriftType
	 */
	public ThriftType getThriftType() {
		return thriftType;
	}

	/**
	 * @param thriftType the thriftType to set
	 */
	public void setThriftType(ThriftType thriftType) {
		this.thriftType = thriftType;
	}

	/**
	 * @return the generic
	 */
	public Generic getGeneric() {
		return generic;
	}

	/**
	 * @param generic the generic to set
	 */
	public void setGeneric(Generic generic) {
		this.generic = generic;
	}
	
	
	
}
