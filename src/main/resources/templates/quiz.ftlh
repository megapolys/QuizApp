<#import "parts/common.ftlh" as c>
<@c.page>
    <h3>Страница редактирования теста "${quiz.name}"</h3>
    <div class="row mb-5">
        <form action="/quiz/rename/${quiz.id}" method="get">
            <div class="input-group">
                <input class="form-control" type="text" value="${quiz.name}" name="quizName"
                       placeholder="Неужели так сложно придумать название для теста"/>
                <button class="btn btn-primary" type="submit">Переименовать</button>
            </div>
        </form>
    </div>
    <div class="row mb-5">
        <div class="accordion" id="accordionExample">
            <div class="accordion-item">
                <h2 class="accordion-header">
                    <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                        Пять вариантов (Никогда - Всегда)
                    </button>
                </h2>
                <div id="collapseOne" class="accordion-collapse collapse show" data-bs-parent="#accordionExample">
                    <div class="accordion-body">
                        <form class="row g-3" action="/quiz/task/add/fiveVariant" method="post" enctype="multipart/form-data">
                            <div class="col-1 mb-3">
                                <div class="input-group">
                                    <span class="input-group-text">#</span>
                                    <input id="positionValue" class="form-control" type="text" name="position"/>
                                </div>
                            </div>
                            <div class="col-11 mb-3">
                                <div class="input-group col-9">
                                    <span class="input-group-text">Текст вопроса</span>
                                    <textarea id="inputText" class="form-control" name="questionText"></textarea>
                                </div>
                            </div>
                            <div class="col-12 mb-3">
                                <label for="formFile" class="form-label">Вспомогательная картинка</label>
                                <input class="form-control" type="file" name="file" id="formFile">
                            </div>
                            <div class="input-group mb-3">
                                <span class="input-group-text">Никогда</span>
                                <input type="text" name="firstWeight" class="form-control" placeholder="0">
                                <span class="input-group-text">Редко</span>
                                <input type="text" name="secondWeight" class="form-control" placeholder="1">
                                <span class="input-group-text">Иногда</span>
                                <input type="text" name="thirdWeight" class="form-control" placeholder="2">
                                <span class="input-group-text">Часто</span>
                                <input type="text" name="fourthWeight" class="form-control" placeholder="3">
                                <span class="input-group-text">Всегда</span>
                                <input type="text" name="fifthWeight" class="form-control" placeholder="4">
                            </div>
                            <div class="input-group mb-3">
                                <#if decisions?has_content>
                                    <#list decisions as decision>
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox" name="decisions"
                                                   value="${decision.id}" id="decision-${decision.id}">
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
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                        Два варианта (Да, Нет)
                    </button>
                </h2>
                <div id="collapseTwo" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
                    <div class="accordion-body">
                        <form class="row g-3" action="/quiz/task/add/yesOrNo" method="post" enctype="multipart/form-data">
                            <div class="col-1 mb-3">
                                <div class="input-group">
                                    <span class="input-group-text">#</span>
                                    <input id="positionValue" class="form-control" type="text" name="position"/>
                                </div>
                            </div>
                            <div class="col-11 mb-3">
                                <div class="input-group col-9">
                                    <span class="input-group-text">Текст вопроса</span>
                                    <textarea id="inputText" class="form-control" name="questionText"></textarea>
                                </div>
                            </div>
                            <div class="col-12 mb-3">
                                <label for="formFile" class="form-label">Вспомогательная картинка</label>
                                <input class="form-control" type="file" name="file" id="formFile">
                            </div>
                            <div class="input-group mb-3">
                                <span class="input-group-text">Да</span>
                                <input type="text" name="yesWeight" class="form-control" placeholder="4">
                                <span class="input-group-text">Нет</span>
                                <input type="text" name="noWeight" class="form-control" placeholder="0">
                            </div>
                            <div class="input-group mb-3">
                                <#if decisions?has_content>
                                    <#list decisions as decision>
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox" name="decisions"
                                                   value="${decision.id}" id="decision-${decision.id}">
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
    <#list taskList as task>
        <#if task.fiveVariantTask?has_content>
            <div class="row">
                <div class="col-6">${task.position}. ${task.fiveVariantTask.questionText?truncate(50, "...")}</div>
                <div class="col-2"><a href="/quiz/task/delete/${task.id?c}">Удалить</a></div>
            </div>
        </#if>
        <#if task.yesOrNoTask?has_content>
            <div class="row">
                <div class="col-6">${task.position}. ${task.yesOrNoTask.questionText?truncate(50, "...")}</div>
                <div class="col-2"><a href="/quiz/task/delete/${task.id?c}">Удалить</a></div>
            </div>
        </#if>
    </#list>

</@c.page>