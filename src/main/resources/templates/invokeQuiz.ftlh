<#import "parts/common.ftlh" as c>
<#import "parts/invoke.ftlh" as invoke>
<@c.page>
    <@invoke.invoke "/invokeQuiz/${task.quiz.id?c}" false/>
    <div class="progress" role="progressbar" aria-label="Basic example" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">
        <div class="progress-bar" style="width: ${progress?c}%"></div>
    </div>
</@c.page>