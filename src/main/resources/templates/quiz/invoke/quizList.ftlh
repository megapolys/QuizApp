<#import "../../parts/common.ftlh" as c>
<@c.page>
    <h3 class="mb-3">Список назначенных опросников</h3>
    <table id="quiz-invoke-table" class="table">
        <thead>
        <tr>
            <th style="width: 60%"></th>
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
            let quizResultList = await getQuizResultList();
            fillQuizResultTable(quizResultList);
        }

        function fillQuizResultTable(quizResultList) {
            let table = $('#quiz-invoke-table tbody');
            table.html('');
            quizResultList.forEach(quizResult => {
                table.append(`
                <tr>
                    <td colspan="3"><h5>` + quizResult.quiz.shortName + `. ` + quizResult.quiz.name + `</h5></td>
                </tr>
                `);
                quizResult.results.forEach(result => {
                    table.append(`
                    <tr>
                        <td></td>
                        <td>` + (result.complete ? "Тест пройден" : '<a href="/quiz/invoke/' + result.id + '">' + (result.countCompleted > 0 ? "Продолжить " + result.countCompleted + "/" + result.taskCount : "Начать") + '</a>') + `</td>
                        <td>` + (result.complete ? result.completeDate : '') + `</td>
                    </tr>
                    `);
                })
            })
        }

        async function getQuizResultList() {
            return getRequestWithError('/api/quiz/invoke', 'Ошибка при загрузки списка тестов');
        }
    </script>
</@c.page>