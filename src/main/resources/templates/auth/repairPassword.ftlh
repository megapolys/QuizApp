<#import "../parts/common.ftlh" as c>
<@c.page>
    <h3 class="mb-3">Восстановление пароля</h3>
    <#if validCode??>
        <form action="/repairPassword/${validCode}" method="post">
            <div class="col-5 mb-3">
                <div class="mb-3">
                    <label for="inputPassword" class="form-label">Новый пароль</label>
                    <input type="password" class="form-control" id="inputPassword" name="password" placeholder="пароль">
                </div>
                <div class="mb-3">
                    <label for="inputPassword2" class="form-label">Подтверждение пароля</label>
                    <input type="password" class="form-control" id="inputPassword2" name="password2"
                           placeholder="пароль">
                </div>
                <button type="submit" class="btn btn-primary">Жмяк</button>

            </div>
        </form>
    <#else>
        <form action="repairPassword" method="post">
            <div class="col-3 mb-3">
                <label for="inputMail" class="form-label">Email</label>
                <input id="inputMail" class="form-control" type="email" name="email">
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-primary">Отправить</button>
        </form>
    </#if>
</@c.page>