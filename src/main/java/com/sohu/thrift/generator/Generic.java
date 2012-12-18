/**
 * 
 */
package com.sohu.thrift.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
		for (int i = 0; i < types.size(); i++) {
			Object type = types.get(i);
			if(type instanceof Generic) {
				sb.append(((Generic) type).toThriftString());
			}else {
				ThriftType thriftType = (ThriftType) type;
				sb.append(thriftType.getValue());
			}
			if(i != types.size() - 1) {
				sb.append(", ");
			}
		}
		sb.append(">");
		return sb.toString();
	}
	
	public static Generic fromType(Type type) {
		if(!(type instanceof ParameterizedType)) {
			return null;
		}
		Generic generic = new Generic();
		ParameterizedType parameterizedType = (ParameterizedType) type;
		Type[] types = parameterizedType.getActualTypeArguments();
		for (Type type2 : types) {
			if(type2 instanceof ParameterizedType) {
				generic.addGeneric(fromType(type2));
				continue;
			}
			ThriftType thriftType = ThriftType.fromJavaType((Class<?>)type2);
			if(thriftType == ThriftType.STRUCT) {
				thriftType = thriftType.clone();
				thriftType.setJavaClass((Class<?>)type2);
				thriftType.setValue(((Class<?>)type2).getSimpleName());
			}
			generic.addGeneric(thriftType);
		}
		return generic;
	}
	
}
