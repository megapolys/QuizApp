<#import "parts/common.ftlh" as c>
<@c.page>
    <h3>Список тестов</h3>
    <form action="/quiz/add" method="get">
        <div class="row">
            <div class="col-1">
                <input class="form-control" type="text" name="quizShortName" placeholder="Индекс"/>
            </div>
            <div class="col-6">
                <input class="form-control" type="text" name="quizName" placeholder="Наименование теста"/>
            </div>
            <div class="col-2">
                <button class="btn btn-primary" type="submit">Добавить</button>
            </div>
        </div>
    </form>
    <#list quizList as quiz>
        <div class="row">
            <div class="col-7">${quiz.shortName}. ${quiz.name}</div>
            <div class="col-2"><a href="/quiz/${quiz.id}">Редактировать</a></div>
            <div class="col-2"><a href="/quiz/delete/${quiz.id}">Удалить</a></div>
        </div>
    </#list>

</@c.page>