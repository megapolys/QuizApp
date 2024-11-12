<#-- @ftlvariable name="changeDecision" type="com.example.servingwebcontent.model.decision.Decision" -->
<#-- @ftlvariable name="changeGroup" type="com.example.servingwebcontent.model.decision.GroupWithDecisions" -->
<#import "../parts/common.ftlh" as c>
<#import "../parts/decisions.ftlh" as d>
<@c.page>
    <h3 class="mb-3">Список решений</h3>
    <h5 class="mb-3">Группа</h5>
    <#if changeGroup??>
        <form class="mb-3" action="/decisions/group/${changeGroup.id?c}" method="post">
            <div class="row">
                <div class="col-4">
                    <input class="form-control" type="text" name="name" placeholder="Группа"
                           value="${changeGroup.name}"/>
                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                </div>
                <div class="col-2">
                    <button class="btn btn-primary" type="submit">Переименовать</button>
                </div>
                <div class="col-4">
                    <a class="btn btn-danger" href="#"
                       onclick="deleteGroup(${changeGroup.id}, '${_csrf.parameterName}', '${_csrf.token}')">Расформировать
                        группу</a>
                </div>
            </div>
        </form>
    <#else>
        <form class="mb-3" action="/decisions/group" method="post">
            <div class="row">
                <div class="col-4">
                    <input class="form-control" type="text" name="name" placeholder="Группа"/>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <div class="col-2">
                    <button class="btn btn-primary" type="submit">Добавить группу</button>
                </div>
            </div>
        </form>
    </#if>
    <h5 class="mb-3">Решение</h5>
    <form id="decision-form" class="mb-3">
        <div class="row mb-3">
            <div class="col-4">
                <select name="group" class="form-select" aria-label="group_list">
                    <option value=""></option>
                    <#list groups as group>
                        <option value="${group.id}"
                                <#if (changeDecision.group)?? && group.id == changeDecision.group.id>selected</#if>>${group.name}</option>
                    </#list>
                </select>
            </div>
            <div class="col-6">
                <input class="form-control" type="text" name="name" placeholder="Наименование решения"
                       value="${(changeDecision.name)!}"/>
            </div>
        </div>
        <div class="row">
            <div class="col-8">
                <textarea class="form-control" name="description"
                          placeholder="Описание">${(changeDecision.description)!}</textarea>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <div class="col-2">
                <button class="btn btn-primary"
                        onclick="addDecision()"><#if changeDecision??>Изменить решение<#else>Добавить решение</#if></button>
            </div>
        </div>
    </form>
    <@d.decisions groups=groups decisions_=decisions/>

    <div id="groupDecisionContent"></div>

    <script>

        $(async function () {
            let groupDecisionContent = $("#groupDecisionContent");
            let groupDecisions = await getGroupDecisions();
            if (groupDecisions.length > 0) {
                groupDecisionContent.html('<div class="accordion" id="accordion-groups"></div>');
                let accordionGroups = $("#accordion-groups");
                let decisionsName = 'decisions';
                groupDecisions.forEach(groupDecision => {
                    let id = groupDecision.id;
                    let groupId = id + '-' + decisionsName;
                    accordionGroups.append(
                        '<div class="accordion-item">' +
                        '<h2 class="accordion-header">' +
                        '<button id="group-' + groupId + '" class="accordion-button " type="button" data-bs-toggle="collapse" data-bs-target="#panelsStayOpen-group-' + groupId + '" aria-expanded="true" aria-controls="panelsStayOpen-group-' + groupId + '">' +
                        groupDecision.name +
                        '</button>' +
                        '</h2>' +
                        '' +
                        '<div id="panelsStayOpen-group-' + groupId + '" class="accordion-collapse collapse show">' +
                        '<div class="row">' +
                        '' +
                        '<div class="col-2">' +
                        (!!id ? '<div class="m-3">' +
                            '<a href="/decisions/group/updateAction/' + id + '">Изменить группу</a>' +
                            '</div>' : '') +
                        '</div>' +
                        '' +
                        '<div class="accordion-body col-10">' +
                        (groupDecision.decisions.length == 0 ? 'Нет решений' :
                                '<table class="table table-striped">' +
                                '<thead>' +
                                '<tr>' +
                                '<th style="width: 60%"></th>' +
                                '<th style="width: 20%"></th>' +
                                '<th style="width: 20%"></th>' +
                                '</tr>' +
                                '</thead>' +
                                '<tbody id="decisions-' + groupId + '">' +
                                '</tbody>' +
                                '</table>'
                        ) +
                        '</div>' +
                        '' +
                        '</div>' +
                        '</div>' +
                        '</div>'
                    );

                })
            }
        })

        async function getGroupDecisions() {
            console.log('get group decisions')
            let response = await fetch("/api/decisions", {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            });
            if (response.ok) {
                return response.json();
            } else {
                showError('Ошибка при загрузке решений');
            }
        }

        async function addDecision() {
            console.log('add decision');
            let form = $('#decision-form');
            let decision = {
                id: null,
                name: form.find("[name|='name']").val(),
                description: form.find("[name|='description']").val(),
                groupId: form.find("[name|='group']").val()
            };
            await fetch("/api/decisions", {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    '${_csrf.headerName}': '${_csrf.token}'
                },
                body: JSON.stringify(decision)
            });
        }

        async function deleteGroup(id, csrf_parameter_name, _csrf) {
            await fetch("/api/decisions/group/" + id, {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    '${_csrf.headerName}': _csrf
                }
            });
        }


    </script>
</@c.page>
