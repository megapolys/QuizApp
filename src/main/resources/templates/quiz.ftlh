<#-- @ftlvariable name="task" type="com.example.servingwebcontent.domain.quiz.QuizTask" -->
<#import "parts/common.ftlh" as c>
<#import "parts/taskForm.ftlh" as task>
<@c.page>
    <a class="btn btn-primary mb-3" href="/quiz/list">Назад</a>
    <h3 class="mb-3">Страница редактирования теста "${quiz.name}"</h3>
    <div class="row mb-5">
        <form action="/quiz/rename/${quiz.id}" method="get">
            <div class="input-group">
                <input class="form-control" style="width: 5%" type="text" value="${quiz.shortName}" name="shortName"/>
                <input class="form-control" style="width: 75%" type="text" value="${quiz.name}" name="name"
                       placeholder="Неужели так сложно придумать название для теста"/>
                <button class="btn btn-primary" type="submit">Переименовать</button>
            </div>
        </form>
    </div>
    <div class="row mb-5 text-bg-light">
        <#assign path = (taskForm.taskType == FIVE_VARIANT)?string("/quiz/task/add/fiveVariant",
        (taskForm.taskType == YES_OR_NO)?string("/quiz/task/add/yesOrNo", ""))>
        <form id="taskForm" class="row g-3" action="${path}" method="post"
              enctype="multipart/form-data">
            <div class="col-2 mb-3">
                <div class="input-group">
                    <span class="input-group-text">№</span>
                    <input id="positionValue" class="form-control" type="number" min="1"
                           value="${(taskForm.position)!?c}" name="position"/>
                </div>
            </div>
            <div class="col-10 mb-3">
                <div class="input-group col-9">
                    <span class="input-group-text">Текст задания</span>
                    <textarea id="inputText" class="form-control"
                              name="preQuestionText">${(taskForm.preQuestionText)!}</textarea>
                </div>
            </div>
            <div class="col-12 mb-3">
                <div class="input-group">
                    <span class="input-group-text">Текст вопроса</span>
                    <textarea id="inputText" class="form-control"
                              name="questionText">${(taskForm.questionText)!}</textarea>
                </div>
            </div>
            <div class="col-12 mb-3">
                <label for="formFile" class="form-label">Вспомогательная картинка</label>
                <input class="form-control" type="file" name="file" id="formFile">
            </div>
            <ul id="taskTabs" class="nav nav-tabs">
                <li class="nav-item">
                    <a class="nav-link ${(taskForm.taskType == FIVE_VARIANT)?string("active", "")}" href="##"
                       onclick="switchTab(this, '#fiveVariantTab', '/quiz/task/add/fiveVariant')">Пять вариантов (Никогда - Всегда)</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${(taskForm.taskType == YES_OR_NO)?string("active", "")}" href="##"
                       onclick="switchTab(this, '#yesOrNoTab', '/quiz/task/add/yesOrNo')" >Два варианта (Да, Нет)</a>
                </li>
            </ul>
            <div id="taskTypeTabs">
                <div id="fiveVariantTab" class="input-group mb-3" style="display: ${(taskForm.taskType == FIVE_VARIANT)?string("", "none")}">
                    <span class="input-group-text">Никогда</span>
                    <input type="number" name="firstWeight" class="form-control" step="0.5" placeholder="0"
                           value="${(taskForm.firstWeight)!?c}">
                    <span class="input-group-text">Редко</span>
                    <input type="number" name="secondWeight" class="form-control" step="0.5" placeholder="0"
                           value="${(taskForm.secondWeight)!?c}">
                    <span class="input-group-text">Иногда</span>
                    <input type="number" name="thirdWeight" class="form-control" step="0.5" placeholder="2"
                           value="${(taskForm.thirdWeight)!?c}">
                    <span class="input-group-text">Часто</span>
                    <input type="number" name="fourthWeight" class="form-control" step="0.5" placeholder="3"
                           value="${(taskForm.fourthWeight)!?c}">
                    <span class="input-group-text">Всегда</span>
                    <input type="number" name="fifthWeight" class="form-control" step="0.5" placeholder="4"
                           value="${(taskForm.fifthWeight)!?c}">
                </div>
                <div id="yesOrNoTab" class="input-group mb-3" style="display: ${(taskForm.taskType == YES_OR_NO)?string("", "none")}">
                    <span class="input-group-text">Да</span>
                    <input type="number" name="yesWeight" class="form-control" step="0.5" placeholder="4"
                           value="${(taskForm.yesWeight)!?c}">
                    <span class="input-group-text">Нет</span>
                    <input type="number" name="noWeight" class="form-control" step="0.5" placeholder="0"
                           value="${(taskForm.noWeight)!?c}">
                </div>
            </div>
            <div class="mb-3">
                <#if decisions?has_content>
                    <#list decisions as decision>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" name="decisions"
                                   value="${decision.id}"
                                   id="decision-${decision.id}" ${(taskForm.decisions)!?map(d -> d.id)?seq_contains(decision.id)?string("checked", "")}>
                            <label class="form-check-label" for="decision-${decision.id}">
                                ${decision.name}
                            </label>
                        </div>
                    </#list>
                </#if>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <input type="hidden" name="quiz" value="${quiz.id?c}"/>
            <div class="row mb-2 justify-content-md-center">
                <button class="btn btn-primary col-3" type="submit">Добавить вопрос</button>
            </div>
        </form>
    </div>
    <table class="table table-striped">
        <thead>
        <tr>
            <th style="width: 5%"></th>
            <th style="width: 55%"></th>
            <th style="width: 20%"></th>
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
                <td><a href="/quiz/task/delete/${task.id?c}">Удалить</a></td>
            </tr>
        <#else>
            <tr>
                <td colspan="4">Список вопросов пока пуст</td>
            </tr>
        </#list>
        </tbody>
    </table>

</@c.page>

<script type="text/javascript">
    function switchTab(el, tab_id, path) {
        $("#taskTabs").children('li').children('a').removeClass("active");
        $("#taskTypeTabs").children().hide();

        $(el).addClass("active");
        $(tab_id).show();

        $("#taskForm").attr("action", path);
    }
</script>