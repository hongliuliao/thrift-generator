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


	@Override
	public String toString() {
		return "ThriftField [thriftType=" + thriftType + ", genericsType="
				+ generic + ", name=" + name + "]";
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
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
