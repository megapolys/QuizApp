<#import "parts/common.ftlh" as c>
<@c.page>
    <div class="text-danger mb-3">${message!}</div>
    <div class="card">
        <h3 class="card-header">Вопрос #${task.task.position}</h3>
        <div class="card-body">
            <p class="card-text"><#if task.task.fiveVariantTask??>${task.task.fiveVariantTask.questionText!}<#else>${task.task.yesOrNoTask.questionText!}</#if></p>
            <form action="/invokeQuiz/${task.quiz.id?c}" method="post">
                <#if task.task.fiveVariantTask??>
                    <#if task.task.fiveVariantTask.fileName??>
                        <img class="mb-3 img-thumbnail" src="/img/${task.task.fiveVariantTask.fileName}" width="200px" height="200px">
                    </#if>
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
                    <div class="mb-3">
                        <label for="inputText" class="form-label">Альтернативный ответ</label>
                        <textarea id="inputText" class="form-control" name="text" placeholder="Свой вариант...">${areaText!}</textarea>
                    </div>
                </#if>
                <#if task.task.yesOrNoTask??>
                    <#if task.task.yesOrNoTask.fileName??>
                        <img class="mb-3 img-thumbnail" src="/img/${task.task.yesOrNoTask.fileName}" width="200px" height="200px">
                    </#if>
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
                    <div class="mb-3">
                        <label for="inputText" class="form-label">Альтернативный ответ</label>
                        <textarea id="inputText" class="form-control" name="text" placeholder="Свой вариант...">${areaText!}</textarea>
                    </div>
                </#if>
                <input type="hidden" name="task" value="${task.id}"/>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <#if lastTask>
                    <button type="submit" class="btn btn-success">Завершить тестирование</button>
                <#else>
                    <button type="submit" class="btn btn-primary">Некст</button>
                </#if>
            </form>
        </div>
        <div class="card-footer text-body-secondary text-center">${pagination}</div>
    </div>
    <div class="progress" role="progressbar" aria-label="Basic example" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">
        <div class="progress-bar" style="width: ${progress?c}%"></div>
    </div>
</@c.page>