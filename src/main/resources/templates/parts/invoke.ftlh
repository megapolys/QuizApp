<#-- @ftlvariable name="task" type="com.example.servingwebcontent.domain.quiz.result.QuizTaskResult" -->
<#import "modal.ftlh" as modal>
<#macro invoke path admin>
    <div class="card">
        <h3 class="card-header">№${task.task.position} <#if task.task.fiveVariantTask??>${task.task.fiveVariantTask.preQuestionText!}<#else>${task.task.yesOrNoTask.preQuestionText!}</#if></h3>
        <div class="card-body">
            <p class="card-text"><#if task.task.fiveVariantTask??>${task.task.fiveVariantTask.questionText!}<#else>${task.task.yesOrNoTask.questionText!}</#if></p>
            <form action="${path}" method="post">
                <#if task.task.fiveVariantTask??>
                    <#if (task.task.fiveVariantTask.fileName)??>
                        <@modal.image "Показать картинку, возможно картинка будет стрёмная. Просьба удостовериться, что вы не потребляете пищу в данный момент, спасибо." "/img/${task.task.fiveVariantTask.fileName}" "modalImageFiveVariant"/>
                    </#if>
                    <#if !admin>
                        <div class="input-group mb-3">
                            <div class="form-check me-3">
                                <input class="form-check-input" type="radio" name="variant" value="1" id="radio-1">
                                <label class="form-check-label" for="radio-1">Никогда</label>
                            </div>
                            <div class="form-check me-3">
                                <input class="form-check-input" type="radio" name="variant" value="2" id="radio-2">
                                <label class="form-check-label" for="radio-2">Редко</label>
                            </div>
                            <div class="form-check me-3">
                                <input class="form-check-input" type="radio" name="variant" value="3" id="radio-3">
                                <label class="form-check-label" for="radio-3">Иногда</label>
                            </div>
                            <div class="form-check me-3">
                                <input class="form-check-input" type="radio" name="variant" value="4" id="radio-4">
                                <label class="form-check-label" for="radio-4">Часто</label>
                            </div>
                            <div class="form-check me-3">
                                <input class="form-check-input" type="radio" name="variant" value="5" id="radio-5">
                                <label class="form-check-label" for="radio-5">Всегда</label>
                            </div>
                        </div>
                    </#if>
                    <div class="mb-3">
                        <label for="inputText" class="form-label">Альтернативный ответ</label>
                        <textarea id="inputText" class="form-control" name="text"
                                  placeholder="Свой вариант..." ${admin?string("disabled", "")}>${areaTask?has_content?then(areaTask, task.text!)}</textarea>
                    </div>
                </#if>
                <#if task.task.yesOrNoTask??>
                    <#if (task.task.yesOrNoTask.fileName)??>
                        <@modal.image "Показать картинку, возможно картинка будет стрёмная. Просьба удостовериться, что вы не потребляете пищу в данный момент, спасибо." "/img/${task.task.yesOrNoTask.fileName}" "modalImageYesOrNo"/>
                    </#if>
                    <#if !admin>
                        <div class="input-group mb-3">
                            <div class="form-check me-3">
                                <input class="form-check-input" type="radio" name="variant" value="1" id="radio-1">
                                <label class="form-check-label" for="radio-1">Да</label>
                            </div>
                            <div class="form-check me-3">
                                <input class="form-check-input" type="radio" name="variant" value="2" id="radio-2">
                                <label class="form-check-label" for="radio-2">Нет</label>
                            </div>
                        </div>
                    </#if>
                    <div class="mb-3">
                        <label for="inputText" class="form-label">Альтернативный ответ</label>
                        <textarea id="inputText" class="form-control" name="text"
                                  placeholder="Свой вариант..." ${admin?string("disabled", "")}>${areaTask?has_content?then(areaTask, task.text!)}</textarea>
                    </div>
                </#if>
                <input type="hidden" name="task" value="${task.id?c}"/>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <#if admin>
                    <div class="input-group mb-3">
                        <span class="input-group-text">Текущий балл, высчитанный автоматически:</span>
                        <input type="number" name="score" class="form-control" step="0.5" value="${score}" disabled>
                        <label class="input-group-text">Балл</label>
                        <input type="number" name="altScore" class="form-control" step="0.5" value="${task.altScore!}">
                    </div>
                    <button type="submit" class="btn btn-primary">Сохранить</button>
                <#else>
                    <#if lastTask>
                        <button type="submit" class="btn btn-success">Завершить тестирование</button>
                    <#else>
                        <button type="submit" class="btn btn-primary">Некст</button>
                    </#if>
                </#if>
            </form>
        </div>
        <#if !admin>
            <div class="card-footer text-body-secondary text-center">${pagination}</div>
        </#if>
    </div>
</#macro>