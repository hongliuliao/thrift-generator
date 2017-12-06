
	namespace java ${thriftServicePackage}

<#list enumList as thriftEnum>
	enum ${thriftEnum.name} {
		<#list thriftEnum.fields as field>
			${field.name} = ${field.value}<#if field_has_next>,</#if>
		</#list>
	}
</#list>

<#list structList as struct>
	struct ${struct.name} {
		<#list struct.fields as field>
			${field_index + 1}:${field.genericType.toThriftString()} ${field.name}<#if field_has_next>,</#if>
		</#list>
	}
</#list>

<#list exceptions as ex>
	exception ${ex.name} {
		<#list ex.fields as field>
			${field_index + 1}:${field.genericType.toThriftString()} ${field.name}<#if field_has_next>,</#if>
		</#list>
	}
</#list>

<#list serviceList as service>
	service ${service.name} {
		 <#list service.methods as method>
		    <#if method.comments>
		    	/**
		    	 * ${method.comment}
		      <#list method.docTags as dt>
		         * @${dt.name} ${dt.value} 
		      </#list>
		         */
		    </#if>
		 	${method.returnGenericType.toThriftString()} ${method.name}<@compress single_line=true>
			(<#list method.methodArgs as arg>${arg_index + 1}:${arg.genericType.toThriftString()} ${arg.name}<#if arg_has_next>, </#if></#list>)<#if method.exceptions?size != 0> throws (<#list method.exceptions as ex>${ex_index+1}:${ex.name} ex${ex_index+1}<#if ex_has_next>,</#if></#list>)</#if><#if method_has_next>,</#if>
			</@compress>
		        	
		 </#list>
	}
</#list>
