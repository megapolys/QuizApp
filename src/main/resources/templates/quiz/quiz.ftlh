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
    <table class="table table-striped">
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
        <#--        <#list taskList as task>-->
        <#--            <tr>-->
        <#--                <td>${task.position}</td>-->
        <#--                <td>${(task.fiveVariantTask.questionText)!(task.yesOrNoTask.questionText)?truncate(50, "...")}</td>-->
        <#--                <#assign type = task.fiveVariantTask?has_content?string("fiveVariant", "yesOrNo")>-->
        <#--                <td><a href="/quiz/task/update/${type}/${task.id?c}">Изменить</a></td>-->
        <#--                <td>-->
        <#--                    <@modal.deleteConfirm path="/quiz/task/delete/${task.id?c}"-->
        <#--                    id="confirm-delete-task-${task.id}"-->
        <#--                    text="Точно удалить задание?"/>-->
        <#--                </td>-->
        <#--                <td><#if !task.decisions?has_content><div style="color: red">Решений не назначено</div></#if></td>-->
        <#--            </tr>-->
        <#--        <#else>-->
        <#--            <tr>-->
        <#--                <td colspan="4">Список вопросов пока пуст</td>-->
        <#--            </tr>-->
        <#--        </#list>-->
        </tbody>
    </table>

    <script>

        $(async function () {
            await loadContent();
        });

        async function loadContent() {
            let quiz = await getQuiz(${quizId});
            fillQuizHeader(quiz.name);
            await fillQuizChangeForm(quiz);
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

        async function getQuiz(id) {
            return getRequestWithError('/api/quiz/' + id, 'Ошибка при загрузке данных теста');
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
    </script>
</@c.page>
