<#import "parts/common.ftlh" as c>
<@c.page>
    <h3>Изменение пользователя ${user.username}</h3>
    <form action="/user" method="post">
        <div class="row mb-3">
            <div class="col-6">
                <h5>Роли</h5>
                <#list roles as role>
                    <div class="form-check">
                        <input class="form-check-input" name="roles" type="checkbox"
                               value="${role}" ${user.roles?seq_contains(role)?string("checked", "")}
                               id="role-${role.name()?esc}">
                        <label class="form-check-label" for="role-${role.name()?esc}">${role}</label>
                    </div>
                </#list>
            </div>
            <div class="col-6">
                <h5>Опросники</h5>
                <#list quizzes as quiz>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="quizzes" value="${quiz.id}"
                               id="quiz-${quiz.id?esc}" ${user.quizzes?seq_contains(quiz)?string("checked", "")}>
                        <label class="form-check-label" for="quiz-${quiz.id?esc}">${quiz.shortName}</label>
                    </div>
                </#list>
            </div>
        </div>
        <input type="hidden" value="${user.id}" name="userId">
        <input type="hidden" value="${_csrf.token}" name="_csrf">
        <button class="btn btn-primary" type="submit">Сохранить</button>
    </form>
</@c.page>