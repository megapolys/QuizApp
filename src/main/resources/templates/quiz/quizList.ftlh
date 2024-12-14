<#import "../parts/common.ftlh" as c>
<@c.page>
    <h3 class="mb-3">Список тестов</h3>
    <div id="quiz-form" class="mb-3"></div>
    <table id="quiz-table" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 5%">Краткое наименование</th>
            <th style="width: 65%">Наименование</th>
            <th style="width: 10%">Размер</th>
            <th style="width: 10%"></th>
            <th style="width: 10%"></th>
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
            fillQuizForm();
            await fillQuizListTable();
        }

        function fillQuizForm() {
            $('#quiz-form').html(`
                <div class="row">
                    <div class="col-2">
                        <input class="form-control" type="text" name="shortName" placeholder="Краткое наименование" aria-describedby="validationQuizFormShortName"/>
                        <div id="validationQuizFormShortName" class="invalid-feedback"></div>
                    </div>
                    <div class="col-8">
                        <input class="form-control" type="text" name="name" placeholder="Наименование теста" aria-describedby="validationQuizFormName"/>
                        <div id="validationQuizFormName" class="invalid-feedback"></div>
                    </div>
                    <div class="col-2">
                        <button class="btn btn-primary" onclick="addQuiz()">Добавить</button>
                    </div>
                </div>
            `);
        }

        async function fillQuizListTable() {
            let quizList = await getQuizList();
            let quizListBody = $('#quiz-table tbody');
            quizListBody.html('');
            quizList.forEach(quiz => {
                quizListBody.append(`
                    <tr>
                        <td>` + quiz.shortName + `</td>
                        <td>` + quiz.name + `</td>
                        <td>` + (quiz.size === 0 ? 'пусто' : quiz.size) + `</td>
                        <td><a href="/quiz/` + quiz.id + `">Открыть</a></td>
                        <td>` + deleteConfirmString('deleteQuiz(' + quiz.id + ')', 'confirm-delete-quiz-' + quiz.id, 'Вы точно уверены, что хотите удалить опросник? Это необратимая операция, которая приведет к тому же к удалению всех результатов решения данного опросника.') + `</td>
                    </tr>
                `);
            })
        }

        async function addQuiz() {
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
                body: JSON.stringify(quiz)
            });
            if (response.ok) {
                showSuccess('Тест ' + quiz.name + ' успешно добавлен');
                await loadContent();
            } else {
                response.json().then(json => {
                    fillFormInvalidFields(json, form);
                })
            }
        }

        async function deleteQuiz(id) {
            await deleteByPath('/api/quiz/' + id, 'Тест успешно удален');
            await loadContent();
        }

        async function getQuizList() {
            return getRequestWithError('/api/quiz', 'Ошибка при загрузки списка тестов');
        }
    </script>
</@c.page>