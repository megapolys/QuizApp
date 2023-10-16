<#-- @ftlvariable name="task" type="com.example.servingwebcontent.domain.quiz.QuizTask" -->
<#import "../parts/common.ftlh" as c>
<#import "../parts/taskForm.ftlh" as task>
<#import "../parts/decisions.ftlh" as d>
<#import "../parts/modal.ftlh" as modal>
<@c.page>
    <a class="btn btn-primary mb-3" href="/quiz/list">Назад</a>
    <h3 class="mb-3">Страница редактирования теста "${quiz.name}"</h3>
    <div class="row mb-5">
        <form action="/quiz/rename/${quiz.id?c}" method="post">
            <div class="input-group">
                <input class="form-control" style="width: 5%" type="text" value="${quiz.shortName}" name="shortName"/>
                <input class="form-control" style="width: 75%" type="text" value="${quiz.name}" name="name"
                       placeholder="Неужели так сложно придумать название для теста"/>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button class="btn btn-primary" type="submit">Переименовать</button>
            </div>
        </form>
    </div>
    <div class="row mb-5 text-bg-light">
        <#assign path = (taskForm.taskType == FIVE_VARIANT)?string("/quiz/task/add/fiveVariant",
        (taskForm.taskType == YES_OR_NO)?string("/quiz/task/add/yesOrNo", ""))>
        <@task.task path taskForm groups decisions false/>
    </div>
    <table class="table table-striped">
        <thead>
        <tr>
            <th style="width: 5%"></th>
            <th style="width: 55%"></th>
            <th style="width: 10%"></th>
            <th style="width: 10%"></th>
            <th style="width: 20%"></th>
        </tr>
        </thead>
        <tbody>
        <#list taskList as task>
            <tr>
                <td>${task.position}</td>
                <td>${(task.fiveVariantTask.questionText)!(task.yesOrNoTask.questionText)?truncate(50, "...")}</td>
                <#assign type = task.fiveVariantTask?has_content?string("fiveVariant", "yesOrNo")>
                <td><a href="/quiz/task/update/${type}/${task.id?c}">Изменить</a></td>
                <td>
                    <@modal.deleteConfirm path="/quiz/task/delete/${task.id?c}"
                    id="confirm-delete-task-${task.id}"
                    text="Точно удалить задание?"/>
                </td>
                <td><#if !task.decisions?has_content><div style="color: red">Решений не назначено</div></#if></td>
            </tr>
        <#else>
            <tr>
                <td colspan="4">Список вопросов пока пуст</td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>
