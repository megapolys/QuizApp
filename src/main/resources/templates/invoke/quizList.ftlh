<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">
<@c.page>
    <h3 class="mb-3">Список опросников</h3>
    <table class="table table-striped">
        <thead>
        <tr>
            <th style="width: 60%"></th>
            <th style="width: 40%"></th>
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
                        <#if quiz.inProgress()>
                            <a href="/invokeQuiz/${quiz.quizId()?c}">Продолжить ${quiz.progress()}</a>
                        <#else>
                            <a href="/startQuiz/${quiz.quizId()?c}">Начать</a>
                        </#if>
                    </#if>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>