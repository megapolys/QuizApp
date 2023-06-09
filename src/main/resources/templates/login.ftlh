<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>
<@c.page>
    <h3>Авторизация</h3>
    <div>${message!}</div>
    <@l.login "/login" false />
</@c.page>