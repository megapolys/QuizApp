<#-- @ftlvariable name="user" type="com.example.servingwebcontent.domain.User" -->
<#macro login path>
    <@loginForm path false false false true/>
</#macro>
<#macro registration path>
    <@loginForm path true false false/>
</#macro>
<#macro edit path>
    <@loginForm path false true false/>
</#macro>
<#macro passwordOnly path>
    <@loginForm path false false true/>
</#macro>

<#macro loginForm path registration edit passwordOnly regButton=false>
    <form <#if registration>id="recaptcha-form"</#if> action="${path}" method="post">
        <div class="col-5 mb-3">
            <#if !passwordOnly>
                <div class="mb-3">
                    <label for="inputLogin" class="form-label">Логин</label>
                    <input id="inputLogin" class="form-control" type="text" name="username"
                           aria-describedby="inputLoginHelp"
                           placeholder="логин" value="${(user.username)!}"/>
                </div>
            </#if>
            <div class="mb-3">
                <label for="inputPassword"
                       class="form-label"><#if edit || passwordOnly>Новый пароль<#else>Пароль</#if></label>
                <input type="password" class="form-control" id="inputPassword" name="password" placeholder="пароль">
            </div>
            <#if repairPassword??>
                <div class="mb-3">
                    <a href="/repairPassword">Восстановить пароль</a>
                </div>
            </#if>
            <#if registration || edit || passwordOnly>
                <div class="mb-3">
                    <label for="inputPassword2" class="form-label">Подтверждение пароля</label>
                    <input type="password" class="form-control" id="inputPassword2" name="password2"
                           placeholder="пароль">
                </div>
            </#if>
            <#if registration || edit>
                <div class="mb-3">
                    <label for="lastNameInput" class="form-label">Фамилия</label>
                    <input id="lastNameInput" class="form-control" type="text" name="lastName" placeholder="Фамилия"
                           value="${(user.lastName)!}"/>
                </div>
                <div class="mb-3">
                    <label for="firstNameInput" class="form-label">Имя</label>
                    <input id="firstNameInput" class="form-control" type="text" name="firstName" placeholder="Имя"
                           value="${(user.firstName)!}"/>
                </div>
                <div class="mb-3">
                    <label for="inputEmail" class="form-label">Адрес почты</label>
                    <input id="inputEmail" class="form-control" type="email" name="email" placeholder="email"
                           value="${(user.email)!}"/>
                </div>
                <div class="mb-3">
                    <div class="form-label">Гендер</div>
                    <input class="form-check-input" type="radio" name="male" value="true"
                           id="gender-1" ${((user.male)!false)?string("checked", "")}>
                    <label class="form-check-label" for="gender-1">Муж</label>
                    <input class="form-check-input" type="radio" name="male" value="false"
                           id="gender-2" ${((user.male)!true)?string("", "checked")}>
                    <label class="form-check-label" for="gender-2">Жен</label>
                    <#if registration>
                        <input class="form-check-input" type="radio" name="male" value="" id="gender-3">
                        <label class="form-check-label" for="gender-3">Другое</label>
                    </#if>
                </div>
                <div class="mb-3">
                    <label for="inputYearBorn" class="form-label">Год рождения</label>
                    <input id="inputYearBorn" class="form-control" type="number" min="1900" max="2023" name="yearBorn"
                           value="${(user.yearBorn)!?c}">
                </div>
            </#if>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <#if registration>
                <button class="g-recaptcha btn btn-primary" type="submit"
                        data-sitekey="6LfqyWEoAAAAAIU_4p5uMfgTYVopDoU_ZFC74L5W"
                        data-callback='onSubmit'
                        data-action='submit'>Нажми меня
                </button>
            <#else>
                <button type="submit" class="btn btn-primary">Жмяк</button>
                <#if regButton>
                    <a href="/registration" class="btn btn-primary ms-4">Регистрация</a>
                </#if>
            </#if>
        </div>
    </form>
    <script>
        function onSubmit(token) {
            document.getElementById("recaptcha-form").submit();
        }
    </script>
</#macro>

<#macro logout out>
    <#if out>
        <form action="/logout" method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button class="navbar-text btn btn-outline-secondary" type="submit">Выйти</button>
        </form>
    <#else>
        <a class="navbar-text btn btn-outline-secondary" href="/login">Войти</a>
    </#if>
</#macro>