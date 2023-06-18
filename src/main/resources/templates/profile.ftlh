<#import "parts/common.ftlh" as c>
<@c.page>
    <h3 class="mb-3">Профиль</h3>
    <dl class="row mb-3">
        <dt class="col-sm-3">Логин</dt>
        <dd class="col-sm-9">${(user.username)!}</dd>
    </dl>
    <dl class="row mb-3">
        <dt class="col-sm-3">Почта</dt>
        <dd class="col-sm-9">${(user.email)!}</dd>
    </dl>
    <dl class="row mb-3">
        <dt class="col-sm-3">Фамилия</dt>
        <dd class="col-sm-9">${(user.lastName)!}</dd>
    </dl>
    <dl class="row mb-3">
        <dt class="col-sm-3">Имя</dt>
        <dd class="col-sm-9">${(user.firstName)!}</dd>
    </dl>
    <dl class="row mb-3">
        <dt class="col-sm-3">Год рождения</dt>
        <dd class="col-sm-9">${(user.yearBorn)!?c}</dd>
    </dl>
    <dl class="row mb-3">
        <dt class="col-sm-4"><a class="btn btn-primary" href="/user/edit">Изменить данные профиля</a></dt>
    </dl>
</@c.page>