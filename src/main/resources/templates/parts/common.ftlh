<#macro page>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Bodymind State</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css"
              integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ"
              crossorigin="anonymous">
        <link rel="stylesheet" href="/static/style.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
        <script src="https://www.google.com/recaptcha/api.js"></script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="/static/decisions.js"></script>
    </head>
    <body>
    <#include "navbar.ftlh">
    <div class="container mt-3">
        <#if message??>
            <div class="alert alert-danger mb-3">${message!}</div>
        </#if>
        <#if successMessage??>
            <div class="alert alert-success mb-3">${successMessage!}</div>
        </#if>
        <#nested>
    </div>
    <footer class="py-3 my-4">
        <ul class="nav justify-content-center border-bottom pb-3 mb-3">
            <li class="nav-item"><a href="/main" class="nav-link px-2 text-muted">Главная</a></li>
            <li class="nav-item"><a href="/userQuizList" class="nav-link px-2 text-muted">Опросники</a></li>
            <li class="nav-item"><a href="/userTopicList" class="nav-link px-2 text-muted">Анализы</a></li>
            <#if isAdmin>
                <li class="nav-item"><a href="/user/list" class="nav-link px-2 text-muted">Пользователи</a></li>
                <li class="nav-item"><a href="/quiz/list" class="nav-link px-2 text-muted">Тесты</a></li>
                <li class="nav-item"><a href="/medical/list" class="nav-link px-2 text-muted">Создать анализ</a></li>
                <li class="nav-item"><a href="/decisions" class="nav-link px-2 text-muted">Решения</a></li>
            </#if>
        </ul>
        <p class="text-center text-muted">Телеграм разработчика для жалоб и предложений
            <a class="icon-link" href="https://t.me/dlosev">
                <i class="bi bi-telegram"></i>
            </a>
        </p>
    </footer>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.7/dist/umd/popper.min.js"
            integrity="sha384-zYPOMqeu1DAVkHiLqWBUTcbYfZ8osu1Nd6Z89ify25QV9guujx43ITvfi12/QExE"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.min.js"
            integrity="sha384-Y4oOpwW3duJdCWv5ly8SCFYWqFDsfob/3GkgExXKV4idmbt98QcxXYs9UoXAB7BZ"
            crossorigin="anonymous"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
            const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));
        });
    </script>
    </body>
    </html>
</#macro>