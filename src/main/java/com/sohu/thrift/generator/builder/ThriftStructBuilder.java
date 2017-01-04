package com.sohu.thrift.generator.builder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sohu.thrift.generator.Generic;
import com.sohu.thrift.generator.ThriftEnum;
import com.sohu.thrift.generator.ThriftEnumField;
import com.sohu.thrift.generator.ThriftField;
import com.sohu.thrift.generator.ThriftStruct;
import com.sohu.thrift.generator.ThriftType;

public class ThriftStructBuilder {
	
    private static Logger log = Logger.getLogger(ThriftStructBuilder.class);
	
    ThriftFieldBuilder thriftFieldBuilder = new ThriftFieldBuilder();
    
    private boolean isIncludeSuperField;

    ThriftStructBuilder() {
        isIncludeSuperField = true;
    }
	
	public ThriftStruct buildThriftStruct(Class<?> clazz, List<ThriftStruct> structs, List<ThriftEnum> enums) {
        List<Field> fields = new ArrayList<Field>();
        
        if (isIncludeSuperField) {
            Class<?> superClass = clazz.getSuperclass();
            while (superClass != null && superClass != Object.class) {
                log.debug("include super class field, class:" + clazz.getSuperclass());
                Field[] sf = superClass.getDeclaredFields();
                for (Field s : sf) {
                    fields.add(s);
                }
                superClass = superClass.getSuperclass();
            }
        }
	
        Field[] myFields = clazz.getDeclaredFields();
        for (Field f : myFields) {
            fields.add(f);
        }

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
        structs.add(struct);

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
			if(field.getName().equals("$VALUES") || field.getName().equals("__PARANAMER_DATA")) {
				continue;
			}
			ThriftEnumField nameValue = new ThriftEnumField(field.getName(), i);
			nameValues.add(nameValue);
		}
		thriftEnum.setFields(nameValues);
		return thriftEnum;
	}
    
    public void setIncludeSuperField(boolean isInclude) {
        this.isIncludeSuperField = isInclude;
    }
}
