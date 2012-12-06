/**
 * 
 */
package com.sohu.thrift.generator.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import com.sohu.thrift.generator.Generic;
import com.sohu.thrift.generator.ThriftType;

/**
 * It is CommontUtils?? May be call ReflectionUtils is better? :)
 * @author hongliuliao
 *
 * createTime:2012-9-11 上午10:54:48
 */
public class CommonUtils {
	private static final Log log = LogFactory.getLog(CommonUtils.class);
	
	public static boolean isBasicType(Class<?> clazz) {
		if(Number.class.isAssignableFrom(clazz)) {
			return true;
		}
		if(clazz == int.class || clazz == short.class || clazz == long.class) {
			return true;
		}
		if(clazz == boolean.class || clazz == Boolean.class) {
			return true;
		}
		if(clazz == String.class) {
			return true;
		}
		return false;
	}
	
	public static boolean isCollectionType(Class<?> clazz) {
		if(List.class.isAssignableFrom(clazz) || Set.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz)) {
			return true;
		}
		return false;
	}
	
	public static Class<?> forName(String className) {
		try {
			return Class.forName(className);
		} catch (Exception e) {
			log.error("Class not found",e);
			throw new RuntimeException("Class not found",e);
		}
	}
	
	public static Object newInstance(String className) {
		try {
			Class<?> clazz = forName(className);
			return clazz.newInstance();
		} catch (Exception e) {
			log.error("newInstance error!", e);
			throw new RuntimeException("newInstance error!", e);
		}
	}
	
	public static Object newInstance(Class<?> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			log.error("newInstance error!", e);
			throw new RuntimeException("newInstance error!", e);
		}
	}
	
	public static Method findMethodByName(Class<?> clazz, String methodName) {
		Method[] methods = clazz.getDeclaredMethods();
		Map<String, Method> methodMap = new HashMap<String, Method>();
		for (Method method : methods) {
			if((method.getModifiers() & Modifier.FINAL) != 0) {
				continue;
			}
			String tempName = method.getName();
			if(methodMap.get(tempName) != null) {
				continue;
			}
			methodMap.put(method.getName(), method);
		}
		return methodMap.get(methodName);
	}
	
	/**
	 * @param protoClass
	 * @return
	 */
	public static Builder getBuilder(Class<?> protoClass) {
		Method newBuildMethod = findMethod(protoClass, "newBuilder");
		Object builder = invokeMethod(newBuildMethod, protoClass);
		return (Builder) builder;
	}
	
	public static Method findMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
		try {
			return clazz.getDeclaredMethod(methodName, paramTypes);
		} catch (Exception e) {
			throw new RuntimeException("find method error!", e);
		}
	}
	
	public static Field findField(Class<?> clazz, String fieldName) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (Exception e) {
			throw new RuntimeException("Find field error!", e);
		}
	}
	
	public static Object invokeMethod(Method method, Object target, Object... args) {
		try {
			return method.invoke(target, args);
		} catch (Exception e) {
			throw new RuntimeException("invoke target error!", e);
		}
	}
	
	public static List<FieldDescriptor> getFieldDescriptors(Message.Builder builder) {
		return builder.getDescriptorForType().getFields();
	}
	
	public static Class<?>[] getFieldGenericType(Field field) {
		Type genericFieldType = field.getGenericType();
		
		if (genericFieldType instanceof ParameterizedType) {
			ParameterizedType aType = (ParameterizedType) genericFieldType;
			Type[] fieldArgTypes = aType.getActualTypeArguments();
			return (Class<?>[]) fieldArgTypes;
		}
		return null;
	}
	
	public static List<Class<?>> getMethodGenericParameterTypes(Method method) { 
		List<Class<?>> results = new ArrayList<Class<?>>();
		Type[] genericParameterTypes = method.getGenericParameterTypes();
		for (Type genericParameterType : genericParameterTypes) {
			if (genericParameterType instanceof ParameterizedType) {
				ParameterizedType aType = (ParameterizedType) genericParameterType;
				Type[] parameterArgTypes = aType.getActualTypeArguments();
				for (Type parameterArgType : parameterArgTypes) {
					Class<?> parameterArgClass = (Class<?>) parameterArgType;
					results.add(parameterArgClass);
				}
				return results;
			}
		}
		return results;
	}
	
	public static void getGenericParameterTypes(Type type, List<Class<?>> genericTypes) {
		if (type instanceof ParameterizedType) {
			ParameterizedType aType = (ParameterizedType) type;
			Type[] parameterArgTypes = aType.getActualTypeArguments();
			for (Type parameterArgType : parameterArgTypes) {
				if(parameterArgType instanceof ParameterizedType) {
					getGenericParameterTypes(parameterArgType, genericTypes);
					continue;
				}
				Class<?> parameterArgClass = (Class<?>) parameterArgType;
				genericTypes.add(parameterArgClass);
			}
		}
	}
	
	public static <T> void removeRepeat(List<T> list) {
		Set<T> set = new HashSet<T>();
		for (Iterator<T> i = list.iterator();i.hasNext();) {
			if(!set.add(i.next())) {
				i.remove();
			}
		}
	}
	
	public static String convertJavaSimpleToThrift(String javaDesc) {
		if(javaDesc.indexOf("List") != -1) {
			return "list";
		}
		
		if(javaDesc.indexOf("Map") != -1) {
			return "map";
		}
		
		if(javaDesc.indexOf("Set") != -1) {
			return "set";
		}
		
		javaDesc = javaDesc.replace("Short", "short");
		javaDesc = javaDesc.replace("short", "short");
		javaDesc = javaDesc.replace("Integer", "i32");
		javaDesc = javaDesc.replace("int", "i32");
		javaDesc = javaDesc.replace("Long", "i64");
		javaDesc = javaDesc.replace("long", "i64");
		javaDesc = javaDesc.replace("String", "string");
		
		return javaDesc;
	}
	
	public static String convertJavaToThrift(String javaDesc) {
		javaDesc = javaDesc.replace("java.lang.Short", "short");
		javaDesc = javaDesc.replace("java.lang.Integer", "i32");
		javaDesc = javaDesc.replace("java.lang.Long", "i64");
		javaDesc = javaDesc.replace("java.lang.String", "string");
		javaDesc = javaDesc.replace("java.util.List", "list");
		javaDesc = javaDesc.replace("java.util.Set", "set");
		javaDesc = javaDesc.replace("java.util.Map", "map");
		return javaDesc;
	}
	
	public static String convertJavaTypeToSimple(String javaDesc) {
		if((javaDesc.indexOf("List") != -1) || (javaDesc.indexOf("Set") != -1)
				|| javaDesc.indexOf("Map") != -1) {
			javaDesc = javaDesc.replace("java.lang.Short", "Short");
			javaDesc = javaDesc.replace("java.lang.Integer", "Integer");
			javaDesc = javaDesc.replace("java.lang.Long", "Long");
		} else {
			javaDesc = javaDesc.replace("java.lang.Short", "short");
			javaDesc = javaDesc.replace("java.lang.Integer", "int");
			javaDesc = javaDesc.replace("java.lang.Long", "long");
		}
		
		javaDesc = javaDesc.replace("java.lang.String", "String");
		javaDesc = javaDesc.replace("java.util.List", "List");
		javaDesc = javaDesc.replace("java.util.Set", "Set");
		javaDesc = javaDesc.replace("java.util.Map", "Map");
		return javaDesc;
	}
	
	public static List<Class<?>> getMethodReturnTypeRelationClasses(Method method) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		getGenericParameterTypes(method.getGenericReturnType(), classes);
		for (Iterator<Class<?>> i = classes.iterator();i.hasNext();) {
			Class<?> clazz = i.next();
			if(isBasicType(clazz) || isCollectionType(clazz)) {
				i.remove();
			}
		}
		return classes;
	}
	
	public static List<Class<?>> getMethodArgsRelationClasses(Type argType) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if(argType instanceof ParameterizedType) {
			getGenericParameterTypes(argType, classes);
		}else if(!isBasicType((Class<?>)argType)) {
			if(! isCollectionType((Class<?>)argType)) {
				classes.add((Class<?>)argType);
			}else {
				getGenericParameterTypes(argType, classes);
			}
		}
		for (Iterator<Class<?>> i = classes.iterator();i.hasNext();) {
			Class<?> clazz = i.next();
			if(isBasicType(clazz) || isCollectionType(clazz)) {
				i.remove();
			}
		}
		return classes;
	}
	
	public static Generic getGenericsByType(Type type) {
		if(!(type instanceof ParameterizedType)) {
			return null;
		}
		Generic generic = new Generic();
		ParameterizedType parameterizedType = (ParameterizedType) type;
		Type[] types = parameterizedType.getActualTypeArguments();
		for (Type type2 : types) {
			if(type2 instanceof ParameterizedType) {
				generic.addGeneric(getGenericsByType(type2));
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
	
	public static Field[] getCanSerializeField(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		List<Field> canSerializeFields = new ArrayList<Field>();
		for (Field field : fields) {
			if(field.getName().equals("__PARANAMER_DATA")) {
				continue;
			}
			if(field.isEnumConstant()) {
				continue;
			}
			if(field.getType() == Date.class) {
				continue;
			}
			if((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) {
				continue;
			}
			canSerializeFields.add(field);
		}
		Field[] newFields = new Field[canSerializeFields.size()];
		return canSerializeFields.toArray(newFields);
	}
	
	public static Class<?> getRawClass(Type type) {
		if(type instanceof ParameterizedType) {
			return (Class<?>) ((ParameterizedType) type).getRawType();
		}else {
			return (Class<?>) type;
		}
	}
	
	public static String underlineField2JavaField(String ulField){
		if(ulField.indexOf("_") == -1) {
			return ulField;
		}
//		按"_"分割之后让首字母大写(除了第一个单词)
		String[] names=ulField.toLowerCase().split("_");
		StringBuffer javaField=new StringBuffer(names[0]);
		for(int i=1;i<names.length;i++){
			javaField.append(CommonUtils.getFirstUpper(names[i]));
		}
		return javaField.toString();
	}
	
	public static String getFirstUpper(String str){
		return str.substring(0, 1).toUpperCase()+str.substring(1);
	}
	
}
