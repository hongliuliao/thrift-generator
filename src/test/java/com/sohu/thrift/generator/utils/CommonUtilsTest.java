package com.sohu.thrift.generator.utils;

import java.util.List;

import org.junit.Test;

import com.sohu.thrift.generator.test.ICommonUserService;

import junit.framework.Assert;

public class CommonUtilsTest {
	@Test
    public void testGetMethodsFromSrc() {
    	List<String> ms = CommonUtils.getMethodsFromSource("src/test/java", ICommonUserService.class);
    	Assert.assertEquals("login", ms.get(0));
    	Assert.assertEquals("getUserById", ms.get(1));
    	Assert.assertEquals("saveUser", ms.get(2));
    }

}
