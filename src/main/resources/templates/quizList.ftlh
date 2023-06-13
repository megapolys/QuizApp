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
                <td><a href="/quiz/${quiz.id?c}">Редактировать</a></td>
                <td>
                    <!-- Button trigger modal -->
                    <a href="/quiz/delete/${quiz.id?c}" data-bs-toggle="modal" data-bs-target="#confirmModal-${quiz.id?c}">
                        Удалить
                    </a>

                    <!-- Modal -->
                    <div class="modal fade" id="confirmModal-${quiz.id?c}" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h1 class="modal-title fs-5" id="staticBackdropLabel">Подтверждение</h1>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    Вы точно уверены, что хотите удалить опросник? Это необратимая операция, которая приведет к тому же к удалению всех результатов решения данного опросника.
                                </div>
                                <div class="modal-footer">
                                    <a class="btn btn-danger" href="/quiz/delete/${quiz.id?c}">Удалить</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>

</@c.page>