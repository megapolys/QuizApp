<#import "../../parts/common.ftlh" as c>
<#import "../../parts/decisions.ftlh" as d>
<@c.page>
    <div class="row mb-3">
        <div class="col-2">
            <a class="btn btn-primary" href="/result/topic/${userId?c}">Назад</a>
        </div>
        <div class="col-10">
            <h3>${result.topicResult().medicalTopic.name}</h3>
        </div>
    </div>
    <h4 class="mb-3">Сводная таблица решений</h4>
    <table class="table mb-5">
        <thead>
        <tr>
            <th style="width: 30%">Группа</th>
            <th style="width: 50%">Решение</th>
            <th style="width: 5%">Баллы</th>
            <th style="width: 10%">Баллы/Вопросы</th>
            <th style="width: 5%">Вопросы</th>
        </tr>
        </thead>
        <tbody>
        <#list result.decisions() as decision>
            <tr>
                <td>${(decision.decision().group.name)!}</td>
                <td><@d.decisionWithDescription decision=decision.decision() name=decision.decision().name/></td>
                <td>${decision.score()?string["0.##"]}</td>
                <td>${decision.altScore()?string["0.##"]}</td>
                <td>${decision.count()?c}</td>
            </tr>
        </#list>
        </tbody>
    </table>
    <h4 class="mb-3">Список заполненных анализов</h4>
    <table class="table mb-5">
        <thead>
        <tr>
            <th style="width: 40%">Название анализа</th>
            <th style="width: 10%">Решения</th>
            <th style="width: 5%">Левый оптимум</th>
            <th style="width: 5%">Правый оптимум</th>
            <th style="width: 5%">Значение</th>
            <th style="width: 20%"></th>
            <th style="width: 5%">Балл</th>
            <th style="width: 10%">Изменение балла</th>
        </tr>
        </thead>
        <tbody>
        <#setting locale="en_US">
        <#list result.tasks() as task>
            <#if task.taskResult().value??>
                <#assign color=task.taskResult().altScore?has_content?then("table-success", "")>
                <tr class="${color}">
                    <td>${(task.taskResult().medicalTask.name)?truncate(50, "...") + task.taskResult().medicalTask.unit?has_content?then(" - " + task.taskResult().medicalTask.unit, "")}</td>
                    <td>
                        <a href="##" data-bs-toggle="modal" data-bs-target="#medical-task-${task.taskResult().medicalTask.id}" >
                            Посмотреть
                        </a>
                        <div class="modal fade" id="medical-task-${task.taskResult().medicalTask.id}" tabindex="-1" aria-hidden="true">
                            <div class="modal-xl modal-fullscreen-lg-down modal-dialog modal-dialog-centered modal-dialog-scrollable">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h2>${task.taskResult().medicalTask.name}</h2>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <table class="table table-striped">
                                            <thead>
                                            <tr>
                                                <th style="width: 50%">Название решения</th>
                                                <th style="width: 50%">Описание решения</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <#list task.decisions() as decision>
                                                <tr>
                                                    <td>${decision.group?has_content?then(decision.group.name + " _ ", "") + decision.name}</td>
                                                    <td>${decision.description!}</td>
                                                </tr>
                                            </#list>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </td>
                    <td>${task.taskResult().medicalTask.leftMid?string["0.##"]}</td>
                    <td>${task.taskResult().medicalTask.rightMid?string["0.##"]}</td>
                    <td>${task.taskResult().value?string["0.##"]}</td>
                    <td>
                        <div class="progress-stacked">
                            <div class="progress" role="progressbar"
                                 style="width: ${task.analyseForm().values()[0]}%">
                                <div class="progress-bar bg-danger"></div>
                            </div>
                            <div class="progress" role="progressbar"
                                 style="width: ${task.analyseForm().values()[1]}%">
                                <div class="progress-bar bg-warning"></div>
                            </div>
                            <div class="progress" role="progressbar"
                                 style="width: ${task.analyseForm().values()[2]}%">
                                <div class="progress-bar bg-success"></div>
                            </div>
                            <div class="progress" role="progressbar"
                                 style="width: ${task.analyseForm().values()[3]}%">
                                <div class="progress-bar bg-warning"></div>
                            </div>
                            <div class="progress" role="progressbar"
                                 style="width: ${task.analyseForm().values()[4]}%">
                                <div class="progress-bar bg-danger"></div>
                            </div>
                        </div>
                        <div class="fs-3" style="transform: translate(${task.analyseForm().marker() - 1}%, -72%)">I</div>
                    </td>
                    <td>${task.resultScore()}</td>
                    <td>
                        <form action="/result/topic/updateTaskResult/${userId}/${result.topicResult().id?c}/${task.taskResult().id?c}"
                              method="post">
                            <div class="input-group">
                                <input type="number" name="altScore" class="form-control" step="0.5"/>
                                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                                <button class="btn btn-outline-secondary" type="submit">Изменить</button>
                            </div>
                        </form>
                    </td>
                </tr>
            </#if>
        </#list>
        </tbody>
    </table>
</@c.page>