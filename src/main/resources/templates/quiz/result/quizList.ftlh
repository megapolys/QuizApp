<#import "../../parts/common.ftlh" as c>
<@c.page>
    <a class="btn btn-primary mb-3" href="/user/list">К списку пользователей</a>
    <h2 id="user-card" class="mb-3"></h2>
    <h3 class="mb-3">Активные опросники</h3>
    <table id="quiz-result-table" class="table">
        <thead>
        <tr>
            <th style="width: 10%">Дата</th>
            <th style="width: 10%">Балл</th>
            <th style="width: 10%">Статус</th>
            <th style="width: 10%"></th>
            <th style="width: 10%"></th>
        </tr>
        </thead>
        <tbody></tbody>
    </table>

    <h3 class="mb-3">Список опросников</h3>
    <table id="quiz-table" class="table">
        <thead>
        <tr>
            <th style="width: 60%">Наименование</th>
            <th style="width: 10%">Размер</th>
            <th style="width: 30%">Действие</th>
        </tr>
        </thead>
        <tbody></tbody>
    </table>

    <script>

        $(async function () {
            await loadContent();
        });

        async function loadContent() {
            let userId = ${userId};
            let user = await getUserById(userId);
            fillUserCard(user);
            let quizResultList = await getQuizResultList(userId);
            let colorRatioProperty = await getColorRatioProperty();
            fillQuizResultTable(quizResultList, colorRatioProperty);
            let quizList = await getQuizList();
            fillQuizTable(quizList, quizResultList, userId);
        }

        function fillUserCard(user) {
            $('#user-card').html(user.lastName + ' ' + user.firstName + ' ' + user.birthday);
        }

        function fillQuizResultTable(quizResultList, colorRatioProperty) {
            let table = $('#quiz-result-table tbody');
            table.html('');
            quizResultList.forEach(quizResult => {
                table.append(`
                <tr>
                    <td colspan="5"><h5>` + quizResult.quiz.shortName + `. ` + quizResult.quiz.name + `</h5></td>
                </tr>
                `);
                quizResult.results.forEach(result => {
                    let colorClass = "";
                    if (result.complete) {
                        if (result.score > colorRatioProperty.red) {
                            colorClass = "table-danger";
                        } else if (result.score > colorRatioProperty.yellow) {
                            colorClass = "table-warning";
                        } else {
                            colorClass = "table-success";
                        }
                    }
                    table.append(`
                    <tr class="` + colorClass + `">
                        <td>` + (result.complete ? result.completeDate : "") + `</td>
                        <td>` + (result.complete ? result.score : "") + `</td>
                        <td>` + (result.complete ? "Пройден" : "В работе " + result.countCompleted + "/" + result.taskCount) + `</td>
                        <td>` + (result.complete ? '<a href="/quiz/result/"' + result.id + '">Результаты</a>' : '') + `</td>
                        <td>` + deleteConfirmString('deleteQuizResult(' + result.id + ')', 'confirm-delete-quizResult-' + result.id, 'Точно удалить результаты теста?') + `</td>
                    </tr>
                    `);
                })
            })
        }

        function fillQuizTable(quizList, quizResultList, userId) {
            let assignedQuizSet = new Set();
            let completedQuizSet = new Set();
            quizResultList.forEach(quizResult => {
                assignedQuizSet.add(quizResult.quiz.id);
                let allCompleted = true;
                quizResult.results.forEach(result => {
                    if (!result.complete) {
                        allCompleted = false;
                    }
                })
                if (allCompleted) {
                    completedQuizSet.add(quizResult.quiz.id);
                }
            })
            let table = $('#quiz-table tbody');
            table.html('');
            quizList.forEach(quiz => {
                table.append(`
                <tr>
                    <td>` + quiz.shortName + '. ' + quiz.name + `</td>
                    <td>` + (quiz.size > 0 ? '' : 'пусто') + `</td>
                    <td>` + (completedQuizSet.has(quiz.id) ?
                    '<button class="btn btn-link p-0 mb-1" onclick="createQuizResult(' + userId + ', ' + quiz.id + ')">Назначить еще раз</button>'
                    : (assignedQuizSet.has(quiz.id) ? 'Уже назначено' : '<button class="btn btn-link p-0 mb-1" onclick="createQuizResult(' + userId + ', ' + quiz.id + ')">Назначить</button>')) + `
                    </td>
                </tr>
                `);
            })
        }

        async function getUserById(userId) {
            return getRequestWithError('/api/user/' + userId, 'Ошибка при загрузки данных пользователя');
        }

        async function getQuizList() {
            return getRequestWithError('/api/quiz', 'Ошибка при загрузки списка тестов');
        }

        async function getQuizResultList(userId) {
            return getRequestWithError('/api/quiz/result?userId=' + userId, 'Ошибка при загрузки списка результатов тестов');
        }

        async function getColorRatioProperty() {
            return getRequestWithError('/api/property/color-ratio', 'Ошибка при загрузки настроек цветов');
        }

        async function deleteQuizResult(id) {
            await deleteByPath('/api/quiz/result/' + id, 'Результат теста успешно удален');
            await loadContent();
        }

        async function createQuizResult(userId, quizId) {
            let response = await fetch('/api/quiz/result?userId=' + userId + '&quizId=' + quizId, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    '${_csrf.headerName}': '${_csrf.token}'
                }
            });
            if (response.ok) {
                showSuccess('Тест успешно назначен пользователю');
                await loadContent();
            } else {
                showError('Ошибка при попытке назначить тест пользователю');
            }
        }

    </script>
</@c.page>