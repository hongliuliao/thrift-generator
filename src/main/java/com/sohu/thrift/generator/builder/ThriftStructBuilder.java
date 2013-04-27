package com.sohu.thrift.generator.builder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.sohu.thrift.generator.Generic;
import com.sohu.thrift.generator.ThriftEnum;
import com.sohu.thrift.generator.ThriftEnumField;
import com.sohu.thrift.generator.ThriftField;
import com.sohu.thrift.generator.ThriftStruct;
import com.sohu.thrift.generator.ThriftType;

public class ThriftStructBuilder {
	
	ThriftFieldBuilder thriftFieldBuilder = new ThriftFieldBuilder();
	
	public ThriftStruct buildThriftStruct(Class<?> clazz, List<ThriftStruct> structs, List<ThriftEnum> enums) {
		Field[] fields = clazz.getDeclaredFields();
		ThriftStruct struct = new ThriftStruct();
		List<ThriftField> thriftFields = new ArrayList<ThriftField>();
		for (Field field : fields) {
			ThriftField thriftField = thriftFieldBuilder.buildThriftField(this, field, structs, enums);
			if(thriftField == null) {
				continue;
			}
			thriftFields.add(thriftField);
		}
		struct.setName(clazz.getSimpleName());
		struct.setFields(thriftFields);
		return struct;
	}
	
	/**
	 * @param structs
	 * @param generic
	 */
	public void buildStrutsByGeneric(List<ThriftStruct> structs,
			Generic generic, List<ThriftEnum> enums) {
		List<ThriftType> thriftTypes = generic.getTypes();
		for (ThriftType subThriftType : thriftTypes) {
			if(subThriftType.isStruct()) {
				buildThriftStruct(subThriftType.getJavaClass(), structs, enums);
			}
			if(subThriftType instanceof Generic) {
				this.buildStrutsByGeneric(structs, (Generic) subThriftType, enums);
			}
		}
	}
	
	public ThriftEnum buildThriftEnum(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		ThriftEnum thriftEnum = new ThriftEnum();
		thriftEnum.setName(clazz.getSimpleName());
		
		List<ThriftEnumField> nameValues = new ArrayList<ThriftEnumField>();
		for (int i = 0;i < fields.length;i ++) {
			Field field = fields[i];
			if(field.getName().equals("ENUM$VALUES") || field.getName().equals("__PARANAMER_DATA")) {
				continue;
			}
			ThriftEnumField nameValue = new ThriftEnumField(field.getName(), i);
			nameValues.add(nameValue);
		}
		thriftEnum.setFields(nameValues);
		return thriftEnum;
	}
}
