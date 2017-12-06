/**
 * 
 */
package com.sohu.thrift.generator.utils;

import java.lang.reflect.Method;

import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;

/**
 * @author hongliuliao
 *
 * createTime:2012-12-6 下午3:12:38
 */
public class ParameterNameDiscoverer {
	
	Paranamer paranamer = new CachingParanamer();
	
	public String[] getParameterNames(Method arg0) {
		return paranamer.lookupParameterNames(arg0, false);
	}

}
