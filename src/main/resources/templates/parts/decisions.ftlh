<#macro decisions groups decisions_>
    <div class="accordion" id="accordion_groups">
        <#list groups as group>
            <@accordionGroup group.id?c group.name group.decisions/>
        </#list>
        <@accordionGroup "nonGroup" "Не в группе" decisions_/>
    </div>
</#macro>

<#macro accordionGroup id name decisions>
    <div class="accordion-item">
        <h2 class="accordion-header">
            <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#panelsStayOpen-group-${id}" aria-expanded="true" aria-controls="panelsStayOpen-group-${id}">
                ${name}
            </button>
        </h2>
        <div id="panelsStayOpen-group-${id}" class="accordion-collapse collapse show">
            <div class="row">
                <div class="col-2">
                    <#if id != "nonGroup">
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
                                    <td><a href="##" class="text-decoration-none" <#if decision.description?has_content>data-bs-toggle="tooltip" data-bs-title="${decision.description}"</#if>>${decision.name}</a></td>
                                    <td><a href="/decisions/updateAction/${decision.id?c}">Изменить</a></td>
                                    <td><a href="/decisions/delete/${decision.id?c}">Удалить</a></td>
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