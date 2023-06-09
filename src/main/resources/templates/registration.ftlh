<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>
<@c.page>
    <h3>Add new user</h3>
    <div>${message!}</div>
    <@l.login "/registration" true />
    <a href="/testMail">testMail</a>
</@c.page>