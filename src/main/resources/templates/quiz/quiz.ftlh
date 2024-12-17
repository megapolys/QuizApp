<#import "../parts/common.ftlh" as c>
<@c.page>
    <a class="btn btn-primary mb-3" href="/quiz/list">Назад</a>
    <h3 id="quiz-header" class="mb-3"></h3>
    <div id="quiz-update-form" class="mb-5"></div>
    <div class="row mb-5 text-bg-light">
        <div id="task-form" class="row g-3"></div>
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
            let taskList = await getTaskList(quiz.id);
            let position = getNextTaskPosition(taskList);
            fillTaskFromByCreate(quiz.id, true, groupDecisions, ungroupedDecisions, position);
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

        function fillTaskFromByCreate(quizId, isFiveVariant, groupDecisions, ungroupedDecisions, position) {
            let decisionsName = 'decisions';
            $('#task-form').html(`
                <div class="row mb-3 justify-content-md-center">
                    <a href="#" class="btn btn-primary col-3" onclick="createTask(` + quizId + `, '` + decisionsName + `')">Добавить вопрос</a>
                </div>
                <div class="col-2 mb-3">
                    <div class="input-group">
                        <span class="input-group-text">№</span>
                        <input id="positionValue" class="form-control" type="number" min="1" name="position" value="` + position + `" aria-describedby="validationTaskFormPosition"/>
                        <div id="validationTaskFormPosition" class="invalid-feedback"></div>
                    </div>
                </div>
                <div class="col-10 mb-3">
                    <div class="input-group col-9">
                        <span class="input-group-text">Текст задания</span>
                        <textarea id="inputText" class="form-control" name="preQuestionText" aria-describedby="validationTaskFormPreQuesionText"></textarea>
                        <div id="validationTaskFormPreQuesionText" class="invalid-feedback"></div>
                    </div>
                </div>
                <div class="col-12 mb-3">
                    <div class="input-group">
                        <span class="input-group-text">Текст вопроса</span>
                        <textarea id="inputText" class="form-control" name="questionText" aria-describedby="validationTaskFormQuesionText"></textarea>
                        <div id="validationTaskFormQuesionText" class="invalid-feedback"></div>
                    </div>
                </div>
                <div class="col-6 mb-3">
                    <label for="formFile" class="form-label">Вспомогательная картинка</label>
                    <input class="form-control" type="file" name="file" id="formFile">
                </div>
                <div class="col-6 mb-3"></div>
                <ul id="task-tabs" class="nav nav-tabs">
                    <li class="nav-item">
                        <a class="nav-link ` + (isFiveVariant ? 'active' : '') + `" href="#!"
                           onclick="switchTab(this, '#five-variant-tab')">Пять вариантов (Никогда - Всегда)</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link` + (isFiveVariant ? '' : 'active') + `" href="#!"
                           onclick="switchTab(this, '#yes-or-no-tab')">Два варианта (Да, Нет)</a>
                    </li>
                </ul>
                <div id="task-type-tabs"></div>
                <div class="mb-3">
                    <h5 class="mb-3">Решения</h5>
                    <div id="list-of-chosen-decisions"></div>
                    <div id="decisions-checkbox"></div>
                </div>
                <div class="row mb-2 justify-content-md-center">
                    <a href="#" class="btn btn-primary col-3" onclick="createTask(` + quizId + `, '` + decisionsName + `')">Добавить вопрос</a>
                </div>
            `);
            let taskTypeTabs = $('#task-type-tabs');
            appendFiveVariantTab(taskTypeTabs, isFiveVariant, null);
            appendYesOrNoTab(taskTypeTabs, !isFiveVariant, null);
            let chosenDecisions = new Set();
            $('#list-of-chosen-decisions').html(listOfChosenDecisions(decisionsName, chosenDecisions, groupDecisions, ungroupedDecisions));
            $('#decisions-checkbox').html(decisionsCheckbox(decisionsName, chosenDecisions, groupDecisions, ungroupedDecisions));
        }

        async function fillTaskFormByUpdate(taskId) {
            let task = await getTask(taskId);
            let position = task.position;
            let quizId = task.quizId;
            let isFiveVariant = !!task.fiveVariantTask;
            let preQuestionText = isFiveVariant ? task.fiveVariantTask.preQuestionText : task.yesOrNoTask.preQuestionText;
            let questionText = isFiveVariant ? task.fiveVariantTask.questionText : task.yesOrNoTask.questionText;
            let fileName = isFiveVariant ? task.fiveVariantTask.fileName : task.yesOrNoTask.fileName;
            let decisionsName = 'decisions';
            $('#task-form').html(`
                <div class="row mb-3 justify-content-md-center">
                    <a href="#" class="btn btn-primary col-4" onclick="loadContent()">Отменить изменение</a>
                    <div class="col-5"></div>
                    <a href="#" class="btn btn-primary col-3" onclick="updateTask(` + quizId + `, ` + taskId + `, '` + decisionsName + `')">Изменить вопрос</a>
                </div>
                <div class="col-2 mb-3">
                    <div class="input-group">
                        <span class="input-group-text">№</span>
                        <input id="positionValue" class="form-control" type="number" min="1" name="position" value="` + position + `" aria-describedby="validationTaskFormPosition"/>
                        <div id="validationTaskFormPosition" class="invalid-feedback"></div>
                    </div>
                </div>
                <div class="col-10 mb-3">
                    <div class="input-group col-9">
                        <span class="input-group-text">Текст задания</span>
                        <textarea id="inputText" class="form-control" name="preQuestionText" aria-describedby="validationTaskFormPreQuesionText">` + preQuestionText + `</textarea>
                        <div id="validationTaskFormPreQuesionText" class="invalid-feedback"></div>
                    </div>
                </div>
                <div class="col-12 mb-3">
                    <div class="input-group">
                        <span class="input-group-text">Текст вопроса</span>
                        <textarea id="inputText" class="form-control" name="questionText" aria-describedby="validationTaskFormQuesionText">` + questionText + `</textarea>
                        <div id="validationTaskFormQuesionText" class="invalid-feedback"></div>
                    </div>
                </div>
                <div class="col-6 mb-3">
                    <label for="formFile" class="form-label">Вспомогательная картинка</label>
                    <input class="form-control" type="file" name="file" id="formFile">
                </div>
                <div class="col-6 mb-3">
                    ` + (fileName ? `
                    <div>Выбрана картинка: ` + fileName + `</div>
                    <div>
                        <input class="form-check-input" type="checkbox" name="deleteFile" id="deleteFileCheckbox">
                        <label class="form-check-label" for="deleteFileCheckbox">Удалить картинку</label>
                    </div>` : '') + `
                </div>
                <ul id="task-tabs" class="nav nav-tabs">
                    <li class="nav-item">
                        <a class="nav-link ` + (isFiveVariant ? 'active' : '') + `" href="#!"
                           onclick="switchTab(this, '#five-variant-tab')">Пять вариантов (Никогда - Всегда)</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ` + (isFiveVariant ? '' : 'active') + `" href="#!"
                           onclick="switchTab(this, '#yes-or-no-tab')">Два варианта (Да, Нет)</a>
                    </li>
                </ul>
                <div id="task-type-tabs"></div>
                <div class="mb-3">
                    <h5 class="mb-3">Решения</h5>
                    <div id="list-of-chosen-decisions"></div>
                    <div id="decisions-checkbox"></div>
                </div>
                <div class="row mb-2 justify-content-md-center">
                    <a href="#" class="btn btn-primary col-3" onclick="updateTask(` + quizId + `, ` + taskId + `, '` + decisionsName + `')">Изменить вопрос</a>
                </div>
            `);
            let taskTypeTabs = $('#task-type-tabs');
            appendFiveVariantTab(taskTypeTabs, isFiveVariant, task.fiveVariantTask);
            appendYesOrNoTab(taskTypeTabs, !isFiveVariant, task.yesOrNoTask);
            let groupDecisions = await getGroupDecisions();
            let ungroupedDecisions = await getUngroupedDecisions();
            let chosenDecisions = new Set();
            task.decisionIds.forEach(decisionId => {
                chosenDecisions.add(decisionId);
            });
            $('#list-of-chosen-decisions').html(listOfChosenDecisions(decisionsName, chosenDecisions, groupDecisions, ungroupedDecisions));
            $('#decisions-checkbox').html(decisionsCheckbox(decisionsName, chosenDecisions, groupDecisions, ungroupedDecisions));
        }

        async function appendFiveVariantTab(appendTo, active, task) {
            let property = await getFiveVariantProperty();
            appendTo.append($(`
                <div id="five-variant-tab" class="input-group mb-3" style="display: ` + (active ? '' : 'none') + `">
                    <span class="input-group-text">Никогда</span>
                    <input type="number" name="firstWeight" class="form-control" step="0.5" placeholder="` + property.firstWeight + `" value="` + (task ? task.firstWeight : '') + `">
                    <span class="input-group-text">Редко</span>
                    <input type="number" name="secondWeight" class="form-control" step="0.5" placeholder="` + property.secondWeight + `" value="` + (task ? task.secondWeight : '') + `">
                    <span class="input-group-text">Иногда</span>
                    <input type="number" name="thirdWeight" class="form-control" step="0.5" placeholder="` + property.thirdWeight + `" value="` + (task ? task.thirdWeight : '') + `">
                    <span class="input-group-text">Часто</span>
                    <input type="number" name="fourthWeight" class="form-control" step="0.5" placeholder="` + property.fourthWeight + `" value="` + (task ? task.fourthWeight : '') + `">
                    <span class="input-group-text">Всегда</span>
                    <input type="number" name="fifthWeight" class="form-control" step="0.5" placeholder="` + property.fifthWeight + `" value="` + (task ? task.fifthWeight : '') + `">
                </div>
            `));
        }

        async function appendYesOrNoTab(appendTo, active, task) {
            let property = await getYesOrNoProperty();
            appendTo.append($(`
                <div id="yes-or-no-tab" class="input-group mb-3" style="display: ` + (active ? '' : 'none') + `">
                    <span class="input-group-text">Да</span>
                    <input type="number" name="yesWeight" class="form-control" step="0.5" placeholder="` + property.yesWeight + `" value="` + (task ? task.yesWeight : '') + `">
                    <span class="input-group-text">Нет</span>
                    <input type="number" name="noWeight" class="form-control" step="0.5" placeholder="` + property.noWeight + `" value="` + (task ? task.noWeight : '') + `">
                </div>
            `));
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
                table.append($(`
                    <tr>
                        <td>` + task.position + `</td>
                        <td>` + task.text + `</td>
                        <td><a href="#" onclick="fillTaskFormByUpdate(` + task.id + `)">Изменить</a></td>
                        <td>` + deleteConfirmString('deleteTask(' + task.id + ')', 'confirm-delete-task-' + task.id, 'Точно удалить вопрос?') + `</td>
                        <td>` + (task.decisionsCount === 0 ? '<div style="color: red">Решений не назначено</div>' : task.decisionsCount) + `</td>
                    </tr>
                `));
            })
        }

        async function getQuiz(id) {
            return getRequestWithError('/api/quiz/' + id, 'Ошибка при загрузке данных теста');
        }

        async function getTaskList(quizId) {
            return getRequestWithError('/api/quiz/task?quizId=' + quizId, 'Ошибка при загрузке списка вопросов');
        }

        async function getTask(taskId) {
            return getRequestWithError('/api/quiz/task?taskId=' + taskId, 'Ошибка при загрузке вопроса');
        }

        async function getFiveVariantProperty() {
            return getRequestWithError("/api/property/fiveVariant", 'Ошибка при загрузке дефолтных значений для вопроса 5 вариантов');
        }

        async function getYesOrNoProperty() {
            return getRequestWithError("/api/property/yesOrNo", 'Ошибка при загрузке дефолтных значений для вопроса да/нет');
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

        async function createTask(quizId, decisionsName) {
            clearMessage();
            let form = $('#task-form');
            let decisions = [];
            form.find("[name|='" + decisionsName + "']").each(function () {
                let checkbox = $(this);
                if (checkbox.is(':checked')) {
                    decisions.push(checkbox.val());
                }
            })
            let fiveVariant = null;
            let fiveVariantTab = $('#five-variant-tab');
            if (fiveVariantTab.css('display') !== 'none') {
                fiveVariant = {
                    firstWeight: fiveVariantTab.find("[name|='firstWeight']").val(),
                    secondWeight: fiveVariantTab.find("[name|='secondWeight']").val(),
                    thirdWeight: fiveVariantTab.find("[name|='thirdWeight']").val(),
                    fourthWeight: fiveVariantTab.find("[name|='fourthWeight']").val(),
                    fifthWeight: fiveVariantTab.find("[name|='fifthWeight']").val()
                }
            }
            let yesOrNo = null;
            let yesOrNoTab = $('#yes-or-no-tab');
            if (yesOrNoTab.css('display') !== 'none') {
                yesOrNo = {
                    yesWeight: yesOrNoTab.find("[name|='yesWeight']").val(),
                    noWeight: yesOrNoTab.find("[name|='noWeight']").val()
                }
            }
            let taskCreateCommand = {
                quizId: quizId,
                position: form.find("[name|='position']").val(),
                preQuestionText: form.find("[name|='preQuestionText']").val(),
                questionText: form.find("[name|='questionText']").val(),
                decisionIds: decisions,
                fiveVariant: fiveVariant,
                yesOrNo: yesOrNo,
            };
            let formData = new FormData();

            formData.append('taskCreateCommand', new Blob([JSON.stringify(taskCreateCommand)], {
                type: 'application/json'
            }), '');
            let file = form.find("[name|='file']").prop('files')[0];
            formData.append("file", file);
            let response = await fetch("/api/quiz/task", {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    '${_csrf.headerName}': '${_csrf.token}'
                },
                body: formData
            });
            if (response.ok) {
                showSuccess('Вопрос успешно сохранен');
                await loadContent();
            } else {
                response.json().then(json => {
                    fillFormInvalidFields(json, form);
                })
            }
        }

        async function updateTask(quizId, taskId, decisionsName) {
            clearMessage();
            let form = $('#task-form');
            let decisions = [];
            form.find("[name|='" + decisionsName + "']").each(function () {
                let checkbox = $(this);
                if (checkbox.is(':checked')) {
                    decisions.push(checkbox.val());
                }
            })
            let fiveVariant = null;
            let fiveVariantTab = $('#five-variant-tab');
            if (fiveVariantTab.css('display') !== 'none') {
                fiveVariant = {
                    firstWeight: fiveVariantTab.find("[name|='firstWeight']").val(),
                    secondWeight: fiveVariantTab.find("[name|='secondWeight']").val(),
                    thirdWeight: fiveVariantTab.find("[name|='thirdWeight']").val(),
                    fourthWeight: fiveVariantTab.find("[name|='fourthWeight']").val(),
                    fifthWeight: fiveVariantTab.find("[name|='fifthWeight']").val()
                }
            }
            let yesOrNo = null;
            let yesOrNoTab = $('#yes-or-no-tab');
            if (yesOrNoTab.css('display') !== 'none') {
                yesOrNo = {
                    yesWeight: yesOrNoTab.find("[name|='yesWeight']").val(),
                    noWeight: yesOrNoTab.find("[name|='noWeight']").val()
                }
            }
            let taskUpdateCommand = {
                position: form.find("[name|='position']").val(),
                preQuestionText: form.find("[name|='preQuestionText']").val(),
                questionText: form.find("[name|='questionText']").val(),
                decisionIds: decisions,
                fiveVariant: fiveVariant,
                yesOrNo: yesOrNo,
                deleteFile: form.find("[name|='deleteFile']").is(':checked')
            };
            let formData = new FormData();

            formData.append('taskUpdateCommand', new Blob([JSON.stringify(taskUpdateCommand)], {
                type: 'application/json'
            }), '');
            let file = form.find("[name|='file']").prop('files')[0];
            formData.append("file", file);
            let response = await fetch("/api/quiz/task/" + taskId, {
                method: 'PUT',
                headers: {
                    'Accept': 'application/json',
                    '${_csrf.headerName}': '${_csrf.token}'
                },
                body: formData
            });
            if (response.ok) {
                showSuccess('Вопрос успешно изменен');
                await loadContent();
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
            $("#task-tabs li a").removeClass("active");
            $("#task-type-tabs").children().hide();

            $(el).addClass("active");
            $(tabId).show();
        }

        function getNextTaskPosition(taskList) {
            if (taskList) {
                for (let i = 1; i < taskList.length + 1; i++) {
                    if (i !== taskList[i - 1].position) {
                        return i;
                    }
                }
                return taskList.length + 1;
            } else {
                return 1;
            }
        }

    </script>
</@c.page>
