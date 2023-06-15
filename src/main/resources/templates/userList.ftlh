<#import "parts/common.ftlh" as c>
<@c.page>
    <h3>Список пользователей</h3>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Имя</th>
            <th>Почта</th>
            <th>Роли</th>
            <th>Тесты</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <td>${user.username}</td>
                <td>${user.email}</td>
                <td><#list user.roles as role>${role}<#sep>, </#list></td>
                <td><#if user.quizzes?has_content><#list user.quizzes as quiz>${quiz.shortName}<#sep>, </#list><#else>Не назначено</#if></td>
                <td><a href="/user/${user.id}">Изменить</a></td>
                <td><a href="/result/quiz/${user.id}">Результаты</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>