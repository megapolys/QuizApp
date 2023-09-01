<#include "security.ftlh">
<#import "login.ftlh" as l>

<nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container">
        <a class="navbar-brand" href="/">Health-3800</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link ${mainTab!}" href="/main">Главная</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${invokeQuizTab!}" href="/userQuizList">Опросники</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${invokeTopicTab!}" href="/userTopicList">Анализы</a>
                </li>
                <#if isAdmin>
                    <li class="nav-item">
                        <a class="nav-link ${usersTab!}" href="/user/list">Пользователи</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${quizTab!}" href="/quiz/list">Тесты</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${medicalAdminTab!}" href="/medical/list">Мед токены</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${decisionTab!}" href="/decisions">Решения</a>
                    </li>
                </#if>
            </ul>
            <a href="/user" class="navbar-text btn btn-outline-secondary me-3">${name}</a>
            <#if user??>
                <@l.logout true/>
            <#else>
                <@l.logout false/>
            </#if>
        </div>
    </div>
</nav>
