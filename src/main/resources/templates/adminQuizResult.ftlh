<#import "parts/common.ftlh" as c>
<@c.page>
    <a class="btn btn-primary mb-3" href="/result/quiz/${userId}">Назад</a>
    <h3 class="mb-3">Сводная таблица решений</h3>
    <table class="table mb-5">
        <thead>
        <tr>
            <th>Решение</th>
            <th>Баллы</th>
        </tr>
        </thead>
        <tbody>
        <#list result.decisions() as decision>
            <tr>
                <td>${decision.decision().name}</td>
                <td>${decision.score()?c}</td>
            </tr>
        </#list>
        </tbody>
    </table><h3 class="mb-3">Список заданий с текстом</h3>
    <table class="table mb-5">
        <thead>
        <tr>
            <th style="width: 5%">Номер</th>
            <th style="width: 60%">Задание</th>
            <th style="width: 10%">Баллы</th>
            <th style="width: 25%"></th>
        </tr>
        </thead>
        <tbody>
        <#list result.quizResult().taskList as task>
            <#if task.text??>
                <tr>
                    <td>${task.task.position}</td>
                    <td>${(task.task.fiveVariantTask.questionText)!(task.task.yesOrNoTask.questionText)?truncate(50, "...")}</td>
                    <td>${task.altScore}</td>
                    <td><a href="/result/quiz/${userId?c}/${result.quizResult().id?c}/${task.id?c}">Посмотреть</a></td>
                </tr>
            </#if>
        </#list>
        </tbody>
    </table>
</@c.page>