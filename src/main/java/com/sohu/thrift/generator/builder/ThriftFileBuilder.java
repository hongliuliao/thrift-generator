/**
 * 
 */
package com.sohu.thrift.generator.builder;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.thrift.protocol.TType;
import org.junit.Test;

import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import com.sohu.thrift.generator.Generic;
import com.sohu.thrift.generator.ThriftEnum;
import com.sohu.thrift.generator.ThriftEnumField;
import com.sohu.thrift.generator.ThriftField;
import com.sohu.thrift.generator.ThriftMethod;
import com.sohu.thrift.generator.ThriftMethodArg;
import com.sohu.thrift.generator.ThriftService;
import com.sohu.thrift.generator.ThriftStruct;
import com.sohu.thrift.generator.ThriftType;
import com.sohu.thrift.generator.utils.CommonUtils;
import com.sohu.thrift.generator.utils.ParameterNameDiscoverer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author hongliuliao
 *
 * createTime:2012-11-23 上午11:33:35
 */
public class ThriftFileBuilder {
	
	protected Class<?> commonServiceClass;
	
	private static final ParameterNameDiscoverer parameterNameDiscoverer = new ParameterNameDiscoverer();
	
	public String getPackageName() {
		String packageName = commonServiceClass.getPackage().getName();
		String thriftPackage = packageName + ".thrift";
		return thriftPackage;
	}
	
	public ThriftService buildThriftService(List<ThriftStruct> structs, List<ThriftEnum> enums) {
		
		ThriftService service = new ThriftService();
		
		Method[] methods = commonServiceClass.getDeclaredMethods();
		List<ThriftMethod> thriftMethods = new ArrayList<ThriftMethod>();
		for (Method method : methods) {
			structs.addAll(this.getAllStruct(method, enums));
			ThriftMethod thriftMethod = new ThriftMethod();
			thriftMethod.setName(method.getName());
			thriftMethod.setRelationClasses(CommonUtils.getMethodReturnTypeRelationClasses(method));
			
			ThriftType returnThriftType = ThriftType.fromJavaType(method.getGenericReturnType());
			thriftMethod.setReturnGenericType(Generic.fromType(method.getGenericReturnType()));
			if(returnThriftType.isStruct()) {
				this.buildThriftStruct(method.getReturnType(), structs, enums);
			}
			
			Type[] paramTypes = method.getGenericParameterTypes();
			String[] paramNames = parameterNameDiscoverer.getParameterNames(method);
			List<ThriftMethodArg> methodArgs = buildThriftMethodArgs(structs, paramTypes, paramNames, enums);
			
			thriftMethod.setMethodArgs(methodArgs);
			thriftMethods.add(thriftMethod);
		}
		
		service.setName(commonServiceClass.getSimpleName());
		service.setMethods(thriftMethods);
		return service;
	}

	/**
	 * @param structs
	 * @param paramTypes
	 * @param paramNames
	 * @return
	 */
	private List<ThriftMethodArg> buildThriftMethodArgs(List<ThriftStruct> structs, Type[] paramTypes, String[] paramNames, List<ThriftEnum> enums) {
		List<ThriftMethodArg> methodArgs = new ArrayList<ThriftMethodArg>();
		for (int i = 0; i < paramTypes.length; i++) {
			ThriftMethodArg methodArg = new ThriftMethodArg();
			methodArg.setName(paramNames == null || paramNames.length == 0 ? ("arg" + i) : paramNames[i]);

			Type paramType = paramTypes[i];
			ThriftType paramThriftType = ThriftType.fromJavaType(paramType);
			methodArg.setGenericType(Generic.fromType(paramType));
			if(paramThriftType.isStruct()) {
				this.buildThriftStruct((Class<?>)paramType, structs, enums);
			}
			methodArgs.add(methodArg);
		}
		return methodArgs;
	}
	
	public boolean isBasicType(Class<?> clazz) {
		return CommonUtils.isBasicType(clazz);
	}
	
