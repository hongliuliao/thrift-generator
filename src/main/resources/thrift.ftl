
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
			<#if field.thriftType.basicType || field.thriftType.collection>
			${field_index + 1}:${field.thriftType.value}${field.genericsType} ${field.name}<#if field_has_next>,</#if>
			<#elseif field.thriftType.enum || field.thriftType.struct>
			${field_index + 1}:${field.thriftType.javaTypeName}${field.genericsType} ${field.name}<#if field_has_next>,</#if>
			<#else>
			NOT SUPPORT
			</#if>
		</#list>
	}
</#list>

<#list serviceList as service>
	service ${service.name} {
		 <#list service.methods as method>
		 	${method.returnType} ${method.name}(<#list method.methodArgs as arg>${arg_index + 1}:${arg.type} ${arg.name}<#if arg_has_next>,</#if></#list>)<#if method_has_next>,</#if>
		 </#list>
	}
</#list>