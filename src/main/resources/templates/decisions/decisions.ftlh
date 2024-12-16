<#-- @ftlvariable name="changeDecision" type="com.example.servingwebcontent.model.decision.Decision" -->
<#-- @ftlvariable name="changeGroup" type="com.example.servingwebcontent.model.decision.GroupWithDecisions" -->
<#import "../parts/common.ftlh" as c>
<#import "../parts/decisions.ftlh" as d>
<@c.page>
    <h3 class="mb-3">Список решений</h3>
    <h5 class="mb-3">Группа</h5>
    <div id="group-form" class="mb-3"></div>
    <h5 class="mb-3">Решение</h5>
    <div id="decision-form" class="mb-3"></div>

<#--    <@d.decisions groups=groups decisions_=decisions/>-->
    <div id="groupDecisionContent"></div>

    <script>
        $(async function () {
            await loadContent();
        });

        async function loadContent() {
            console.log('laod content');
            clearForms();
            let groupDecisionContent = $("#groupDecisionContent");
            let groupDecisions = await getGroupDecisions();
            let ungroupedDecisions = await getUngroupedDecisions();
            groupDecisionContent.html('<div class="accordion" id="accordion-groups"></div>');
            let accordionGroups = $("#accordion-groups");
            let decisionsName = 'decisions';
            if (groupDecisions.length > 0) {
                groupDecisions.forEach(groupDecision => {
                    let id = groupDecision.id;
                    accordionGroup(accordionGroups, groupDecision.name, groupDecision.decisions, decisionsName, id);
                })
            }
            accordionGroup(accordionGroups, "Не в группе", ungroupedDecisions, decisionsName, "nonGroup");
        }

        function accordionGroup(accordionGroups, groupName, decisions, decisionsName, id) {
            let groupId = id + '-' + decisionsName;
            let decisionGroupId = 'decisions-' + groupId;
            accordionGroups.append(
                '<div class="accordion-item">' +
                '<h2 class="accordion-header">' +
                '<button id="group-' + groupId + '" class="accordion-button " type="button" data-bs-toggle="collapse" data-bs-target="#panelsStayOpen-group-' + groupId + '" aria-expanded="true" aria-controls="panelsStayOpen-group-' + groupId + '">' +
                groupName +
                '</button>' +
                '</h2>' +
                '' +
                '<div id="panelsStayOpen-group-' + groupId + '" class="accordion-collapse collapse show">' +
                '<div class="row">' +
                '' +
                '<div class="col-2">' +
                (id !== "nonGroup" ? '<div class="m-3">' +
                    '<a href="#" onclick="fillGroupFormByUpdate(' + id + ')">Изменить группу</a>' +
                    '</div>' : '') +
                '</div>' +
                '' +
                '<div class="accordion-body col-10">' +
                (decisions.length === 0 ? 'Нет решений' :
                        '<table class="table table-striped">' +
                        '<thead>' +
                        '<tr>' +
                        '<th style="width: 60%"></th>' +
                        '<th style="width: 20%"></th>' +
                        '<th style="width: 20%"></th>' +
                        '</tr>' +
                        '</thead>' +
                        '<tbody id="' + decisionGroupId + '">' +
                        '</tbody>' +
                        '</table>'
                ) +
                '</div>' +
                '' +
                '</div>' +
                '</div>' +
                '</div>'
            );
            let groupAccordionBody = $('#' + decisionGroupId);
            decisions.forEach(decision => {
                let decisionDescriptionModalId = 'decision-modal-' + decision.id;
                let decisionTr = $('<tr></tr>');
                decisionTr.append($('<td></td>').append(
                    !!decision.description ?
                        '<a href="##" type="button" class="icon-link icon-link-hover text-decoration-none p-0" style="--bs-icon-link-transform: translate3d(0, -.125rem, 0);"' +
                        ' data-bs-toggle="modal" data-bs-target="#' + decisionDescriptionModalId + '" >' +
                        '<i class="bi bi-clipboard me-1"></i>' +
                        decision.name +
                        '</a>' +

                        '<div class="modal fade" id="' + decisionDescriptionModalId + '" tabindex="-1" aria-hidden="true">' +
                        '<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">' +
                        '<div class="modal-content">' +
                        '<div class="modal-header">' +
                        '<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>' +
                        '</div>' +
                        '<div class="modal-body">' +
                        decision.description +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>'
                        : '<a href="##" class="text-decoration-none" aria-disabled="true">' + decision.name + '</a>')
                );
                decisionTr.append($('<td></td>').append('<a href="#" onclick="fillDecisionFormByUpdate(' + decision.id + ')">Изменить</a>'));
                decisionTr.append($('<td></td>').append(deleteConfirm('deleteDecision(' + decision.id + ')', 'confirm-delete-decision-' + decision.id, 'Точно удалить решение?')));
                groupAccordionBody.append(decisionTr);
            })
        }

        function clearForms() {
            fillGroupFromByCreate();
            fillDecisionFormByCreate();
        }

        function fillGroupFromByCreate() {
            $('#group-form').html(`
                <div class="row">
                    <div class="col-4">
                        <input class="form-control" type="text" name="name" placeholder="Группа" aria-describedby="validationGroupFormName"/>
                        <div id="validationGroupFormName" class="invalid-feedback"></div>
                    </div>
                    <div class="col-2">
                        <button class="btn btn-primary" onclick="addGroup()">Добавить</button>
                    </div>
                </div>
                `);
        }

        async function fillGroupFormByUpdate(groupId) {
            clearMessage();
            let group = await getGroupById(groupId);
            $('#group-form').html(`
                <div class="row">
                    <div class="col-4">
                        <input class="form-control" type="text" name="name" placeholder="Группа" value="` + group.name + `" aria-describedby="validationGroupFormName"/>
                        <div id="validationGroupFormName" class="invalid-feedback"></div>
                    </div>
                    <div class="col-2">
                        <a class="btn btn-primary" href="#" onclick="updateGroup(` + groupId + `)">Переименовать</a>
                    </div>
                    <div class="col-2"></div>
                    <div class="col-4">
                    <a class="btn btn-danger" href="#" onclick="deleteGroup(` + groupId + `)">Расформировать</a>
                    </div>
                </div>
            `);
        }

        async function fillDecisionFormByCreate() {
            let groups = await getGroups();
            $('#decision-form').html(`
                <div class="row mb-3">
                    <div class="col-4">
                        <select name="group" class="form-select" aria-label="group_list">
                            <option value=""></option>
                            ` + groups.map(group => {
                return '<option value="' + group.id + '">' + group.name + '</option>';
            }).join('') + `
                        </select>
                    </div>
                    <div class="col-8">
                        <input class="form-control" type="text" name="name" placeholder="Наименование решения"
                               aria-describedby="validationDecisionFormName"/>
                        <div id="validationDecisionFormName" class="invalid-feedback"></div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-8">
                        <textarea class="form-control" name="description" placeholder="Описание"></textarea>
                    </div>
                    <div class="col-4">
                        <button class="btn btn-primary" onclick="addDecision()">Добавить решение</button>
                    </div>
                </div>
            `);
        }

        async function fillDecisionFormByUpdate(decisionId) {
            clearMessage();
            let groups = await getGroups();
            let decision = await getDecisionById(decisionId);
            $('#decision-form').html(`
                <div class="row mb-3">
                    <div class="col-4">
                        <select name="group" class="form-select" aria-label="group_list">
                            <option value=""></option>
                            ` + groups.map(group => {
                return '<option value="' + group.id + '"' + (group.id === decision.groupId ? 'selected' : '') + '>' + group.name + '</option>';
            }).join('') + `
                        </select>
                    </div>
                    <div class="col-8">
                        <input class="form-control" type="text" name="name" placeholder="Наименование решения"
                               value="` + (!decision.name ? '' : decision.name) + `" aria-describedby="validationDecisionFormName"/>
                        <div id="validationDecisionFormName" class="invalid-feedback"></div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-8">
                        <textarea class="form-control" name="description"
                        placeholder="Описание">` + (!decision.description ? '' : decision.description) + `</textarea>
                    </div>
                    <div class="col-4">
                        <button class="btn btn-primary" onclick="updateDecision(` + decisionId + `)">Изменить решение</button>
                    </div>
                </div>
            `);
        }

        async function getGroups() {
            return getRequestWithError("/api/decisions/group", 'Ошибка при загрузке групп');
        }

        async function getGroupById(groupId) {
            return getRequestWithError("/api/decisions/group/" + groupId, 'Ошибка при попытки получения группы по идентификатору');
        }

        async function getDecisionById(decisionId) {
            return getRequestWithError("/api/decisions/" + decisionId, 'Ошибка при попытки получения решения по идентификатору');
        }

        async function addGroup() {
            let form = $('#group-form');
            let group = {
                id: null,
                name: form.find("[name|='name']").val()
            };
            let response = await fetch("/api/decisions/group", {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    '${_csrf.headerName}': '${_csrf.token}'
                },
                body: JSON.stringify(group)
            });
            if (response.ok) {
                showSuccess('Группа ' + group.name + ' успешно создана');
                await loadContent();
            } else {
                response.json().then(json => {
                    fillFormInvalidFields(json, form);
                })
            }
        }

        async function updateGroup(groupId) {
            let form = $('#group-form');
            let group = {
                id: groupId,
                name: form.find("[name|='name']").val()
            };
            let response = await fetch("/api/decisions/group", {
                method: 'PUT',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    '${_csrf.headerName}': '${_csrf.token}'
                },
                body: JSON.stringify(group)
            });
            if (response.ok) {
                showSuccess('Группа ' + group.name + ' успешно изменена');
                await loadContent();
            } else {
                response.json().then(json => {
                    fillFormInvalidFields(json, form);
                })
            }
        }

        async function addDecision() {
            let form = $('#decision-form');
            let decision = {
                id: null,
                name: form.find("[name|='name']").val(),
                description: form.find("[name|='description']").val(),
                groupId: form.find("[name|='group']").val()
            };
            let response = await fetch("/api/decisions", {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    '${_csrf.headerName}': '${_csrf.token}'
                },
                body: JSON.stringify(decision)
            });
            if (response.ok) {
                showSuccess('Решение ' + decision.name + ' успешно создано');
                await loadContent();
            } else {
                response.json().then(json => {
                    fillFormInvalidFields(json, form);
                })
            }
        }

        async function updateDecision(decisionId) {
            let form = $('#decision-form');
            let decision = {
                id: decisionId,
                name: form.find("[name|='name']").val(),
                description: form.find("[name|='description']").val(),
                groupId: form.find("[name|='group']").val()
            };
            let response = await fetch("/api/decisions", {
                method: 'PUT',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    '${_csrf.headerName}': '${_csrf.token}'
                },
                body: JSON.stringify(decision)
            });
            if (response.ok) {
                showSuccess('Решение ' + decision.name + ' успешно изменено');
                await loadContent();
            } else {
                response.json().then(json => {
                    fillFormInvalidFields(json, form);
                })
            }
        }

        async function deleteGroup(id) {
            await deleteByPath("/api/decisions/group/" + id, 'Группа успешно удалена', 'Возникли непредвиденные ошибки при попытке удаления группы');
            await loadContent();
        }

        async function deleteDecision(id) {
            await deleteByPath("/api/decisions/" + id, 'Решение успешно удалено', 'Возникли непредвиденные ошибки при попытке удаления решения');
            await loadContent();
        }

    </script>
</@c.page>
