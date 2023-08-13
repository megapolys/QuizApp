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
    <div class="accordion-item">
        <h2 class="accordion-header">
            <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#panelsStayOpen-group-${id}" aria-expanded="true" aria-controls="panelsStayOpen-group-${id}">
                ${name}
            </button>
        </h2>
        <div id="panelsStayOpen-group-${id}" class="accordion-collapse collapse <#if !checkbox>show</#if>">
            <div class="row">
                <div class="col-2">
                    <#if id != "nonGroup" && !checkbox>
                        <div class="m-3">
                            <a href="/decisions/group/updateAction/${id}">Изменить группу</a>
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
                                                       id="decision-${decision.id}" ${checkedDecisions?map(d -> d.id)?seq_contains(decision.id)?string("checked", "")}>
                                                <label class="form-check-label" for="decision-${decision.id}">
                                                    ${decision.name}
                                                </label>
                                            </div>
                                        </td>
                                        <td colspan="2"><a href="##" class="text-decoration-none" <#if decision.description?has_content>data-bs-toggle="tooltip" data-bs-title="${decision.description}"</#if>>Описание</a></td>
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
    <#if decisions_?has_content>
        <div class="mb-3">
            Выбранные решения:
            <#list decisions_ as decision>
                ${decision.group?has_content?string(decision.group.name + ".", "")}<@decisionWithDescription decision/><#sep>, </#sep>
            </#list>
        </div>
    </#if>
</#macro>

<#macro decisionWithDescription decision>
    <a href="##" class="text-decoration-none" <#if decision.description?has_content>data-bs-toggle="tooltip" data-bs-title="${decision.description}"</#if>>${decision.name}</a>
</#macro>