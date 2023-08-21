<#import "../parts/common.ftlh" as c>
<@c.page>
    <h3>Список пользователей</h3>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Имя</th>
            <th>Почта</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <td>${user.username}</td>
                <td>${user.email!}</td>
                <td><a href="/result/quiz/${user.id}">Управление</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>