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
    <div class="row mb-5">
        <div class="accordion" id="accordionExample">
            <div class="accordion-item">
                <h2 class="accordion-header">
                    <button class="accordion-button" type="button" data-bs-toggle="collapse"
                            data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                        Пять вариантов (Никогда - Всегда)
                    </button>
                </h2>
                <div id="collapseOne" class="accordion-collapse collapse ${yesOrNoTask?has_content?string("", "show")}" data-bs-parent="#accordionExample">
                    <div class="accordion-body">
                        <@task.fiveVariant "/quiz/task/add/fiveVariant"/>
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
                <div id="collapseTwo" class="accordion-collapse collapse ${yesOrNoTask?has_content?string("show", "")}" data-bs-parent="#accordionExample">
                    <div class="accordion-body">
                        <@task.yesOrNo "/quiz/task/add/yesOrNo"/>
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
            <tr>
                <td>${task.position}</td>
                <td>${(task.fiveVariantTask.questionText)!(task.yesOrNoTask.questionText)?truncate(50, "...")}</td>
                <#assign type = task.fiveVariantTask?has_content?string("fiveVariant", "yesOrNo")>
                <td><a href="/quiz/task/update/${type}/${task.id?c}">Изменить</a></td>
                <td><a href="/quiz/task/delete/${task.id?c}">Удалить</a></td>
            </tr>
        </#list>
        </tbody>
    </table>

</@c.page>