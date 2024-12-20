<#macro login out>
    <#if out>
        <form action="/logout" method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button class="navbar-text btn btn-outline-secondary" type="submit">Выйти</button>
        </form>
    <#else>
        <a class="navbar-text btn btn-outline-secondary" href="/login">Войти</a>
    </#if>
</#macro>