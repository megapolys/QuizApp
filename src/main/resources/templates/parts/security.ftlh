<#assign known = SPRING_SECURITY_CONTEXT??>

<#if known>
    <#assign user = SPRING_SECURITY_CONTEXT.authentication.principal>
    <#assign name = user.getUsername()>
    <#assign isAdmin = user.isAdmin()>
<#else>
    <#assign isAdmin = false>
</#if>