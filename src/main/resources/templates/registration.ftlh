<#import "parts/common.ftlh" as c>
<@c.page>
    <h3>Add new user</h3>
    <div>${message!""}</div>
    <form action="/registration" method="post">
        <div><label> User Name : <input type="text" name="username"/> </label></div>
        <div><label> Password: <input type="password" name="password"/> </label></div>
        <div><label> Email: <input type="text" name="email"/> </label></div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <div><input type="submit" value="Sign In"/></div>
    </form>
</@c.page>