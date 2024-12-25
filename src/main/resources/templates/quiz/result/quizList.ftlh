<#import "../../parts/common.ftlh" as c>
<#import "../../parts/modal.ftlh" as modal>
<@c.page>
    <a class="btn btn-primary mb-3" href="/user/list">К списку пользователей</a>
    <div id="user-card" class="card">
        <div class="card-body">
        </div>
    </div>
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
        <tbody>
        <#list quizResults as quizResult>
            <tr>
                <td colspan="5"><h3>${quizResult.quiz().shortName}. ${quizResult.quiz().name}</h3></td>
            </tr>
            <#list quizResult.results() as quiz>
                <#assign color=quiz.quizResult().complete?then(quiz.red()?then("table-danger", quiz.yellow()?then("table-warning", "table-success")), "")>
                <tr class="${color}">
                    <td>${quiz.quizResult().complete?then(quiz.quizResult().completeDate?string, "")}</td>
                    <td>${quiz.quizResult().complete?then(quiz.score()?string["0.##"], "")}</td>
                    <td>
                        <#if quiz.quizResult().complete>
                            <span>Пройден</span>
                        <#else>
                            <span>В работе ${quiz.progress()}</span>
                        </#if>
                    </td>
                    <td>
                        <#if quiz.quizResult().complete>
                            <a href="/result/quiz/${user.id?c}/${quiz.quizResult().id?c}">Результаты</a>
                        </#if>
                    </td>
                    <td>
                        <@modal.deleteConfirm path="/result/quiz/${user.id?c}/delete/${quiz.quizResult().id?c}"
                        id="confirm-delete-quizResult-${quiz.quizResult().id}"
                        text="Точно удалить результаты теста?"/>
                    </td>
                </tr>
            </#list>
        </#list>
        </tbody>
    </table>

    <h3 class="mb-3">Список опросников</h3>
    <table class="table">
        <thead>
        <tr>
            <th style="width: 60%">Наименование</th>
            <th style="width: 10%">Размер</th>
            <th style="width: 30%">Действие</th>
        </tr>
        </thead>
        <tbody>
        <#list quizList as quiz>
            <tr>
                <td>${quiz.quiz().shortName}. ${quiz.quiz().name}</td>
                <td>${(quiz.quiz().taskList)?has_content?then(quiz.quiz().taskList?size?c, "пусто")}</td>
                <td>
                    <#if !quiz.exists()>
                        <@modal.postButton "/result/quiz/${user.id?c}/newQuiz/${quiz.quiz().id?c}" "Назначить"/>
                    <#elseif quiz.inProgress()>
                        <div>Уже назначено</div>
                    <#else>
                        <@modal.postButton "/result/quiz/${user.id?c}/newQuiz/${quiz.quiz().id?c}" "Назначить еще раз"/>
                    </#if>
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
            let userId = ${userId};
            let user = await getUserById(userId);
            fillUserCard(user);
            let quizResultList = await getQuizResultList(userId);
            let colorRatioProperty = await getColorRatioProperty();
            fillQuizResultTable(quizResultList, colorRatioProperty);
        }

        function fillUserCard(user) {
            $('#user-card .card-body').html(user.lastName + user.firstName + user.birthday);
        }

        function fillQuizResultTable(quizResultList, colorRatioProperty) {
            let table = $('#quiz-result-table tbody');
            table.html('');
            quizResultList.forEach(quizResult => {
                table.append(`
                <tr>
                    <td colspan="5"><h3>` + quizResult.quiz.shortName + `. ` + quizResult.quiz.name + `</h3></td>
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
                        <td>
                            <@modal.deleteConfirm path="/result/quiz/${user.id?c}/delete/${quiz.quizResult().id?c}"
                            id="confirm-delete-quizResult-${quiz.quizResult().id}"
                            text="Точно удалить результаты теста?"/>
                        </td>
                    </tr>
                    `);
                })
            })
        }

        async function getUserById(userId) {
            return getRequestWithError('/api/user/' + userId, 'Ошибка при загрузки данных пользователя');
        }

        async function getQuizResultList(userId) {
            return getRequestWithError('/api/quiz/result?userId=' + userId, 'Ошибка при загрузки списка результатов тестов');
        }

        async function getColorRatioProperty() {
            return getRequestWithError('api/property/color-ratio', 'Ошибка при загрузки настроек цветов');
        }

    </script>
</@c.page>