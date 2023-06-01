<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>
<@c.page>
    <h3>Авторизация</h3>
    <@l.login "/login" false />
</@c.page>