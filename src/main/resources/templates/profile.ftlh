<#import "parts/common.ftlh" as c>
<@c.page>
    <h3>Профиль</h3>
    <dl class="row mb-3">
        <dt class="col-sm-3">Логин</dt>
        <dd class="col-sm-9">${(user.username)!}</dd>
    </dl>
    <dl class="row mb-3">
        <dt class="col-sm-3">Почта</dt>
        <dd class="col-sm-9">${(user.email)!}</dd>
    </dl>
    <dl class="row mb-3">
        <dt class="col-sm-4"><a class="btn btn-primary" href="/user/edit">Изменить данные профиля</a></dt>
    </dl>
</@c.page>