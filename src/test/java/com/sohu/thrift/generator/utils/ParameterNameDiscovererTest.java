package com.sohu.thrift.generator.utils;

import java.lang.reflect.Method;

import org.junit.Test;

import junit.framework.Assert;

public class ParameterNameDiscovererTest {

    private ParameterNameDiscoverer dis = new ParameterNameDiscoverer();

    @Test
    public void testGetParameterNames() throws NoSuchMethodException, SecurityException {
    	Method m = ParameterNameDiscoverer.class.getDeclaredMethod("getParameterNames", Method.class);
    	String [] params = dis.getParameterNames(m);
    	Assert.assertTrue(params.length == 0 || params.length == 1);
    }
    
    
}
