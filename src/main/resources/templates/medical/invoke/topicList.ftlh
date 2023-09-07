<#import "../../parts/common.ftlh" as c>
<#include "../../parts/security.ftlh">
<@c.page>
    <h3 class="mb-3">Список анализов</h3>
    <table class="table table-striped">
        <thead>
        <tr>
            <th style="width: 30%">Наименование</th>
            <th style="width: 10%">Действие</th>
            <th style="width: 10%">Заполнено</th>
            <th style="width: 10%">Дата завершения</th>
            <th style="width: 10%">Изменение</th>
        </tr>
        </thead>
        <tbody>
        <#list topics as topic>
            <tr>
                <td>${topic.name()}</td>
                <td>
                    <a href="/invokeTopic/${topic.topicResultId()?c}">
                        <#if topic.complete()>
                            Скорректировать
                        <#else>
                            <#if topic.inProgress()>
                                Продолжить заполнение
                            <#else>
                                Заполнить
                            </#if>
                        </#if>
                    </a>
                </td>
                <td>${topic.progress()}</td>
                <td>${topic.complete()?then(topic.completeDate()?string, "")}</td>
                <td>${topic.complete()?then(topic.lastUpdateDate()?string, "")}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>