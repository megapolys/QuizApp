<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">
<@c.page>
    <h3>Доступные тесты</h3>
    <#list quizzes as quiz>
        <div class="row">
            <div class="col-6">${quiz.name()}</div>
            <div class="col-2">
                <#if quiz.complete()>
                    <span>Тест пройден</span>
                <#else>
                    <#if quiz.inProgress()>
                        <a href="/invokeQuiz/${quiz.quizId()?c}">Продолжить</a>
                    <#else>
                        <a href="/startQuiz/${quiz.quizId()?c}">Начать</a>
                    </#if>
                </#if>
            </div>
        </div>
    </#list>
</@c.page>