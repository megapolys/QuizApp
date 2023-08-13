<#import "parts/common.ftlh" as c>
<#import "parts/taskForm.ftlh" as task>
<@c.page>
    <a class="btn btn-primary mb-3" href="/quiz/${quiz.id?c}">Назад</a>
    <@task.task path taskForm groups decisions true/>
</@c.page>