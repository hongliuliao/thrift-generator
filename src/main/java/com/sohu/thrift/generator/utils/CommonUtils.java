/**
 * 
 */
package com.sohu.thrift.generator.utils;

import java.io.File;
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

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;

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
	
    public static String getStaticString(Class<?> clazz, String fieldName) {
		try {
			Field f = clazz.getDeclaredField(fieldName);
            return (String) f.get(clazz);
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
	
	public static String column2PropertyName(String columnName) {
		if(columnName.indexOf("_") == -1) {
			return columnName;
		}
//		按"_"分割之后让首字母大写(除了第一个单词)
		String[] names=columnName.toLowerCase().split("_");
		StringBuffer javaField=new StringBuffer(names[0]);
		for(int i=1;i<names.length;i++){
			javaField.append(getFirstUpper(names[i]));
		}
		return javaField.toString();
	}
	
	public static String getFirstUpper(String str){
		return str.substring(0, 1).toUpperCase()+str.substring(1);
	}
	
	public static String getFirstLower(String str){
		return str.substring(0, 1).toLowerCase()+str.substring(1);
	}
	
	public static List<JavaMethod> getMethodsFromSource(String dir, Class<?> c) {
		JavaProjectBuilder builder = new JavaProjectBuilder();
		try {
			builder.addSourceTree(new File(dir));
			JavaClass jc = builder.getClassByName(c.getName());
			return jc.getMethods();
			
		} catch (Exception e) {
			throw new RuntimeException("get method from path err", e);
		}
	}
}
