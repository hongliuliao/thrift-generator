
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

<#list serviceList as service>
	service ${service.name} {
		 <#list service.methods as method>
		 	${method.returnGenericType.toThriftString()} ${method.name}<@compress single_line=true>
			(<#list method.methodArgs as arg>${arg_index + 1}:${arg.genericType.toThriftString()} ${arg.name}<#if arg_has_next>, </#if></#list>)<#if method_has_next>,</#if>
			</@compress>
		        	
		 </#list>
	}
</#list>
