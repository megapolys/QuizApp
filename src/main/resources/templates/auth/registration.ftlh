<#import "../parts/common.ftlh" as c>
<@c.page>
    <h3>Регистрация</h3>
    <form action="/registration" method="post">
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
                <label for="inputPassword2" class="form-label">Подтверждение пароля</label>
                <input type="password" class="form-control" id="inputPassword2" name="password2"
                       placeholder="пароль">
            </div>
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
            </div>
            <div class="mb-3">
                <label for="inputBirthday" class="form-label">Дата рождения</label>
                <input id="inputBirthday" class="form-control" type="date" name="birthday" value="${(user.birthday)!}">
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button class="g-recaptcha btn btn-primary" type="submit"
                    data-sitekey="6LfqyWEoAAAAAIU_4p5uMfgTYVopDoU_ZFC74L5W"
                    data-callback='onSubmit'
                    data-action='submit'>Нажми меня
            </button>
        </div>
    </form>
    <script>
        function onSubmit(token) {
            document.getElementById("recaptcha-form").submit();
        }
    </script>
</@c.page>