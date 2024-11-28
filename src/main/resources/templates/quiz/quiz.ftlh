<#-- @ftlvariable name="task" type="com.example.servingwebcontent.model.quiz.QuizTask" -->
<#import "../parts/common.ftlh" as c>
<#import "../parts/taskForm.ftlh" as task>
<#import "../parts/decisions.ftlh" as d>
<#import "../parts/modal.ftlh" as modal>
<@c.page>
    <a class="btn btn-primary mb-3" href="/quiz/list">Назад</a>
    <h3 id="quiz-header" class="mb-3"></h3>
    <div id="quiz-update-form" class="mb-5"></div>
    <div class="row mb-5 text-bg-light">
        <div id="task-form" class="row g-3">

        </div>
        <#--                        <#assign path = (taskForm.taskType == FIVE_VARIANT)?string("/quiz/task/add/fiveVariant",-->
        <#--                        (taskForm.taskType == YES_OR_NO)?string("/quiz/task/add/yesOrNo", ""))>-->
        <#--                        <@task.task path taskForm groups decisions false/>-->
    </div>
    <table id="task-table" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 5%"></th>
            <th style="width: 55%"></th>
            <th style="width: 10%"></th>
            <th style="width: 10%"></th>
            <th style="width: 20%"></th>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>

    <script>

        $(async function () {
            await loadContent();
        });

        async function loadContent() {
            let quiz = await getQuiz(${quizId});
            fillQuizHeader(quiz.name);
            fillQuizChangeForm(quiz);
            let groupDecisions = await getGroupDecisions();
            let ungroupedDecisions = await getUngroupedDecisions();
            fillTaskFromByCreate(true, groupDecisions, ungroupedDecisions);
            let taskList = await getTaskList(quiz.id);
            fillTaskList(taskList);
        }

        function fillQuizHeader(quizName) {
            $('#quiz-header').html('Страница редактирования теста ' + quizName);
        }

        function fillQuizChangeForm(quiz) {
            $('#quiz-update-form').html(`
                <div class="row">
                    <div class="col-2">
                        <input class="form-control" type="text" name="shortName" value="` + quiz.shortName + `" placeholder="Краткое наименование" aria-describedby="validationQuizFormShortName"/>
                        <div id="validationQuizFormShortName" class="invalid-feedback"></div>
                    </div>
                    <div class="col-8">
                        <input class="form-control" type="text" name="name" value="` + quiz.name + `" placeholder="Наименование теста" aria-describedby="validationQuizFormName"/>
                        <div id="validationQuizFormName" class="invalid-feedback"></div>
                    </div>
                    <div class="col-2">
                        <button class="btn btn-primary" onclick="updateQuiz(` + quiz.id + `)">Изменить</button>
                    </div>
                </div>
            `);
        }

        function fillTaskFromByCreate(isFiveVariant, groupDecisions, ungroupedDecisions) {
            $('#task-form').html(`
                <div class="row mb-3 justify-content-md-center">
                    <button class="btn btn-primary col-3" type="submit">Добавить вопрос</button>
                </div>
                <div class="col-2 mb-3">
                    <div class="input-group">
                        <span class="input-group-text">№</span>
                        <input id="positionValue" class="form-control" type="number" min="1" name="position"/>
                    </div>
                </div>
                <div class="col-10 mb-3">
                    <div class="input-group col-9">
                        <span class="input-group-text">Текст задания</span>
                        <textarea id="inputText" class="form-control" name="preQuestionText"></textarea>
                    </div>
                </div>
                <div class="col-12 mb-3">
                    <div class="input-group">
                        <span class="input-group-text">Текст вопроса</span>
                        <textarea id="inputText" class="form-control"
                                  name="questionText"></textarea>
                    </div>
                </div>
                <div class="col-6 mb-3">
                    <label for="formFile" class="form-label">Вспомогательная картинка</label>
                    <input class="form-control" type="file" name="file" id="formFile">
                </div>
                <div class="col-6 mb-3"></div>
                <ul id="taskTabs" class="nav nav-tabs">
                    <li class="nav-item">
                        <a class="nav-link ` + (isFiveVariant ? 'active' : '') + `" href="#"
                           onclick="switchTab(this, '#fiveVariantTab')">Пять вариантов (Никогда - Всегда)</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link` + (isFiveVariant ? '' : 'active') + `" href="#"
                           onclick="switchTab(this, '#yesOrNoTab')">Два варианта (Да, Нет)</a>
                    </li>
                </ul>
                <div id="taskTypeTabs">
                    <div id="fiveVariantTab" class="input-group mb-3"
                         style="display: ` + (isFiveVariant ? '' : 'none') + `">
                        <span class="input-group-text">Никогда</span>
                        <input type="number" name="firstWeight" class="form-control" step="0.5" placeholder="0">
                        <span class="input-group-text">Редко</span>
                        <input type="number" name="secondWeight" class="form-control" step="0.5" placeholder="0">
                        <span class="input-group-text">Иногда</span>
                        <input type="number" name="thirdWeight" class="form-control" step="0.5" placeholder="2">
                        <span class="input-group-text">Часто</span>
                        <input type="number" name="fourthWeight" class="form-control" step="0.5" placeholder="3">
                        <span class="input-group-text">Всегда</span>
                        <input type="number" name="fifthWeight" class="form-control" step="0.5" placeholder="4">
                    </div>
                </div>
                <div id="yesOrNoTab" class="input-group mb-3"
                     style="display: ` + (isFiveVariant ? 'none' : '') + `">
                    <span class="input-group-text">Да</span>
                    <input type="number" name="yesWeight" class="form-control" step="0.5" placeholder="4">
                    <span class="input-group-text">Нет</span>
                    <input type="number" name="noWeight" class="form-control" step="0.5" placeholder="0">
                </div>
                <div class="mb-3">
                    <h5 class="mb-3">Решения</h5>
                    <div id="list-of-chosen-decisions"></div>
                    <div id="decisions-checkbox"></div>
                </div>
                <div class="row mb-2 justify-content-md-center">
                    <a href="#" class="btn btn-primary col-3" onclick="createTask()">Добавить вопрос</a>
                </div>
            `);
            let decisionsName = 'decisions';
            let chosenDecisions = new Set();
            $('#list-of-chosen-decisions').html(listOfChosenDecisions(decisionsName, chosenDecisions, groupDecisions, ungroupedDecisions));
            $('#decisions-checkbox').html(decisionsCheckbox(decisionsName, chosenDecisions, groupDecisions, ungroupedDecisions));
        }

        function listOfChosenDecisions(decisionsName, chosenDecisions, groups, ungroupedDecisions) {
            let result = $(`
                <div class="mb-3">
                    <div class="row mb-2">
                        <div class="col-4">
                            Выбранные решения:
                        </div>
                        <div class="col-4">
                            <input class="form-control" list="` + decisionsName + `-decisionList" id="` + decisionsName + `-list" placeholder="Вводи решение"/>
                            <datalist id="` + decisionsName + `-decisionList">
                            </datalist>
                        </div>
                        <div class="col-4">
                            <button type="button" class="btn btn-primary" onclick="clickByChosen('` + decisionsName + `-list', '` + decisionsName + `-decisionList')">Клик</button>
                        </div>
                    </div>
                    <div id="chosen-` + decisionsName + `">
                    </div>
                </div>
            `);
            let datalist = result.find('#' + decisionsName + '-decisionList');
            let chosen = result.find('#chosen-' + decisionsName);
            groups.forEach(group => {
                group.decisions.forEach(decision => {
                    datalist.append('<option for="#' + decisionsName + '-' + decision.id + '" value="' + group.name + ' _ ' + decision.name + '"/>');
                    if (chosenDecisions.has(decision.id)) {
                        chosen.append(chosenElement(
                            'group-' + group.id + '-' + decisionsName,
                            decisionsName + '-' + decision.id,
                            group.name + ' _ ' + decision.name
                        ));
                    }
                })
            })
            ungroupedDecisions.forEach(decision => {
                datalist.append('<option for="#' + decisionsName + '-' + decision.id + '" value="' + decision.name + '"/>');
                if (chosenDecisions.has(decision.id)) {
                    chosenDecisions.forEach(decision => {
                        chosen.append(chosenElement(
                            'group-nonGroup-' + decisionsName,
                            decisionsName + '-' + decision.id,
                            decision.name
                        ));
                    })
                }
            })
            return result;
        }

        function chosenElement(groupId, decisionId, name) {
            return $(`
                <div id="decision-chosen-` + decisionId + `">
                    <div class="p-2 mb-2 rounded-3 position-relative" style="background-color: #efadce">
                        <a onclick=decisionFocus('` + groupId + `','` + decisionId + `')>` + name + `</a>
                        <button onclick="$('#` + decisionId + `')[0].click()" type="button" class="btn-close position-absolute top-50 end-0 translate-middle-y" aria-label="Close"></button>
                    </div>
                </div>
            `)
        }

        function decisionsCheckbox(decisionsName, chosenDecisions, groups, ungroupedDecisions) {
            let result = $('<div class="accordion" id="accordion_groups"></div>');
            groups.forEach(group => {
                result.append(accordionGroup(
                    decisionsName,
                    'group-' + group.id + '-' + decisionsName,
                    group.name,
                    group.decisions,
                    chosenDecisions,
                    false
                ));
            })
            result.append(accordionGroup(
                decisionsName,
                'group-nonGroup-' + decisionsName,
                'Не в группе',
                ungroupedDecisions,
                chosenDecisions,
                true
            ));
            return result;
        }

        function accordionGroup(decisionsName, groupId, groupName, decisions, chosenDecisions, ungrouped) {
            return $(`
                <div class="accordion-item">
                    <h2 class="accordion-header">
                        <button id="` + groupId + `" class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#panelsStayOpen-` + groupId + `" aria-expanded="true" aria-controls="panelsStayOpen-` + groupId + `">
                            ` + groupName + `
                        </button>
                    </h2>
                    <div id="panelsStayOpen-` + groupId + `" class="accordion-collapse collapse">
                        <div class="row">
                            <div class="accordion-body col-12">
                                ` + (decisions.length === 0 ? '<p>Нет решений</p>' : `
                                    <table class="table table-striped">
                                        <thead>
                                        <tr>
                                            <th style="width: 60%"></th>
                                            <th style="width: 20%"></th>
                                            <th style="width: 20%"></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        ` + decisions.map(decision => {
                let decisionId = decisionsName + `-` + decision.id;
                return `
                                            <tr data-id="` + decision.name + `">
                                                <td>
                                                    <div class="form-check">
                                                        <input class="form-check-input" type="checkbox" name="` + decisionsName + `"
                                                               value="` + decision.id + `"
                                                               id="` + decisionId + `" ` + (chosenDecisions.has(decision.id) ? 'checked' : '') + `
                                                               onclick="chooseDecision(
                                                                       '#chosen-` + decisionsName + `',
                                                                       '` + (ungrouped ? '' : groupName + ' _ ') + decision.name + `',
                                                                       '` + groupId + `',
                                                                       '` + decisionId + `'
                                                                       )"
                                                        />
                                                        <label class="form-check-label" for="` + decisionId + `">
                                                            ` + decision.name + `
                                                        </label>
                                                    </div>
                                                </td>
                                                <td colspan="2">
                                                </td>
                                            </tr>
                                            `;
            }).join('') + `
                                        </tbody>
                                    </table>
                                `) + `
                            </div>
                        </div>
                    </div>
                </div>
            `);
        }

        function fillTaskList(taskList) {
            let table = $('#task-table tbody');
            table.html('');
            if (taskList.length === 0) {
                table.append(`
                    <tr>
                        <td colspan="5">Список вопросов пока пуст</td>
                    </tr>
                `);
            }
            taskList.forEach(task => {
                let tr = $(`
                    <tr>
                        <td>` + task.position + `</td>
                        <td>` + task.text + `</td>
                        <td><a href="#" onclick="fillTaskFormByUpdate(` + task.id + `)">Изменить</a></td>
                        <td></td>
                        <td>` + (task.decisionsCount === 0 ? '<div style="color: red">Решений не назначено</div>' : task.decisionsCount) + `</td>
                    </tr>
                `);
                tr.find('td')[3].html(deleteConfirm('deleteTask(' + task.id + ')', 'confirm-delete-task-' + task.id, 'Точно удалить вопрос?'))
                table.append(tr);
            })
        }

        async function getQuiz(id) {
            return getRequestWithError('/api/quiz/' + id, 'Ошибка при загрузке данных теста');
        }

        async function getTaskList(quizId) {
            return getRequestWithError('/api/quiz/task?quizId=' + quizId, 'Ошибка при загрузке списка вопросов');
        }

        async function getGroupDecisions() {
            return getRequestWithError("/api/decisions", 'Ошибка при загрузке решений с группами');
        }

        async function getUngroupedDecisions() {
            return getRequestWithError("/api/decisions/ungrouped", 'Ошибка при загрузке решений');
        }

        async function updateQuiz(id) {
            clearMessage();
            let form = $('#quiz-update-form');
            let body = {
                id: id,
                name: form.find("[name|='name']").val(),
                shortName: form.find("[name|='shortName']").val()
            };
            let response = await fetch("/api/quiz", {
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
                showSuccess('Тест ' + body.name + ' успешно изменена');
                fillQuizHeader(body.name);
            } else {
                response.json().then(json => {
                    fillFormInvalidFields(json, form);
                })
            }
        }

        async function deleteTask(taskId) {
            await deleteByPath("/api/quiz/task/" + taskId, 'Вопрос успешно удален', 'Возникли непредвиденные ошибки при попытке удаления вопроса');
            await loadContent();
        }

        function switchTab(el, tabId) {
            $("#taskTabs li a").removeClass("active");
            $("#taskTypeTabs").children().hide();

            $(el).addClass("active");
            $(tabId).show();
        }

    </script>
</@c.page>
