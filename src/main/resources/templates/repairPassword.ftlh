<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>
<@c.page>
    <h3 class="mb-3">Восстановление пароля</h3>
    <#if validCode??>
        <@l.passwordOnly "/repairPassword/${validCode}"/>
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