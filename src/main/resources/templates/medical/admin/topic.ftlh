<#import "../../parts/common.ftlh" as c>
<@c.page>
    <a class="btn btn-primary mb-3" href="/medical/list">Назад</a>
    <h3 id="medical-topic-header" class="mb-3"></h3>
    <div id="medical-topic-update-form" class="row mb-5"></div>
    <div class="mb-3 p-3 text-bg-light">
        <div id="medical-task-form" class="row">
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
        </div>
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
        <tbody></tbody>
    </table>

    <script>

        $(async function () {
            await loadContent();
        });

        async function loadContent() {
            let medicalTopic = await getMedicalTopic(${topicId});
            fillMedicalTopicHeader(medicalTopic.name);
            fillMedicalTopicChangeForm(medicalTopic);
            let groupDecisions = await getGroupDecisions();
            let ungroupedDecisions = await getUngroupedDecisions();
            fillMedicalTaskFromByCreate(medicalTopic.id, groupDecisions, ungroupedDecisions);
            let taskList = await getMedicalTaskList(medicalTopic.id);
            fillMedicalTaskList(taskList);
        }

        function fillMedicalTopicHeader(name) {
            $('#medical-topic-header').html('Страница редактирования ' + name);
        }

        function fillMedicalTopicChangeForm(medicalTopic) {
            $('#medical-topic-update-form').html(`
                <div class="input-group">
                    <input class="form-control" style="width: 75%" type="text" value="` + medicalTopic.name + `" name="name" aria-describedby="validationMedicalFormName"/>
                    <div id="validationMedicalFormName" class="invalid-feedback"></div>
                    <button class="btn btn-primary" onclick="updateMedicalTopic(` + medicalTopic.id + `)">Переименовать</button>
                </div>
            `);
        }

        function fillMedicalTaskFromByCreate(id, groupDecisions, ungroupedDecisions) {
            let decisionsLeftName = 'decisions-left';
            let decisionsRightName = 'decisions-right';
            $('#medical-task-form').html(`
                <div class="row mb-3 justify-content-md-center">
                    <button class="btn btn-primary col-3" onclick="createMedicalTask(` + id + `, ` + decisionsLeftName + `, ` + decisionsRightName + `)">Добавить анализ</button>
                </div>
                <div class="row mb-3">
                    <div class="col-10">
                        <div class="input-group">
                            <span class="input-group-text">Название анализа</span>
                            <input id="inputText" class="form-control" name="name" aria-describedby="validationMedicalTaskFormName"/>
                            <div id="validationMedicalTaskFormName" class="invalid-feedback"></div>
                        </div>
                    </div>
                    <div class="col-2">
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-speedometer"></i></span>
                            <input id="inputText" class="form-control" name="unit" aria-describedby="validationMedicalTaskFormUnit"/>
                            <div id="validationMedicalTaskFormUnit" class="invalid-feedback"></div>
                        </div>
                    </div>
                </div>
                <div class="input-group mb-3">
                    <span class="input-group-text">Левый референс</span>
                    <input type="number" name="leftLeft" class="form-control" step="0.01" aria-describedby="validationMedicalTaskFormReference"/>
                    <span class="input-group-text">Левый оптимум</span>
                    <input type="number" name="leftMid" class="form-control" step="0.01" aria-describedby="validationMedicalTaskFormReference"/>
                    <span class="input-group-text">Правый оптимум</span>
                    <input type="number" name="rightMid" class="form-control" step="0.01" aria-describedby="validationMedicalTaskFormReference"/>
                    <span class="input-group-text">Правый референс</span>
                    <input type="number" name="rightRight" class="form-control" step="0.01" aria-describedby="validationMedicalTaskFormReference"/>
                </div>
                <div id="validationMedicalTaskFormReference" class="invalid-feedback"></div>
                <div class="col-6 p-2 mb-3">
                    <div id="list-of-chosen-decisions-left"></div>
                    <div id="decisions-checkbox-left"></div>
                </div>
                <div class="col-6 p-2 mb-3">
                    <div id="list-of-chosen-decisions-right"></div>
                    <div id="decisions-checkbox-right"></div>
                </div>
                <div class="row mb-2 justify-content-md-center">
                    <button class="btn btn-primary col-3" onclick="createMedicalTask(` + id + `, ` + decisionsLeftName + `, ` + decisionsRightName + `)">Добавить анализ</button>
                </div>
            `);
            let chosenDecisions = new Set();
            $('#list-of-chosen-decisions-left').html(listOfChosenDecisions(decisionsLeftName, chosenDecisions, groupDecisions, ungroupedDecisions));
            $('#decisions-checkbox-left').html(decisionsCheckbox(decisionsLeftName, chosenDecisions, groupDecisions, ungroupedDecisions));
            $('#list-of-chosen-decisions-right').html(listOfChosenDecisions(decisionsRightName, chosenDecisions, groupDecisions, ungroupedDecisions));
            $('#decisions-checkbox-right').html(decisionsCheckbox(decisionsRightName, chosenDecisions, groupDecisions, ungroupedDecisions));
        }

        async function fillMedicalTaskFormByUpdate(id) {
            let task = await getMedicalTask(id);
            let decisionsLeftName = 'decisions-left';
            let decisionsRightName = 'decisions-right';
            $('#medical-task-form').html(`
                <div class="row mb-3 justify-content-md-center">
                    <button class="btn btn-primary col-3" onclick="updateMedicalTask(` + id + `, ` + decisionsLeftName + `, ` + decisionsRightName + `)">Обновить анализ</button>
                </div>
                <div class="row mb-3">
                    <div class="col-10">
                        <div class="input-group">
                            <span class="input-group-text">Название анализа</span>
                            <input id="inputText" class="form-control" name="name" value="` + task.name + `" aria-describedby="validationMedicalTaskFormName"/>
                            <div id="validationMedicalTaskFormName" class="invalid-feedback"></div>
                        </div>
                    </div>
                    <div class="col-2">
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-speedometer"></i></span>
                            <input id="inputText" class="form-control" name="unit" value="` + task.unit + `" aria-describedby="validationMedicalTaskFormUnit"/>
                            <div id="validationMedicalTaskFormUnit" class="invalid-feedback"></div>
                        </div>
                    </div>
                </div>
                <div class="input-group mb-3">
                    <span class="input-group-text">Левый референс</span>
                    <input type="number" name="leftLeft" class="form-control" step="0.01" value="` + task.leftLeft + `" aria-describedby="validationMedicalTaskFormReference"/>
                    <span class="input-group-text">Левый оптимум</span>
                    <input type="number" name="leftMid" class="form-control" step="0.01" value="` + task.leftMid + `" aria-describedby="validationMedicalTaskFormReference"/>
                    <span class="input-group-text">Правый оптимум</span>
                    <input type="number" name="rightMid" class="form-control" step="0.01" value="` + task.rightMid + `" aria-describedby="validationMedicalTaskFormReference"/>
                    <span class="input-group-text">Правый референс</span>
                    <input type="number" name="rightRight" class="form-control" step="0.01" value="` + task.rightRight + `" aria-describedby="validationMedicalTaskFormReference"/>
                </div>
                <div id="validationMedicalTaskFormReference" class="invalid-feedback"></div>
                <div class="col-6 p-2 mb-3">
                    <div id="list-of-chosen-decisions-left"></div>
                    <div id="decisions-checkbox-left"></div>
                </div>
                <div class="col-6 p-2 mb-3">
                    <div id="list-of-chosen-decisions-right"></div>
                    <div id="decisions-checkbox-right"></div>
                </div>
                <div class="row mb-2 justify-content-md-center">
                    <button class="btn btn-primary col-3" onclick="updateMedicalTask(` + id + `, ` + decisionsLeftName + `, ` + decisionsRightName + `)">Обновить анализ</button>
                </div>
            `);
            let groupDecisions = await getGroupDecisions();
            let ungroupedDecisions = await getUngroupedDecisions();
            let leftChosenDecisions = new Set();
            task.decisionIds.forEach(decisionId => {
                leftChosenDecisions.add(decisionId);
            });
            $('#list-of-chosen-decisions-left').html(listOfChosenDecisions(decisionsLeftName, leftChosenDecisions, groupDecisions, ungroupedDecisions));
            $('#decisions-checkbox-left').html(decisionsCheckbox(decisionsLeftName, leftChosenDecisions, groupDecisions, ungroupedDecisions));
            let rightChosenDecisions = new Set();
            task.decisionIds.forEach(decisionId => {
                rightChosenDecisions.add(decisionId);
            });
            $('#list-of-chosen-decisions-right').html(listOfChosenDecisions(decisionsRightName, rightChosenDecisions, groupDecisions, ungroupedDecisions));
            $('#decisions-checkbox-right').html(decisionsCheckbox(decisionsRightName, rightChosenDecisions, groupDecisions, ungroupedDecisions));
        }

        function fillMedicalTaskList(taskList) {
            let table = $('#medical-topic-task-table tbody');
            table.html('');
            if (taskList.length === 0) {
                table.append(`
                    <tr>
                        <td colspan="5">Список анализов пока пуст</td>
                    </tr>
                `);
            }
            taskList.forEach(task => {
                table.append($(`
                    <tr>
                        <td>` + task.name + `</td>
                        <td><a href="#" onclick="fillMedicalTaskFormByUpdate(` + task.id + `)">Изменить</a></td>
                        <td>` + deleteConfirmString('deleteMedicalTask(' + task.id + ')', 'confirm-delete-medical-task-' + task.id, 'Точно удалить анализ?') + `</td>
                        <td>` + (task.leftDecisionsSize === 0 ? '<div style="color: red">Левых решений не назначено</div>' : '') + `</td>
                        <td>` + (task.rightDecisionsSize === 0 ? '<div style="color: red">Правых решений не назначено</div>' : '') + `</td>
                    </tr>
                `));
            })
        }

        async function updateMedicalTopic(id) {
            clearMessage();
            let form = $('#medical-topic-update-form');
            let body = {
                id: id,
                name: form.find("[name|='name']").val()
            };
            let response = await fetch("/api/medical", {
                method: 'PUT',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    '${_csrf.headerName}': '${_csrf.token}'
                },
                body: JSON.stringify(body)
            });
            if (response.ok) {
                clearFromInvalidFields(form);
                showSuccess('Анализ ' + body.name + ' успешно изменена');
                fillMedicalTopicHeader(body.name);
            } else {
                response.json().then(json => {
                    fillFormInvalidFields(json, form);
                })
            }
        }

        async function createMedicalTask(topicId, decisionsLeftName, decisionsRightName) {
            clearMessage();
            let form = $('#medical-task-form');
            let leftDecisions = [];
            form.find("[name|='" + decisionsLeftName + "']").each(function () {
                let checkbox = $(this);
                if (checkbox.is(':checked')) {
                    leftDecisions.push(checkbox.val());
                }
            })
            let rightDecisions = [];
            form.find("[name|='" + decisionsRightName + "']").each(function () {
                let checkbox = $(this);
                if (checkbox.is(':checked')) {
                    rightDecisions.push(checkbox.val());
                }
            })
            let taskCreateCommand = {
                topicId: topicId,
                name: form.find("[name|='name']").val(),
                unit: form.find("[name|='unit']").val(),
                leftLeft: form.find("[name|='leftLeft']").val(),
                leftMid: form.find("[name|='leftMid']").val(),
                rightMid: form.find("[name|='rightMid']").val(),
                rightRight: form.find("[name|='rightRight']").val(),
                leftDecisionIds: leftDecisions,
                rightDecisionIds: rightDecisions
            };

            let response = await fetch("/api/medical/task", {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    '${_csrf.headerName}': '${_csrf.token}'
                },
                body: JSON.stringify(taskCreateCommand)
            });
            if (response.ok) {
                showSuccess('Анализ успешно создан');
                await loadContent();
            } else {
                response.json().then(json => {
                    fillFormInvalidFields(json, form);
                })
            }
        }

        async function getMedicalTopic(topicId) {
            return getRequestWithError('/api/medical/' + topicId, 'Ошибка при загрузке списка анализов');
        }

        async function getMedicalTaskList(medicalTopicId) {
            return getRequestWithError('/api/medical/task/all?medicalTopicId=' + medicalTopicId, 'Ошибка при загрузке списка анализов');
        }

        async function getMedicalTask(medicalTaskId) {
            return getRequestWithError('/api/medical/task/' + medicalTaskId, 'Ошибка при загрузке анализа');
        }
    </script>
</@c.page>