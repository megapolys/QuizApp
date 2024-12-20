<#import "../parts/common.ftlh" as c>
<@c.page>
    <h3 class="mb-3">Авторизация</h3>
    <form action="/login" method="post">
        <div class="col-5 mb-3">
            <div class="mb-3">
                <label for="inputLogin" class="form-label">Логин</label>
                <input id="inputLogin" class="form-control" type="text" name="username"
                       aria-describedby="inputLoginHelp"
                       placeholder="логин" value="${(user.username)!}"/>
            </div>
            <div class="mb-3">
                <label for="inputPassword"
                       class="form-label">Пароль</label>
                <input type="password" class="form-control" id="inputPassword" name="password" placeholder="пароль">
            </div>
            <div class="mb-3">
                <input type="checkbox" class="form-check-input" id="remember-me" name="remember-me"/>
                <label for="remember-me" class="form-check-label">Запомнить меня</label>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-primary">Жмяк</button>
        </div>
    </form>
</@c.page>