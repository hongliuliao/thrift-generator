/**
 * 
 */
package com.sohu.thrift.generator;

import java.util.ArrayList;
import java.util.List;

/**
 * 泛型描述类,是一个树形的结构,每个Generic都有可能有多个types
 * @author hongliuliao
 *
 * createTime:2012-11-28 上午9:45:20
 */
public class Generic extends ThriftType {

	/**
	 * 可能是ThriftType,也可能还是泛型
	 */
	private List<? super ThriftType> types = new ArrayList<ThriftType>();

	/**
	 * @return the types
	 */
	@SuppressWarnings("unchecked")
	public List<ThriftType> getTypes() {
		return (List<ThriftType>) types;
	}

	/**
	 * @param types the types to set
	 */
	public void setTypes(List<ThriftType> types) {
		this.types = types;
	}
	
	public void addGeneric(ThriftType generic) {
		types.add(generic);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("<");
		for (Object type : types) {
			if(type instanceof Generic) {
				sb.append(type.toString());
			}else {
				ThriftType thriftType = (ThriftType) type;
				if(thriftType.isStruct()) {
					sb.append(thriftType.getValue());
				}else {
					sb.append(thriftType.getWarpperClassName());
				}
			}
		}
		sb.append(">");
		return sb.toString();
	}
	
	public String toThriftString() {
		if(types == null || types.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder("<");
		for (Object type : types) {
			if(type instanceof Generic) {
				sb.append(type.toString());
			}else {
				ThriftType thriftType = (ThriftType) type;
				sb.append(thriftType.getValue());
			}
		}
		sb.append(">");
		return sb.toString();
	}
	
}
