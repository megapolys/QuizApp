<#import "../parts/modal.ftlh" as modal>

<#macro decisions groups decisions_>
    <div class="accordion" id="accordion_groups">
        <#list groups as group>
            <@accordionGroup "decisions" group.id?c group.name group.decisions false ""/>
        </#list>
        <@accordionGroup "decisions" "nonGroup" "Не в группе" decisions_ false ""/>
    </div>
</#macro>


<#macro decisionsCheckbox groups decisions_ checkedDecisions decisionsName>
    <div class="accordion" id="accordion_groups">
        <#list groups as group>
            <@accordionGroup decisionsName group.id?c group.name group.decisions true checkedDecisions/>
        </#list>
        <@accordionGroup decisionsName "nonGroup" "Не в группе" decisions_ true checkedDecisions/>
    </div>
</#macro>

<#macro accordionGroup decisionsName id groupName decisions checkbox checkedDecisions>
<#-- @ftlvariable name="decisions" type="java.util.Collection<com.example.servingwebcontent.model.decision.Decision>" -->
    <div class="accordion-item">
        <h2 class="accordion-header">
            <button id="group-${id}-${decisionsName}" class="accordion-button <#if checkbox>collapsed</#if>" type="button" data-bs-toggle="collapse" data-bs-target="#panelsStayOpen-group-${id}-${decisionsName}" aria-expanded="true" aria-controls="panelsStayOpen-group-${id}-${decisionsName}">
                ${groupName}
            </button>
        </h2>
        <div id="panelsStayOpen-group-${id}-${decisionsName}" class="accordion-collapse collapse <#if !checkbox>show</#if>">
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
                                <tr data-id="${decision.name}">
                                    <#if checkbox>
                                        <td>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" name="${decisionsName}"
                                                       value="${decision.id}"
                                                       id="${decisionsName}-${decision.id?c}" ${checkedDecisions?map(d -> d.id)?seq_contains(decision.id)?string("checked", "")}
                                                       onclick="chooseDecision(
                                                               '#chosen-${decisionsName}',
                                                               'decision-chosen-${decision.id}-${decisionsName}',
                                                               '${(decision.group.name)!""}', '${decision.name}',
                                                               'group-${(decision.group.id)!"nonGroup"}-${decisionsName}',
                                                               '${decisionsName}-${decision.id}'
                                                               )"
                                                />
                                                <label class="form-check-label" for="${decisionsName}-${decision.id}">
                                                    ${decision.name}
                                                </label>
                                            </div>
                                        </td>
                                        <td colspan="2">
                                            <#if decision.description?has_content>
                                                <@decisionWithDescription decision "Описание" '${decisionsName}-${decision.id}'/>
                                            </#if>
                                        </td>
                                    <#else>
                                        <td><@decisionWithDescription decision decision.name/></td>
                                        <td><a href="/decisions/updateAction/${decision.id?c}">Изменить</a></td>
                                        <td>
                                            <@modal.deleteConfirm path="/decisions/${decision.id?c}"
                                            id="confirm-delete-decision-${decision.id}"
                                            text="Точно удалить решение?"/>
                                        </td>
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

<#macro listOfChosenDecisions decisionsName decisions_ groups noGroupDecisions>
<#-- @ftlvariable name="decisions_" type="java.util.Collection<com.example.servingwebcontent.model.decision.Decision>" -->
    <div class="mb-3">
        <div class="row mb-2">
            <div class="col-4">
                Выбранные решения:
            </div>
            <div class="col-4">
                <input class="form-control" list="${decisionsName}-decisionList" id="${decisionsName}-list" placeholder="Вводи решение"/>
                <datalist id="${decisionsName}-decisionList">
                    <#list groups as group>
                        <#list group.decisions as decision>
                            <option for="#${decisionsName}-${decision.id}" value="${group.name} _ ${decision.name}"/>
                        </#list>
                    </#list>
                    <#list noGroupDecisions as decision>
                        <option for="#${decisionsName}-${decision.id}" value="${decision.name}"/>
                    </#list>
                </datalist>
            </div>
            <div class="col-4">
                <button type="button" class="btn btn-primary" onclick="clickByChosen('${decisionsName}-list', '${decisionsName}-decisionList')">Клик</button>
            </div>
        </div>
        <div id="chosen-${decisionsName}">
            <#list decisions_ as decision>
                <div id="decision-chosen-${decision.id}-${decisionsName}">
                    <div class="p-2 mb-2 rounded-3 position-relative" style="background-color: #efadce">
                        <a onclick=decisionFocus('group-${(decision.group.id)!"nonGroup"}-${decisionsName}',"${decisionsName}-${decision.id}")><#if decision.group??>${decision.group.name} _ </#if>${decision.name}</a>
                        <button onclick="$('#${decisionsName}-${decision.id}')[0].click()" type="button" class="btn-close position-absolute top-50 end-0 translate-middle-y" aria-label="Close"></button>
                    </div>
                </div>
            </#list>
        </div>
    </div>
</#macro>

<#macro decisionWithDescription decision name id='${decision.id}'>
<#-- @ftlvariable name="decision" type="com.example.servingwebcontent.model.decision.Decision" -->
    <#if decision.description?has_content>
        <a href="##" type="button" class="icon-link icon-link-hover text-decoration-none p-0" style="--bs-icon-link-transform: translate3d(0, -.125rem, 0);"
           data-bs-toggle="modal" data-bs-target="#decision-modal-${id}" >
            <i class="bi bi-clipboard me-1"></i>
            ${name}
        </a>
        <div class="modal fade" id="decision-modal-${id}" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        ${decision.description}
                    </div>
                </div>
            </div>
        </div>
    <#else>
        <a href="##" class="text-decoration-none" aria-disabled="true">${name}</a>
    </#if>
</#macro>