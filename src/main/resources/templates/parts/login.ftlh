<#macro login path registration>
    <form action="${path}" method="post">
        <div class="mb-3 col-3">
            <label for="inputLogin" class="form-label">Логин</label>
            <input id="inputLogin" class="form-control" type="text" name="username" aria-describedby="inputLoginHelp"
                   placeholder="логин" value="${(user.username)!}"/>
            <#if !registration>
                <div id="inputLoginHelp" class="form-text">Можно почту вводить, тоже норм.</div>
            </#if>
        </div>
        <div class="mb-3 col-3">
            <label for="inputPassword" class="form-label">Пароль</label>
            <input type="password" class="form-control" id="inputPassword" name="password" placeholder="пароль" value="${(user.password)!}">
        </div>
        <#if registration>
            <div class="mb-3 col-3">
                <label for="inputEmail" class="form-label">Адрес почты</label>
                <input id="inputEmail" class="form-control" type="email" name="email" placeholder="email" value="${(user.email)!}"/>
            </div>
        </#if>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit"
                class="btn btn-primary">${registration?string("Нажми меня","Жмяк")}</button>
    </form>
</#macro>

<#macro logout out>
    <#if out>
        <form action="/logout" method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button class="btn btn-primary" type="submit">Выйти</button>
        </form>
    <#else>
        <a class="btn btn-primary" href="/login">Войти</a>
    </#if>
</#macro>