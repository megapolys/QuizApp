<#import "parts/common.ftlh" as c>
<@c.page>
    <h3 class="mb-3">Список тестов</h3>
    <form class="mb-3" action="/quiz/add" method="get">
        <div class="row">
            <div class="col-2">
                <input class="form-control" type="text" name="shortName" placeholder="Индекс"/>
            </div>
            <div class="col-8">
                <input class="form-control" type="text" name="name" placeholder="Наименование теста"/>
            </div>
            <div class="col-2">
                <button class="btn btn-primary" type="submit">Добавить</button>
            </div>
        </div>
    </form>
    <table class="table table-striped">
        <thead>
        <tr>
            <th style="width: 5%">Индекс</th>
            <th style="width: 75%">Наименование</th>
            <th style="width: 10%"></th>
            <th style="width: 10%"></th>
        </tr>
        </thead>
        <tbody>
        <#list quizList as quiz>
            <tr>
                <td>${quiz.shortName}</td>
                <td>${quiz.name}</td>
                <td><a href="/quiz/${quiz.id}">Редактировать</a></td>
                <td><a href="/quiz/delete/${quiz.id}">Удалить</a></td>
            </tr>
        </#list>
        </tbody>
    </table>

</@c.page>