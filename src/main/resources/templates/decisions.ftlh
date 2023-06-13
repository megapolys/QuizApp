<#import "parts/common.ftlh" as c>
<@c.page>
    <h3 class="mb-3">Список решений</h3>
    <form class="mb-3" action="/decisions/add" method="get">
        <div class="row">
            <div class="col-6">
                <input class="form-control" type="text" name="name" placeholder="Наименование решения"/>
            </div>
            <div class="col-2">
                <button class="btn btn-primary" type="submit">Добавить</button>
            </div>
        </div>
    </form>
    <table class="table table-striped">
        <thead>
        <tr>
            <th style="width: 50%"></th>
            <th style="width: 15%"></th>
        </tr>
        </thead>
        <tbody>
        <#list decisions as decision>
            <tr>
                <td>${decision.name}</td>
                <td><a href="/decisions/delete/${decision.id?c}">Удалить</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>