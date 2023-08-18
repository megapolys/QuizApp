<#import "../parts/common.ftlh" as c>
<#import "../parts/invoke.ftlh" as invoke>
<@c.page>
    <a class="btn btn-primary mb-3" href="/result/quiz/${userId?c}/${quizResultId?c}">Назад</a>
    <@invoke.invoke "/result/quiz/${userId?c}/${quizResultId?c}/${task.id?c}" true/>
</@c.page>