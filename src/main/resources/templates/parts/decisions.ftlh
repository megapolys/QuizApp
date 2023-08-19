<#macro decisions groups decisions_>
    <div class="accordion" id="accordion_groups">
        <#list groups as group>
            <@accordionGroup group.id?c group.name group.decisions false ""/>
        </#list>
        <@accordionGroup "nonGroup" "Не в группе" decisions_ false ""/>
    </div>
</#macro>

<#macro decisionsCheckbox groups decisions_ checkedDecisions>
    <div class="accordion" id="accordion_groups">
        <#list groups as group>
            <@accordionGroup group.id?c group.name group.decisions true checkedDecisions/>
        </#list>
        <@accordionGroup "nonGroup" "Не в группе" decisions_ true checkedDecisions/>
    </div>
</#macro>

<#macro accordionGroup id name decisions checkbox checkedDecisions>
<#-- @ftlvariable name="decisions" type="java.util.Collection<com.example.servingwebcontent.domain.quiz.decision.QuizDecision>" -->
    <div class="accordion-item">
        <h2 class="accordion-header">
            <button id="group-${id}" class="accordion-button <#if checkbox>collapsed</#if>" type="button" data-bs-toggle="collapse" data-bs-target="#panelsStayOpen-group-${id}" aria-expanded="true" aria-controls="panelsStayOpen-group-${id}">
                ${name}
            </button>
        </h2>
        <div id="panelsStayOpen-group-${id}" class="accordion-collapse collapse <#if !checkbox>show</#if>">
            <div class="row">
                <div class="col-2">
                    <#if id != "nonGroup" && !checkbox>
                        <div class="m-3">
                            <a href="/decisions/group/updateAction/${id?c}">Изменить группу</a>
                        </div>
                    </#if>
                </div>
                <div class="accordion-body col-10">
                    <#if decisions?has_content>
                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <th style="width: 60%"></th>
                                <th style="width: 20%"></th>
                                <th style="width: 20%"></th>
                            </tr>
                            </thead>
                            <tbody>
                            <#list decisions as decision>
                                <tr>
                                    <#if checkbox>
                                        <td>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" name="decisions"
                                                       value="${decision.id}"
                                                       id="decision-${decision.id?c}" ${checkedDecisions?map(d -> d.id)?seq_contains(decision.id)?string("checked", "")}
                                                       onclick="chooseDecision('decision-chosen-${decision.id}', '${(decision.group.name)!""}', '${decision.name}', 'group-${(decision.group.id)!"nonGroup"}', 'decision-${decision.id}')"
                                                >
                                                <label class="form-check-label" for="decision-${decision.id}">
                                                    ${decision.name}
                                                </label>
                                            </div>
                                        </td>
                                        <td colspan="2">
                                            <#if decision.description?has_content>
                                                <a href="##" class="text-decoration-none" <#if decision.description?has_content>data-bs-toggle="tooltip" data-bs-title="${decision.description}"</#if>>Описание</a>
                                            </#if>
                                        </td>
                                    <#else>
                                        <td><@decisionWithDescription decision/></td>
                                        <td><a href="/decisions/updateAction/${decision.id?c}">Изменить</a></td>
                                        <td><a href="/decisions/delete/${decision.id?c}">Удалить</a></td>
                                    </#if>
                                </tr>
                            </#list>
                            </tbody>
                        </table>
                    <#else>
                        <p>Нет решений</p>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro listOfChosenDecisions decisions_>
<#-- @ftlvariable name="decisions_" type="java.util.Collection<com.example.servingwebcontent.domain.quiz.decision.QuizDecision>" -->
    <div class="mb-3">
        Выбранные решения:
        <div id="chosen-decisions">
            <#list decisions_ as decision>
                <a onclick=decisionFocus('group-${(decision.group.id)!"nonGroup"}',"decision-${decision.id}") id="decision-chosen-${decision.id}">${(decision?counter > 1)?string(", ", "")}<#if decision.group??>${decision.group.name}.</#if>${decision.name}</a>
            <#else>
                Нет выбранных решений
            </#list>
        </div>
    </div>
</#macro>

<#macro decisionWithDescription decision>
    <a href="##" class="text-decoration-none" <#if decision.description?has_content>data-bs-toggle="tooltip" data-bs-title="${decision.description}"</#if>>${decision.name}</a>
</#macro>