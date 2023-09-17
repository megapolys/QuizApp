<#import "../../parts/common.ftlh" as c>
<@c.page>
    <a class="btn btn-primary mb-3" href="/user/list">К списку пользователей</a>
    <#if user.lastName?has_content && user.firstName?has_content>
        <div class="card">
            <div class="card-body">
                ${user.lastName!} ${user.firstName!}  ${user.yearBorn?has_content?then(user.yearBorn?c + " г.р.", "")}
            </div>
        </div>
    </#if>
    <h3 class="mb-3">Активные списки</h3>
    <table class="table">
        <thead>
        <tr>
            <th style="width: 10%">Дата завершения</th>
            <th style="width: 10%">Дата последнего обновления</th>
            <th style="width: 10%">Балл</th>
            <th style="width: 10%">Статус</th>
            <th style="width: 10%">Прогресс</th>
            <th style="width: 10%"></th>
            <th style="width: 10%"></th>
        </tr>
        </thead>
        <tbody>
        <#list topicResults as topicResult>
            <tr>
                <td colspan="7"><h3>${topicResult.topic().name}</h3></td>
            </tr>
            <#list topicResult.results() as topic>
                <#assign color=topic.topicResult().completeDate?has_content?then(topic.red()?then("table-danger", topic.yellow()?then("table-warning", "table-success")), "")>
                <tr class="${color}">
                    <td>${topic.topicResult().completeDate?has_content?then(topic.topicResult().completeDate?string, "")}</td>
                    <td>${topic.topicResult().completeDate?has_content?then(topic.topicResult().lastUpdateDate?string, "")}</td>
                    <td>${topic.topicResult().completeDate?has_content?then(topic.score()?string["0.##"], "")}</td>
                    <td>
                        <#if topic.topicResult().completeDate?has_content>
                            <span>Заполнен</span>
                        <#else>
                            <span>В работе</span>
                        </#if>
                    </td>
                    <td>${topic.progress()}</td>
                    <td>
                        <#if topic.topicResult().completeDate?has_content>
                            <a href="/result/topic/${user.id?c}/${topic.topicResult().id?c}">Результаты</a>
                        </#if>
                    </td>
                    <td>
                        <a href="/result/topic/${user.id?c}/delete/${topic.topicResult().id?c}">Удалить</a>
                    </td>
                </tr>
            </#list>
        </#list>
        </tbody>
    </table>

    <h3 class="mb-3">Список анализов</h3>
    <table class="table">
        <thead>
        <tr>
            <th style="width: 60%">Наименование</th>
            <th style="width: 10%">Размер</th>
            <th style="width: 30%">Действие</th>
        </tr>
        </thead>
        <tbody>
        <#list topicList as topic>
            <tr>
                <td>${topic.topic().name}</td>
                <td>${(topic.topic().medicalTasks)?has_content?then(topic.topic().medicalTasks?size?c, "пусто")}</td>
                <td>
                    <#if !topic.exists()>
                        <a href="/result/topic/${user.id?c}/newTopic/${topic.topic().id?c}">Назначить</a>
                    <#elseif topic.inProgress()>
                        <div>Уже назначено</div>
                    <#else>
                        <a href="/result/topic/${user.id?c}/newTopic/${topic.topic().id?c}">Назначить еще раз</a>
                    </#if>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>