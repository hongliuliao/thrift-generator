package com.sohu.thrift.generator.utils;

import java.util.List;

import org.junit.Test;

import com.sohu.thrift.generator.test.ICommonUserService;
import com.thoughtworks.qdox.model.JavaMethod;

import junit.framework.Assert;

public class CommonUtilsTest {
	@Test
    public void testGetMethodsFromSrc() {
    	List<JavaMethod> ms = CommonUtils.getMethodsFromSource("src/test/java", ICommonUserService.class);
    	Assert.assertEquals("login", ms.get(0).getName());
    	Assert.assertEquals("getUserById", ms.get(1).getName());
    	Assert.assertEquals("saveUser", ms.get(2).getName());
    	Assert.assertEquals("user login", ms.get(0).getComment());
    	Assert.assertEquals("param", ms.get(0).getTags().get(0).getName());
    	Assert.assertEquals("id user id", ms.get(0).getTags().get(0).getValue());
    }

}
