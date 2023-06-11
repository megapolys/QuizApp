<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>
<@c.page>
    <h3>Редактирование профиля</h3>
    <@l.login "/user/edit" true />
</@c.page>