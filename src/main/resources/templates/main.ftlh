<#import "parts/common.ftlh" as c>
<@c.page>
    <div>Welcome, <span>${userName}</span></div>
    <h3>Search by name</h3>
    <form action="/main" method="get">
        <input id="findName" type="text" name="findName" value="${findNameFilter!}">
        <input type="submit" value="Search">
    </form>
    <table>
        <tr>
            <th>Name</th>
            <th>Email</th>
        </tr>
        <#list users as user>
            <tr>
                <td>${user.username}</td>
                <td>${user.email}</td>
            </tr>
        <#else>
            <tr>
                <td colspan="2">No users</td>
            </tr>
        </#list>
    </table>
</@c.page>