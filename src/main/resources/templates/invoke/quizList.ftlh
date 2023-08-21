<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">
<@c.page>
    <h3 class="mb-3">Список опросников</h3>
    <table class="table table-striped">
        <thead>
        <tr>
            <th style="width: 60%"></th>
            <th style="width: 20%"></th>
            <th style="width: 20%"></th>
        </tr>
        </thead>
        <tbody>
        <#list quizzes as quiz>
            <tr>
                <td>${quiz.name()}</td>
                <td>
                    <#if quiz.complete()>
                        <span>Тест пройден</span>
                    <#else>
                        <a href="/invokeQuiz/${quiz.quizId()?c}">
                            <#if quiz.inProgress()>
                                Продолжить ${quiz.progress()}
                            <#else>
                                Начать
                            </#if>
                        </a>
                    </#if>
                </td>
                <td>${quiz.complete()?then(quiz.completeDate()?string, "")}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>