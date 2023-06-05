<#import "parts/common.ftlh" as c>
<@c.page>
    <h3>Список пользователей</h3>
    <table>
        <thead>
            <tr>
                <th>Имя</th>
                <th>Почта</th>
                <th>Роли</th>
                <th>Тесты</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <#list users as user>
                <tr>
                    <td>${user.username}</td>
                    <td>${user.email}</td>
                    <td><#list user.roles as role>${role}<#sep>, </#list></td>
                    <td><#list user.quizzes as quiz>${quiz.shortName}<#sep>, </#list></td>
                    <td><a href="/user/${user.id}">Изменить</a></td>
                </tr>
            </#list>
        </tbody>
    </table>
</@c.page>