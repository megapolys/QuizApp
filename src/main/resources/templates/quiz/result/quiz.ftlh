<#import "../../parts/common.ftlh" as c>
<@c.page>
    <div class="row mb-3">
        <div class="col-2">
            <a class="btn btn-primary" href="/quiz/result/${userId?c}">Назад</a>
        </div>
        <div class="col-10">
            <h3 id="quiz-name"></h3>
        </div>
    </div>
    <h4 class="mb-3">Сводная таблица решений</h4>
    <table id="decisions-table" class="table mb-5">
        <thead>
        <tr>
            <th style="width: 60%">Решение</th>
            <th>Баллы</th>
            <th>Баллы/Вопросы</th>
            <th>Вопросы</th>
        </tr>
        </thead>
        <tbody></tbody>
    </table>
    <h4 class="mb-3">Список заданий с текстом</h4>
    <table id="quiz-task-result-table" class="table mb-5">
        <thead>
        <tr>
            <th style="width: 5%">Номер</th>
            <th style="width: 60%">Задание</th>
            <th style="width: 10%">Баллы</th>
            <th style="width: 25%"></th>
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
            let quizResultId = ${quizResultId};
            let quizResultInfo = await getQuizResultInfo(quizResultId);
            $('#quiz-name').html(quizResultInfo.quizName);
            fillDecisionsTable(quizResultInfo.decisions);
            fillTaskTable(quizResultInfo.results, userId, quizResultId);
        }

        function fillDecisionsTable(decisions) {
            let decisionsTable = $('#decisions-table tbody');
            decisionsTable.html('');
            decisions.forEach(decision => {
                decisionsTable.append(`
                <tr>
                    <td>` + decisionWithDescription(decision.decision, decision.decision.name, decision.decision.id) + `</td>
                    <td>` + round(decision.score) + `</td>
                    <td>` + round(decision.score / decision.count) + `</td>
                    <td>` + decision.count + `</td>
                </tr>
                `);
            })
        }

        function fillTaskTable(taskList, userId, quizResultId) {
            let taskTable = $('#quiz-task-result-table tbody');
            taskTable.html('');
            taskList.forEach(task => {
                let colorClass = '';
                if (task.altScore) {
                    colorClass = 'table-success';
                } else if (task.text) {
                    colorClass = 'table-warning';
                }
                taskTable.append(`
                <tr class="` + colorClass + `">
                    <td>` + task.position + `</td>
                    <td>` + task.questionText + `</td>
                    <td>` + round(task.resultScore) + `</td>
                    <td><a href="/quiz/result/` + userId + `/` + quizResultId + `/` + task.id + `">Открыть</a></td>
                </tr>
                `);
            })
        }

        async function getQuizResultInfo(quizResultId) {
            return getRequestWithError('/api/quiz/result/' + quizResultId, 'Ошибка при загрузки данных результата теста');
        }
    </script>
</@c.page>