	public boolean isCollectionType(Class<?> clazz) {
		return CommonUtils.isCollectionType(clazz);
	}
	
	public List<ThriftStruct> getAllStruct(Method method, List<ThriftEnum> enums) {
		List<ThriftStruct> structs = new ArrayList<ThriftStruct>();
		
		List<Class<?>> classes = CommonUtils.getMethodReturnTypeRelationClasses(method);
		for (Class<?> class1 : classes) {
			structs.add(this.buildThriftStruct(class1, structs, enums));
		}
		
		Class<?> returnClass = method.getReturnType();
		ThriftType thriftType = ThriftType.fromJavaType(returnClass);
		if(thriftType.isStruct()) {
			ThriftStruct struct = this.buildThriftStruct(returnClass, structs, enums);
			if(struct != null) {
				structs.add(struct);
			}
		}
		
		Class<?>[] argClasses = method.getParameterTypes();
		List<Class<?>> methodGenericTypes = new ArrayList<Class<?>>();
		List<Class<?>> paramTypes = CommonUtils.getMethodGenericParameterTypes(method);
		for (Class<?> paramType : paramTypes) {
			CommonUtils.getGenericParameterTypes(paramType, methodGenericTypes);
		}
		for (Class<?> genericType : methodGenericTypes) {
			if(this.isBasicType(genericType)) {
				continue;
			}
			if(this.isCollectionType(genericType)) {
				continue;
			}
			this.buildThriftStruct(genericType, structs, enums);
		}
		
		for (Class<?> clazz : argClasses) {
			if(this.isBasicType(clazz)) {
				continue;
			}
			if(this.isCollectionType(clazz)) {
				continue;
			}
			this.buildThriftStruct(clazz, structs, enums);
		}
		
		return structs;
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
	
	public ThriftStruct buildThriftStruct(Class<?> clazz, List<ThriftStruct> structs, List<ThriftEnum> enums) {
		if(Message.class.isAssignableFrom(clazz)) {
			return this.buildThriftStructForThrift(clazz, structs, enums);
		}
		Field[] fields = clazz.getDeclaredFields();
		ThriftStruct struct = new ThriftStruct();
		List<ThriftField> thriftFields = new ArrayList<ThriftField>();
		for (Field field : fields) {
			ThriftField thriftField = this.buildThriftField(field, structs, enums);
			if(thriftField == null) {
				continue;
			}
			thriftFields.add(thriftField);
		}
		struct.setName(clazz.getSimpleName());
		struct.setFields(thriftFields);
		return struct;
	}
	
	public ThriftStruct buildThriftStructForThrift(Class<?> clazz, List<ThriftStruct> structs, List<ThriftEnum> enums) {
		ThriftStruct struct = new ThriftStruct();
		struct.setName(clazz.getSimpleName());
		Message.Builder builder = CommonUtils.getBuilder(clazz);
		List<FieldDescriptor> fieldDescriptors = builder.getDescriptorForType().getFields();
		List<ThriftField> fields = new ArrayList<ThriftField>();
		for (FieldDescriptor fieldDescriptor : fieldDescriptors) {
			ThriftField thriftField = new ThriftField();
			thriftField.setName(fieldDescriptor.getName());
			ThriftType thriftType = ThriftType.fromProtoType(fieldDescriptor);
			
			String fieldName = CommonUtils.underlineField2JavaField(fieldDescriptor.getName());
			Field field = CommonUtils.findField(clazz, CommonUtils.getFirstLower(fieldName + "_"));
			thriftType.setJavaTypeName(field.getType().getSimpleName());
			if(thriftType.isEnum()) {
				buildEnumForProto(enums, fieldDescriptor, field);
			}
			if(thriftType.gettType() == TType.LIST) {
				thriftField.setGenericType(Generic.fromType(field.getGenericType()));
			}
			if(thriftType.isStruct()) {
				structs.add(buildThriftStructForThrift(field.getType(), structs, enums));
			}
			
			Method getMethod = this.getGetMethod(clazz, thriftType, fieldDescriptor);
			thriftField.setGenericType(Generic.fromType(getMethod.getGenericReturnType()));
			fields.add(thriftField);
		}
		struct.setFields(fields);
		return struct;
	}

	private Method getGetMethod(Class<?> clazz, ThriftType thriftType, FieldDescriptor fieldDescriptor) {
		String methodPrefix = "get";
		String getMethodName = methodPrefix + CommonUtils.getFirstUpper(CommonUtils.column2PropertyName(fieldDescriptor.getName()));
		return CommonUtils.findMethodByName(clazz, getMethodName);
	} 
	
	/**
	 * @param enums
	 * @param fieldDescriptor
	 * @param field
	 */
	private void buildEnumForProto(List<ThriftEnum> enums,
			FieldDescriptor fieldDescriptor, Field field) {
		List<EnumValueDescriptor> valueDescriptors = fieldDescriptor.getEnumType().getValues();
		ThriftEnum thriftEnum = new ThriftEnum();
		thriftEnum.setName(field.getType().getSimpleName());
		List<ThriftEnumField> nameValues = new ArrayList<ThriftEnumField>();
		for (EnumValueDescriptor enumValueDescriptor : valueDescriptors) {
			ThriftEnumField nameValue = new ThriftEnumField(enumValueDescriptor.getName(), enumValueDescriptor.getNumber());
			nameValues.add(nameValue);
		}
		thriftEnum.setFields(nameValues);
		enums.add(thriftEnum);
	}
	
	
	
	
	public ThriftField buildThriftField(Field field, List<ThriftStruct> structs, List<ThriftEnum> enums) {
		ThriftField thriftField = new ThriftField();
		thriftField.setName(field.getName());
		if(field.getName().equals("__PARANAMER_DATA")) {
			return null;
		}
		Type type = field.getGenericType();
		Generic generic = Generic.fromType(field.getGenericType());
		thriftField.setGenericType(generic);
		if(type instanceof ParameterizedType) {
			buildStrutsByGeneric(structs, generic, enums);
		}else {
			if(generic.isEnum() || generic.isStruct()) {
				generic.setJavaClass(field.getType());
				generic.setValue(field.getType().getSimpleName());
				generic.setJavaTypeName(field.getType().getSimpleName());
				if(generic.isStruct()) {
					structs.add(buildThriftStruct(field.getType(), structs, enums));
				}else {
					enums.add(buildThriftEnum(field.getType()));
				}
				
			}
		}
		return thriftField;
	}

	/**
	 * @param structs
	 * @param generic
	 */
	private void buildStrutsByGeneric(List<ThriftStruct> structs,
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

	
	@Test
	public void buildToOutputStream(OutputStream os) throws Exception {
		Configuration cfg = new Configuration();
		cfg.setClassForTemplateLoading(ThriftFileBuilder.class, "/");
		Template template = cfg.getTemplate("thrift.ftl");
		Writer out = new OutputStreamWriter(os);
		
		List<ThriftStruct> structs = new ArrayList<ThriftStruct>();
		List<ThriftEnum> enums = new ArrayList<ThriftEnum>();
		ThriftService service = this.buildThriftService(structs, enums);
		
		Map<String, Object> rootMap = new HashMap<String, Object>();
		rootMap.put("thriftServicePackage", this.getPackageName());
		CommonUtils.removeRepeat(structs);
		rootMap.put("structList", structs);
		rootMap.put("enumList", enums);
		CommonUtils.removeRepeat(enums);
		rootMap.put("serviceList", Arrays.asList(service));
		
		template.process(rootMap, out);
	}
	
	/**
	 * @return the commonServiceClass
	 */
	public Class<?> getCommonServiceClass() {
		return commonServiceClass;
	}

	/**
	 * @param commonServiceClass the commonServiceClass to set
	 */
	public void setCommonServiceClass(Class<?> commonServiceClass) {
		this.commonServiceClass = commonServiceClass;
	}
	
	
	
}
