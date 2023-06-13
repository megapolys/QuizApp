<#-- @ftlvariable name="fiveVariantTask" type="com.example.servingwebcontent.domain.quiz.task.FiveVariantTask" -->
<#-- @ftlvariable name="yesOrNoTask" type="com.example.servingwebcontent.domain.quiz.task.YesOrNoTask" -->

<#import "parts/common.ftlh" as c>
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
    <div class="row mb-5">
        <div class="accordion" id="accordionExample">
            <div class="accordion-item">
                <h2 class="accordion-header">
                    <button class="accordion-button" type="button" data-bs-toggle="collapse"
                            data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                        Пять вариантов (Никогда - Всегда)
                    </button>
                </h2>
                <div id="collapseOne" class="accordion-collapse collapse show" data-bs-parent="#accordionExample">
                    <div class="accordion-body">
                        <form class="row g-3" action="/quiz/task/add/fiveVariant" method="post"
                              enctype="multipart/form-data">
                            <div class="col-2 mb-3">
                                <div class="input-group">
                                    <span class="input-group-text">#</span>
                                    <input id="positionValue" class="form-control" type="number" min="1"
                                           value="${position}" name="position"/>
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
                                <input type="number" name="secondWeight" class="form-control" step="0.5" placeholder="1"
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
                                                   id="decision-${decision.id}" ${taskDecisions!?map(d -> d.id)?seq_contains(decision.id)?string("checked", "")}>
                                            <label class="form-check-label" for="decision-${decision.id}">
                                                ${decision.name}
                                            </label>
                                        </div>
                                    </#list>
                                </#if>
                            </div>
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <input type="hidden" name="quiz" value="${quiz.id}"/>
                            <button class="btn btn-primary" type="submit">Добавить вопрос</button>
                        </form>
                    </div>
                </div>
            </div>
            <div class="accordion-item">
                <h2 class="accordion-header">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                            data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                        Два варианта (Да, Нет)
                    </button>
                </h2>
                <div id="collapseTwo" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
                    <div class="accordion-body">
                        <form class="row g-3" action="/quiz/task/add/yesOrNo" method="post"
                              enctype="multipart/form-data">
                            <div class="col-2 mb-3">
                                <div class="input-group">
                                    <span class="input-group-text">#</span>
                                    <input id="positionValue" class="form-control" type="number" min="1"
                                           value="${position}" name="position"/>
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
                                                   id="decision-${decision.id}" ${(taskDecisions)!?map(d -> d.id)?seq_contains(decision.id)?string("checked", "")}>
                                            <label class="form-check-label" for="decision-${decision.id}">
                                                ${decision.name}
                                            </label>
                                        </div>
                                    </#list>
                                </#if>
                            </div>
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <input type="hidden" name="quiz" value="${quiz.id}"/>
                            <button class="btn btn-primary" type="submit">Добавить вопрос</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
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
            <#if task.fiveVariantTask?has_content>
                <tr>
                    <td>${task.position}</td>
                    <td>${(task.fiveVariantTask.questionText)!(task.yesOrNoTask.questionText)?truncate(50, "...")}</td>
                    <td><a href="/quiz/task/update/${task.id?c}">Изменить</a></td>
                    <td><a href="/quiz/task/delete/${task.id?c}">Удалить</a></td>
                </tr>
            </#if>
        </#list>
        </tbody>
    </table>

</@c.page>