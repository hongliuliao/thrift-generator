/**
 * 
 */
package com.sohu.thrift.generator;

import java.lang.reflect.Type;

import org.apache.commons.collections.CollectionUtils;

import com.sohu.thrift.generator.utils.CommonUtils;

/**
 * @author hongliuliao
 *
 * createTime:2012-11-23 上午11:58:02
 */
public class ThriftField {
	
	private ThriftType thriftType;
	
	private Object genericsType;
	
	private Generic generic;
	
	private String name;
	
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
	 * @return the genericsType
	 */
	public Object getGenericsType() {
		if(genericsType instanceof ThriftType) {
			return ((ThriftType)genericsType).getValue();
		}
		if(genericsType == null) {
			return "";
		}
		return genericsType;
	}

	/**
	 * @param genericsType the genericsType to set
	 */
	public void setGenericsType(Object genericsType) {
		this.genericsType = genericsType;
	}

	@Override
	public String toString() {
		return "ThriftField [thriftType=" + thriftType + ", genericsType="
				+ genericsType + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((genericsType == null) ? 0 : genericsType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((thriftType == null) ? 0 : thriftType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ThriftField other = (ThriftField) obj;
		if (genericsType == null) {
			if (other.genericsType != null)
				return false;
		} else if (!genericsType.equals(other.genericsType))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (thriftType != other.thriftType)
			return false;
		return true;
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
	
	public void buildGeneric(Type type) {
		Generic generic = CommonUtils.getGenericsByType(type);
		if(CollectionUtils.isNotEmpty(generic.getTypes())) {
			this.generic = generic;
			this.genericsType = generic.toString();
		}
	}
	
}
