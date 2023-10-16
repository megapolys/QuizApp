<#-- @ftlvariable name="task" type="com.example.servingwebcontent.domain.medical.MedicalTask" -->
<#import "../../parts/common.ftlh" as c>
<#import "../../parts/decisions.ftlh" as d>
<#import "../../parts/modal.ftlh" as modal>
<@c.page>
    <a class="btn btn-primary mb-3" href="/medical/list">Назад</a>
    <h3 class="mb-3">Страница редактирования "${topic.name}"</h3>
    <div class="row mb-5">
        <form action="/medical/rename/${topic.id?c}" method="get">
            <div class="input-group">
                <input class="form-control" style="width: 75%" type="text" value="${topic.name}" name="name" placeholder="Название топика"/>
                <button class="btn btn-primary" type="submit">Переименовать</button>
            </div>
        </form>
    </div>
    <div class="mb-3 p-3 text-bg-light">
        <form method="post" class="row" action="/medical/${topic.id?c}/task/${update!"add"}">
            <div class="row mb-3 justify-content-md-center">
                <button class="btn btn-primary col-3" type="submit"><#if update??>Обновить анализ<#else>Добавить анализ</#if></button>
            </div>
            <div class="row mb-3">
                <div class="col-10">
                    <div class="input-group">
                        <span class="input-group-text">Название анализа</span>
                        <input id="inputText" class="form-control" name="name" value="${(task.name)!}"/>
                    </div>
                </div>
                <div class="col-2">
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-speedometer"></i></span>
                        <input id="inputText" class="form-control" name="unit" value="${(task.unit)!}"/>
                    </div>
                </div>
            </div>
            <#setting locale="en_US">
            <div class="input-group mb-3">
                <span class="input-group-text">Левый референс</span>
                <input type="number" name="leftLeft" class="form-control" step="0.01" value="${(task.leftLeft?string("0.##"))!}"/>
                <span class="input-group-text">Левый оптимум</span>
                <input type="number" name="leftMid" class="form-control" step="0.01" value="${(task.leftMid?string("0.##"))!}"/>
                <span class="input-group-text">Правый оптимум</span>
                <input type="number" name="rightMid" class="form-control" step="0.01" value="${(task.rightMid?string("0.##"))!}"/>
                <span class="input-group-text">Правый референс</span>
                <input type="number" name="rightRight" class="form-control" step="0.01" value="${(task.rightRight?string("0.##"))!}"/>
            </div>
            <div class="col-6 p-2 mb-3">
                <@d.listOfChosenDecisions decisionsName="leftDecisions" groups=groups noGroupDecisions=decisions decisions_=(task.leftDecisions)!/>
                <@d.decisionsCheckbox groups=groups decisions_=decisions decisionsName="leftDecisions" checkedDecisions=(task.leftDecisions)!/>
            </div>
            <div class="col-6 p-2 mb-3">
                <@d.listOfChosenDecisions decisionsName="rightDecisions" groups=groups noGroupDecisions=decisions decisions_=(task.rightDecisions)!/>
                <@d.decisionsCheckbox groups=groups decisions_=decisions decisionsName="rightDecisions" checkedDecisions=(task.rightDecisions)!/>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <input type="hidden" name="id" value="${(task.id)!}"/>
            <div class="row mb-2 justify-content-md-center">
                <button class="btn btn-primary col-3" type="submit"><#if update??>Обновить анализ<#else>Добавить анализ</#if></button>
            </div>
        </form>
    </div>
    <table id="medical-topic-task-table" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 40%"></th>
            <th style="width: 10%"></th>
            <th style="width: 10%"></th>
            <th style="width: 20%"></th>
            <th style="width: 20%"></th>
        </tr>
        </thead>
        <tbody>
        <#list taskList as task>
            <tr data-id="${task.name}">
                <td>${(task.name)?truncate(50, "...") + task.unit?has_content?then(" - " + task.unit, "")}</td>
                <td><a href="/medical/${topic.id?c}/task/update/${task.id?c}">Изменить</a></td>
                <td>
                    <@modal.deleteConfirm path="/medical/${topic.id?c}/task/delete/${task.id?c}"
                    id="confirm-delete-medicalTask-${task.id}"
                    text="Точно удалить анализ?"/>
                </td>
                <td><#if !task.leftDecisions?has_content><div style="color: red">Левых решений не назначено</div></#if></td>
                <td><#if !task.rightDecisions?has_content><div style="color: red">Правых решений не назначено</div></#if></td>
            </tr>
        <#else>
            <tr>
                <td colspan="5">Список анализов пока пуст</td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>