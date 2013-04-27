/**
 * 
 */
package com.sohu.thrift.generator.builder;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.sohu.thrift.generator.Generic;
import com.sohu.thrift.generator.ThriftEnum;
import com.sohu.thrift.generator.ThriftMethod;
import com.sohu.thrift.generator.ThriftMethodArg;
import com.sohu.thrift.generator.ThriftService;
import com.sohu.thrift.generator.ThriftStruct;
import com.sohu.thrift.generator.ThriftType;
import com.sohu.thrift.generator.utils.CommonUtils;
import com.sohu.thrift.generator.utils.ParameterNameDiscoverer;

/**
 * 
 * @author liaohongliu
 *
 * 创建日期:2013-4-27 下午10:08:14
 */
public class ThriftServiceBuilder {
	
	private static final ParameterNameDiscoverer parameterNameDiscoverer = new ParameterNameDiscoverer();
	
	private ThriftStructBuilder thriftStructBuilder = new ThriftStructBuilder();
	
	protected Class<?> commonServiceClass;
	
	List<ThriftStruct> structs = new ArrayList<ThriftStruct>();
	List<ThriftEnum> enums = new ArrayList<ThriftEnum>();
	
	public ThriftServiceBuilder(Class<?> commonServiceClass) {
		super();
		this.commonServiceClass = commonServiceClass;
	}

	public ThriftService buildThriftService() {
		
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
				thriftStructBuilder.buildThriftStruct(method.getReturnType(), structs, enums);
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
	
	private boolean isBasicType(Class<?> clazz) {
		return CommonUtils.isBasicType(clazz);
	}
	
	private boolean isCollectionType(Class<?> clazz) {
		return CommonUtils.isCollectionType(clazz);
	}
	
	public List<ThriftStruct> getAllStruct(Method method, List<ThriftEnum> enums) {
		List<ThriftStruct> structs = new ArrayList<ThriftStruct>();
		
		List<Class<?>> classes = CommonUtils.getMethodReturnTypeRelationClasses(method);
		for (Class<?> class1 : classes) {
			structs.add(thriftStructBuilder.buildThriftStruct(class1, structs, enums));
		}
		
		Class<?> returnClass = method.getReturnType();
		ThriftType thriftType = ThriftType.fromJavaType(returnClass);
		if(thriftType.isStruct()) {
			ThriftStruct struct = thriftStructBuilder.buildThriftStruct(returnClass, structs, enums);
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
			thriftStructBuilder.buildThriftStruct(genericType, structs, enums);
		}
		
		for (Class<?> clazz : argClasses) {
			if(this.isBasicType(clazz)) {
				continue;
			}
			if(this.isCollectionType(clazz)) {
				continue;
			}
			thriftStructBuilder.buildThriftStruct(clazz, structs, enums);
		}
		
		return structs;
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
				thriftStructBuilder.buildThriftStruct((Class<?>)paramType, structs, enums);
			}
			methodArgs.add(methodArg);
		}
		return methodArgs;
	}
	
	public List<ThriftStruct> getStructs() {
		return structs;
	}

	public List<ThriftEnum> getEnums() {
		return enums;
	}
}
