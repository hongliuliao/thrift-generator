/**
 * 
 */
package com.sohu.thrift.generator;


/**
 * @author hongliuliao
 *
 * createTime:2012-11-23 上午11:58:02
 */
public class ThriftField {
	
	private Generic genericType;
	
	private String name;
	
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
