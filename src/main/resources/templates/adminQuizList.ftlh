<#import "parts/common.ftlh" as c>
<@c.page>
    <a class="btn btn-primary mb-3" href="/user/list">К списку пользователей</a>
    <h3 class="mb-3">Список опросников</h3>
    <table class="table">
        <thead>
        <tr>
            <th style="width: 60%">Название опросника</th>
            <th style="width: 10%">Балл</th>
            <th style="width: 10%"></th>
            <th style="width: 10%"></th>
            <th style="width: 10%"></th>
        </tr>
        </thead>
        <tbody>
        <#list quizzes as quiz>
            <#assign color=quiz.quizResult().complete?then(quiz.red()?then("table-danger", quiz.yellow()?then("table-warning", "table-success")), "")>
            <tr class="${color}">
                <td>${quiz.quizResult().quiz.shortName}. ${quiz.quizResult().quiz.name}</td>
                <td>${quiz.quizResult().complete?then(quiz.score()?c, "")}</td>
                <td>
                    <#if quiz.quizResult().complete>
                        <span>Тест пройден</span>
                    <#else>
                        <span>Тест в работе ${quiz.progress()}</span>
                    </#if>
                </td>
                <td>
                    <#if quiz.quizResult().complete>
                        <a href="/result/quiz/${userId?c}/${quiz.quizResult().id?c}">Результаты</a>
                    </#if>
                </td>
                <td>
                    <a href="/result/quiz/${userId?c}/delete/${quiz.quizResult().id?c}">Удалить</a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>