<#-- @ftlvariable name="fiveVariantTask" type="com.example.servingwebcontent.domain.quiz.task.FiveVariantTask" -->
<#-- @ftlvariable name="yesOrNoTask" type="com.example.servingwebcontent.domain.quiz.task.YesOrNoTask" -->
<#-- @ftlvariable name="quizTask" type="com.example.servingwebcontent.domain.quiz.QuizTask" -->
<#import "decisions.ftlh" as d>
<#macro task path taskForm groups decisions update>
    <#-- @ftlvariable name="taskForm" type="com.example.servingwebcontent.domain.validation.TaskForm" -->
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
        <div class="col-6 mb-3">
            <label for="formFile" class="form-label">Вспомогательная картинка</label>
            <input class="form-control" type="file" name="file" id="formFile">
        </div>
        <div class="col-6 mb-3">
            <#if taskForm.fileName??>
                <div>Выбрана картинка: ${taskForm.fileName?substring(37)}</div>
                <div>
                    <input class="form-check-input" type="checkbox" name="deleteFile" id="deleteFile_id">
                    <label class="form-check-label" for="deleteFile_id">Удалить картинку</label>
                </div>
            </#if>
        </div>
        <#if update>
            <#if taskForm.taskType == FIVE_VARIANT>
                <@fiveVariantTab taskForm/>
            </#if>
            <#if taskForm.taskType == YES_OR_NO>
                <@yesOrNoTab taskForm/>
            </#if>
        <#else>
            <ul id="taskTabs" class="nav nav-tabs">
                <li class="nav-item">
                    <a class="nav-link ${(taskForm.taskType == FIVE_VARIANT)?string("active", "")}" href="##"
                       onclick="switchTab(this, '#fiveVariantTab', '/quiz/task/add/fiveVariant')">Пять вариантов
                        (Никогда - Всегда)</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${(taskForm.taskType == YES_OR_NO)?string("active", "")}" href="##"
                       onclick="switchTab(this, '#yesOrNoTab', '/quiz/task/add/yesOrNo')">Два варианта (Да, Нет)</a>
                </li>
            </ul>
            <div id="taskTypeTabs">
                <@fiveVariantTab taskForm/>
                <@yesOrNoTab taskForm/>
            </div>
        </#if>
        <div class="mb-3">
            <h5 class="mb-3">Решения</h5>
            <@d.listOfChosenDecisions decisions_=(taskForm.decisions)!/>
            <@d.decisionsCheckbox groups=groups decisions_=decisions checkedDecisions=(taskForm.decisions)!/>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <input type="hidden" name="quiz" value="${quiz.id?c}"/>
        <input type="hidden" name="quizTask" value="${(quizTask.id)!?c}"/>
        <div class="row mb-2 justify-content-md-center">
            <button class="btn btn-primary col-3"
                    type="submit"><#if update>Обновить вопрос<#else>Добавить вопрос</#if></button>
        </div>
    </form>
</#macro>

<#macro fiveVariantTab taskForm>
    <div id="fiveVariantTab" class="input-group mb-3"
         style="display: ${(taskForm.taskType == FIVE_VARIANT)?string("", "none")}">
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
</#macro>

<#macro yesOrNoTab taskForm>
    <div id="yesOrNoTab" class="input-group mb-3"
         style="display: ${(taskForm.taskType == YES_OR_NO)?string("", "none")}">
        <span class="input-group-text">Да</span>
        <input type="number" name="yesWeight" class="form-control" step="0.5" placeholder="4"
               value="${(taskForm.yesWeight)!?c}">
        <span class="input-group-text">Нет</span>
        <input type="number" name="noWeight" class="form-control" step="0.5" placeholder="0"
               value="${(taskForm.noWeight)!?c}">
    </div>
</#macro>