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
        <#--        <#assign path = (taskForm.taskType == FIVE_VARIANT)?string("/quiz/task/add/fiveVariant",-->
        <#--        (taskForm.taskType == YES_OR_NO)?string("/quiz/task/add/yesOrNo", ""))>-->
        <#--        <@task.task path taskForm groups decisions false/>-->
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

    </script>
</@c.page>
