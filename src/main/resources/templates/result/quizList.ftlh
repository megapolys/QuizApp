<#import "../parts/common.ftlh" as c>
<@c.page>
    <a class="btn btn-primary mb-3" href="/user/list">К списку пользователей</a>
    <#if user.lastName?has_content && user.firstName?has_content>
        <div class="card">
            <div class="card-body">
                ${user.lastName!} ${user.firstName!}  ${user.yearBorn?has_content?then(user.yearBorn?c + " г.р.", "")}
            </div>
        </div>
    </#if>
    <h3 class="mb-3">Активные опросники</h3>
    <table class="table">
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
                <td colspan="5">${quizResult.quiz().shortName}. ${quizResult.quiz().name}</td>
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
                        <a href="/result/quiz/${user.id?c}/delete/${quiz.quizResult().id?c}">Удалить</a>
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
            <th style="width: 60%"></th>
            <th style="width: 40%"></th>
        </tr>
        </thead>
        <tbody>
        <#list quizList as quiz>
            <tr>
                <td>${quiz.quiz().shortName}. ${quiz.quiz().name}</td>
                <td>
                    <#if !quiz.exists()>
                        <a href="/result/quiz/${user.id?c}/newQuiz/${quiz.quiz().id?c}">Назначить</a>
                    <#elseif quiz.inProgress()>
                        <div>Уже назначено</div>
                    <#else>
                        <a href="/result/quiz/${user.id?c}/newQuiz/${quiz.quiz().id?c}">Назначить еще раз</a>
                    </#if>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>