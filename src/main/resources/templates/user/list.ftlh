<#import "../parts/common.ftlh" as c>
<@c.page>
    <h3>Список пользователей</h3>
    <table class="table table-striped">
        <thead>
        <tr>
            <th style="width: 30%">Имя</th>
            <th style="width: 30%">Почта</th>
            <th style="width: 20%"></th>
            <th style="width: 20%"></th>
        </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <td>${user.username}</td>
                <td>${user.email!}</td>
                <td><a href="/result/quiz/${user.id?c}">Управление тестами</a></td>
                <td><a href="/result/topic/${user.id?c}">Управление анализами</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>