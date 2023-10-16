<#import "../../parts/common.ftlh" as c>
<#import "../../parts/modal.ftlh" as modal>
<@c.page>
    <h3 class="mb-3">Список анализов</h3>
    <form class="mb-3" action="/medical/add" method="post">
        <div class="row">
            <div class="col-8">
                <input class="form-control" type="text" name="name" placeholder="Наименование анализа"/>
            </div>
            <div class="col-2">
                <button class="btn btn-primary" type="submit">Добавить</button>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        </div>
    </form>
    <table id="medical-topic-table" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 60%">Наименование</th>
            <th style="width: 10%">Размер</th>
            <th style="width: 10%"></th>
            <th style="width: 10%"></th>
            <th style="width: 10%"></th>
        </tr>
        </thead>
        <tbody>
        <#list topics as topic>
            <tr data-id="${topic.name}">
                <td>${topic.name}</td>
                <td>${(topic.medicalTasks)?has_content?then(topic.medicalTasks?size?c, "пусто")}</td>
                <td><@modal.postButton "/medical/copy/${topic.id?c}" "Скопировать"/></td>
                <td><a href="/medical/${topic.id?c}">Редактировать</a></td>
                <td>
                    <@modal.deleteConfirm path="/medical/delete/${topic.id?c}"
                    id="confirm-delete-medicalTopic-${topic.id}"
                    text="Вы точно уверены, что хотите удалить список анализов? Это необратимая операция, которая приведет к тому же к удалению всех заполненных анкет анализов."/>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>