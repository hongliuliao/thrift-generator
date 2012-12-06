/**
 * 
 */
package com.sohu.thrift.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.sohu.thrift.generator.utils.CommonUtils;

/**
 * @author hongliuliao
 *
 * createTime:2012-11-23 下午2:45:25
 */
public class ThriftMethodArg {
	
	private Object type;//to string
	
	private ThriftType thriftType;
	
	private Generic generic;
	
	private String name;
	
	private List<Class<?>> relationClasses = new ArrayList<Class<?>>();

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
	 * @return the type
	 */
	public Object getType() {
		if(type instanceof ThriftType) {
			return ((ThriftType) type).getValue();
		}
		String desc = CommonUtils.convertJavaToThrift(type.toString());
		for (Class<?> clazz : this.getRelationClasses()) {
			if(desc.indexOf(clazz.getName()) != -1) {
				desc = desc.replace(clazz.getName(), clazz.getSimpleName());
			}
		}
		return desc;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Object type) {
		this.type = type;
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
