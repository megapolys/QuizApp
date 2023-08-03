<#-- @ftlvariable name="fiveVariantTask" type="com.example.servingwebcontent.domain.quiz.task.FiveVariantTask" -->
<#-- @ftlvariable name="yesOrNoTask" type="com.example.servingwebcontent.domain.quiz.task.YesOrNoTask" -->
<#-- @ftlvariable name="quizTask" type="com.example.servingwebcontent.domain.quiz.QuizTask" -->

<#macro fiveVariant path>
    <form class="row g-3" action="${path}" method="post"
          enctype="multipart/form-data">
        <div class="col-2 mb-3">
            <div class="input-group">
                <span class="input-group-text">№</span>
                <input id="positionValue" class="form-control" type="number" min="1"
                       value="${((quizTask.position)!position)!?c}" name="position"/>
            </div>
        </div>
        <div class="col-10 mb-3">
            <div class="input-group col-9">
                <span class="input-group-text">Текст вопроса</span>
                <textarea id="inputText" class="form-control"
                          name="questionText">${(fiveVariantTask.questionText)!}</textarea>
            </div>
        </div>
        <div class="col-12 mb-3">
            <label for="formFile" class="form-label">Вспомогательная картинка</label>
            <input class="form-control" type="file" name="file" id="formFile">
        </div>
        <div class="input-group mb-3">
            <span class="input-group-text">Никогда</span>
            <input type="number" name="firstWeight" class="form-control" step="0.5" placeholder="0"
                   value="${(fiveVariantTask.firstWeight)!?c}">
            <span class="input-group-text">Редко</span>
            <input type="number" name="secondWeight" class="form-control" step="0.5" placeholder="0"
                   value="${(fiveVariantTask.secondWeight)!?c}">
            <span class="input-group-text">Иногда</span>
            <input type="number" name="thirdWeight" class="form-control" step="0.5" placeholder="2"
                   value="${(fiveVariantTask.thirdWeight)!?c}">
            <span class="input-group-text">Часто</span>
            <input type="number" name="fourthWeight" class="form-control" step="0.5" placeholder="3"
                   value="${(fiveVariantTask.fourthWeight)!?c}">
            <span class="input-group-text">Всегда</span>
            <input type="number" name="fifthWeight" class="form-control" step="0.5" placeholder="4"
                   value="${(fiveVariantTask.fifthWeight)!?c}">
        </div>
        <div class="mb-3">
            <#if decisions?has_content>
                <#list decisions as decision>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="decisions"
                               value="${decision.id}"
                               id="decision-${decision.id}" ${(quizTask.decisions!taskDecisions)!?map(d -> d.id)?seq_contains(decision.id)?string("checked", "")}>
                        <label class="form-check-label" for="decision-${decision.id}">
                            ${decision.name}
                        </label>
                    </div>
                </#list>
            </#if>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <input type="hidden" name="quiz" value="${quiz.id?c}"/>
        <input type="hidden" name="quizTask" value="${(quizTask.id)!?c}"/>
        <button class="btn btn-primary" type="submit">Добавить вопрос</button>
    </form>
</#macro>

<#macro yesOrNo path>
    <form class="row g-3" action="${path}" method="post"
          enctype="multipart/form-data">
        <div class="col-2 mb-3">
            <div class="input-group">
                <span class="input-group-text">№</span>
                <input id="positionValue" class="form-control" type="number" min="1"
                       value="${((quizTask.position)!position)!?c}" name="position"/>
            </div>
        </div>
        <div class="col-10 mb-3">
            <div class="input-group col-9">
                <span class="input-group-text">Текст вопроса</span>
                <textarea id="inputText" class="form-control"
                          name="questionText">${(yesOrNoTask.questionText)!}</textarea>
            </div>
        </div>
        <div class="col-12 mb-3">
            <label for="formFile" class="form-label">Вспомогательная картинка</label>
            <input class="form-control" type="file" name="file" id="formFile">
        </div>
        <div class="input-group mb-3">
            <span class="input-group-text">Да</span>
            <input type="number" name="yesWeight" class="form-control" step="0.5" placeholder="4"
                   value="${(yesOrNoTask.yesWeight)!?c}">
            <span class="input-group-text">Нет</span>
            <input type="number" name="noWeight" class="form-control" step="0.5" placeholder="0"
                   value="${(yesOrNoTask.noWeight)!?c}">
        </div>
        <div class="mb-3">
            <#if decisions?has_content>
                <#list decisions as decision>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="decisions"
                               value="${decision.id}"
                               id="decision-${decision.id}" ${(quizTask.decisions!taskDecisions)!?map(d -> d.id)?seq_contains(decision.id)?string("checked", "")}>
                        <label class="form-check-label" for="decision-${decision.id}">
                            ${decision.name}
                        </label>
                    </div>
                </#list>
            </#if>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <input type="hidden" name="quiz" value="${quiz.id?c}"/>
        <input type="hidden" name="quizTask" value="${(quizTask.id)!?c}"/>
        <button class="btn btn-primary" type="submit">Добавить вопрос</button>
    </form>
</#macro>