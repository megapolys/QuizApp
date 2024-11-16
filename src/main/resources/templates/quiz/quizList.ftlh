<#import "../parts/common.ftlh" as c>
<#import "../parts/modal.ftlh" as modal>
<@c.page>
    <h3 class="mb-3">Список тестов</h3>
    <div id="quiz-form" class="mb-3"></div>
    <table class="table table-striped">
        <thead>
        <tr>
            <th style="width: 5%">Индекс</th>
            <th style="width: 65%">Наименование</th>
            <th style="width: 10%">Размер</th>
            <th style="width: 10%"></th>
            <th style="width: 10%"></th>
        </tr>
        </thead>
        <tbody>
        <#list quizList as quiz>
            <tr>
                <td>${quiz.shortName}</td>
                <td>${quiz.name}</td>
                <td>
                    <#if quiz.size == 0>
                        пусто
                    <#else>
                        ${quiz.size}
                    </#if>
                </td>
                <td><a href="/quiz/${quiz.id?c}">Редактировать</a></td>
                <td>
                    <@modal.deleteConfirm path="/quiz/delete/${quiz.id?c}" id="confirm-delete-quiz-${quiz.id}"
                    text="Вы точно уверены, что хотите удалить опросник? Это необратимая операция, которая приведет к тому же к удалению всех результатов решения данного опросника."/>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>

    <script>
        $(async function () {
            await loadContent();
        });


        async function loadContent() {
            fillQuizForm();
        }

        function fillQuizForm() {
            $('#quiz-form').html(`
                <div class="row">
                    <div class="col-2">
                        <input class="form-control" type="text" name="shortName" placeholder="Краткое наименование"/>
                    </div>
                    <div class="col-8">
                        <input class="form-control" type="text" name="name" placeholder="Наименование теста"/>
                    </div>
                    <div class="col-2">
                        <button class="btn btn-primary" onclick="addQuiz()">Добавить</button>
                    </div>
                </div>
            `);
        }

        function addQuiz() {
            let form = $('#quiz-form');
            let quiz = {
                shortName: form.find("[name|='shortName']").val(),
                name: form.find("[name|='name']").val()
            };
            let response = await fetch("/api/quiz", {
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
                    fillInvalidFields(json, form);
                })
            }
        }
    </script>
</@c.page>