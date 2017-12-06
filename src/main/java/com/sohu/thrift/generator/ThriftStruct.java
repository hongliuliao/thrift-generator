/**
 * 
 */
package com.sohu.thrift.generator;

import java.util.List;

/**
 * @author hongliuliao
 *
 * createTime:2012-11-23 上午11:49:16
 */
public class ThriftStruct {
	
	private String name;
	
	private List<ThriftField> fields;
	
	private Class<?> peerClass;

	public Class<?> getPeerClass() {
		return peerClass;
	}

	public void setPeerClass(Class<?> peerClass) {
		this.peerClass = peerClass;
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
	 * @return the fields
	 */
	public List<ThriftField> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<ThriftField> fields) {
		this.fields = fields;
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
		ThriftStruct other = (ThriftStruct) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ThriftStruct [name=" + name + ", fields=" + fields + "]";
	}
	
	
	
}
