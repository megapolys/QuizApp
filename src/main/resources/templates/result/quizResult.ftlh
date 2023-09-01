<#import "../parts/common.ftlh" as c>
<#import "../parts/decisions.ftlh" as d>
<@c.page>
    <div class="row mb-3">
        <div class="col-2">
            <a class="btn btn-primary" href="/result/quiz/${userId?c}">Назад</a>
        </div>
        <div class="col-10">
            <h3>${result.quizResult().quiz.name}</h3>
        </div>
    </div>
    <@d.resultDecisionsTable result.decisions()!/>
    <h4 class="mb-3">Список заданий с текстом</h4>
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
                <#assign color=task.altScore?has_content?then("table-success", task.text?has_content?then("table-warning", ""))>
                <tr class="${color}">
                    <td>${task.task.position}</td>
                    <td>${(task.task.fiveVariantTask.questionText)!(task.task.yesOrNoTask.questionText)?truncate(50, "...")}</td>
                    <td>${task.resultScore}</td>
                    <td><a href="/result/quiz/${userId?c}/${result.quizResult().id?c}/${task.id?c}">Посмотреть</a></td>
                </tr>
            </#if>
        </#list>
        </tbody>
    </table>
</@c.page>