<#-- @ftlvariable name="changeDecision" type="com.example.servingwebcontent.model.quiz.decision.QuizDecision" -->
<#-- @ftlvariable name="changeGroup" type="com.example.servingwebcontent.model.quiz.decision.DecisionGroup" -->
<#import "../parts/common.ftlh" as c>
<#import "../parts/decisions.ftlh" as d>
<@c.page>
    <h3 class="mb-3">Список решений</h3>
    <h5 class="mb-3">Группа</h5>
    <#if changeGroup??>
        <form class="mb-3" action="/decisions/group/update/${changeGroup.id?c}" method="get">
            <div class="row">
                <div class="col-4">
                    <input class="form-control" type="text" name="name" placeholder="Группа"
                           value="${changeGroup.name}"/>
                </div>
                <div class="col-2">
                    <button class="btn btn-primary" type="submit">Переименовать</button>
                </div>
                <div class="col-4">
                    <a class="btn btn-danger" href="/decisions/group/delete/${changeGroup.id?c}">Расформировать группу</a>
                </div>
            </div>
        </form>
    <#else>
        <form class="mb-3" action="/decisions/group/add" method="get">
            <div class="row">
                <div class="col-4">
                    <input class="form-control" type="text" name="name" placeholder="Группа"/>
                </div>
                <div class="col-2">
                    <button class="btn btn-primary" type="submit">Добавить группу</button>
                </div>
            </div>
        </form>
    </#if>
    <h5 class="mb-3">Решение</h5>
    <form class="mb-3" <#if changeDecision??>action="/decisions/update/${changeDecision.id?c}" <#else>action="/decisions/add"</#if> method="get">
        <div class="row mb-3">
            <div class="col-4">
                <select name="group" class="form-select" aria-label="group_list">
                    <option value=""></option>
                    <#list groups as group>
                        <option value="${group.id}" <#if (changeDecision.group)?? && group.id == changeDecision.group.id>selected</#if>>${group.name}</option>
                    </#list>
                </select>
            </div>
            <div class="col-6">
                <input class="form-control" type="text" name="name" placeholder="Наименование решения" value="${(changeDecision.name)!}"/>
            </div>
        </div>
        <div class="row">
            <div class="col-8">
                <textarea class="form-control" name="description" placeholder="Описание">${(changeDecision.description)!}</textarea>
            </div>
            <div class="col-2">
                <button class="btn btn-primary" type="submit"><#if changeDecision??>Изменить решение<#else>Добавить решение</#if></button>
            </div>
        </div>
    </form>
    <@d.decisions groups=groups decisions_=decisions/>
</@c.page>
