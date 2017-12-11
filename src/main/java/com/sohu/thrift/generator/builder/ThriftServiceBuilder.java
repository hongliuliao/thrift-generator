/**
 * 
 */
package com.sohu.thrift.generator.builder;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.sohu.thrift.generator.Generic;
import com.sohu.thrift.generator.ThriftEnum;
import com.sohu.thrift.generator.ThriftMethod;
import com.sohu.thrift.generator.ThriftMethodArg;
import com.sohu.thrift.generator.ThriftService;
import com.sohu.thrift.generator.ThriftStruct;
import com.sohu.thrift.generator.ThriftType;
import com.sohu.thrift.generator.utils.CommonUtils;
import com.sohu.thrift.generator.utils.ParameterNameDiscoverer;
import com.thoughtworks.qdox.model.JavaMethod;

/**
 * 
 * @author liaohongliu
 *
 * 创建日期:2013-4-27 下午10:08:14
 */
public class ThriftServiceBuilder {
	
	private static final ParameterNameDiscoverer parameterNameDiscoverer = new ParameterNameDiscoverer();

    private static Logger log = Logger.getLogger(ThriftServiceBuilder.class);
	
	private ThriftStructBuilder thriftStructBuilder;
	
	protected Class<?> commonServiceClass;
	
	private String srcDir;
	
	List<ThriftStruct> structs = new ArrayList<ThriftStruct>();
	List<ThriftEnum> enums = new ArrayList<ThriftEnum>();
	List<ThriftStruct> exceptions = new ArrayList<ThriftStruct>();
	
	public ThriftServiceBuilder() {
		super();
        thriftStructBuilder = new ThriftStructBuilder();
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
			
			thriftMethod.setExceptions(this.buildThriftExceptions(method, structs, enums));
			
			thriftMethods.add(thriftMethod);
		}
		
		// order by source define order
		thriftMethods = rebuildBySoureInfo(thriftMethods);
		
		// mv exceptions from common struct to exceptions
		for (Iterator<ThriftStruct> i = structs.iterator(); i.hasNext();) {
			ThriftStruct ts = i.next();
			if (Throwable.class.isAssignableFrom(ts.getPeerClass())) {
				exceptions.add(ts);
				i.remove();
			}
		}
		
		service.setName(commonServiceClass.getSimpleName());
		service.setMethods(thriftMethods);
		return service;
	}

	private List<ThriftMethod> rebuildBySoureInfo(List<ThriftMethod> thriftMethods) {
		if (this.srcDir == null) {
			return thriftMethods;
		}
		List<JavaMethod> ms = CommonUtils.getMethodsFromSource(srcDir, this.commonServiceClass);
		if (!ms.isEmpty() && ms.size() == thriftMethods.size()) {
			List<ThriftMethod> copyMethods = new ArrayList<ThriftMethod>();
			for (JavaMethod jm : ms) {
				for (ThriftMethod tm : thriftMethods) {
					if (!tm.getName().equals(jm.getName())) { 
						continue;
					}
					// find the method in source
					tm.setJavaMethod(jm);
					copyMethods.add(tm);
					break;
				}
			}
			thriftMethods = copyMethods;
		} else {
			log.warn("get method order fail, ms size:" + ms.size());
		}
		
		return thriftMethods;
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
			thriftStructBuilder.buildThriftStruct(class1, structs, enums);
		}
		
		Class<?> returnClass = method.getReturnType();
		ThriftType thriftType = ThriftType.fromJavaType(returnClass);
		if (thriftType.isStruct()) {
			thriftStructBuilder.buildThriftStruct(returnClass, structs, enums);
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
        log.info("get arg class size:" + argClasses.length);
		
		for (Class<?> clazz : argClasses) {
			if(this.isBasicType(clazz)) {
				continue;
			}
			if(this.isCollectionType(clazz)) {
				continue;
			}
            log.debug(" start build method arg class :" + clazz);
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
	
	private List<ThriftStruct> buildThriftExceptions(Method method, List<ThriftStruct> structs, List<ThriftEnum> enums) {
        Type[] ts = method.getGenericExceptionTypes();
        List<ThriftStruct> tsList = new ArrayList<ThriftStruct>();
        for (Type t : ts) {
        	ThriftStruct ths = thriftStructBuilder.buildThriftStruct((Class<?>)t, structs, enums);
        	tsList.add(ths);
        }
        return tsList;
	}
	
	public List<ThriftStruct> getStructs() {
		return structs;
	}

	public List<ThriftEnum> getEnums() {
		return enums;
	}

    public void setIncludeSuper(boolean isInclude) {
        this.thriftStructBuilder.setIncludeSuperField(isInclude);
    }

    public void setServiceClass(Class<?> service) {
        this.commonServiceClass = service;
    }
    
    public String getSrcDir() {
		return srcDir;
	}

	public void setSrcDir(String srcDir) {
		this.srcDir = srcDir;
	}

	public List<ThriftStruct> getExceptions() {
		return exceptions;
	}

	public void setExceptions(List<ThriftStruct> exceptions) {
		this.exceptions = exceptions;
	}

}
