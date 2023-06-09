<#import "parts/common.ftlh" as c>
<@c.page>
    <h3>Изменение пользователя ${user.username}</h3>
    <form action="/user" method="post">
        <input type="text" name="username" value="${user.username}">
        <input type="email" name="email" value="${user.email}">
        <#list roles as role>
            <div>
                <label><input type="checkbox" name="roles"
                              value="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>${role}</label>
            </div>
        </#list>
        <#list quizzes as quiz>
            <div class="form-check">
                <input class="form-check-input" type="checkbox" name="quizzes" value="${quiz.id}"
                       id="quiz-${quiz.id}" ${user.quizzes?seq_contains(quiz)?string("checked", "")}>
                <label class="form-check-label" for="quiz-${quiz.id}">
                    ${quiz.shortName}
                </label>
            </div>
        </#list>
        <input type="hidden" value="${user.id}" name="userId">
        <input type="hidden" value="${_csrf.token}" name="_csrf">
        <button type="submit">Сохранить</button>
    </form>
</@c.page>