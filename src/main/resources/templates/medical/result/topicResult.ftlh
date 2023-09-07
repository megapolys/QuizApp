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
    <@d.resultDecisionsTable result.decisions()!/>
    <h4 class="mb-3">Список заданий с текстом</h4>
    <table class="table mb-5">
        <thead>
        <tr>
            <th style="width: 40%">Название анализа</th>
            <th style="width: 5%">Значение</th>
            <th style="width: 15%"></th>
            <th style="width: 20%">Балл</th>
            <th style="width: 20%">Изменение балла</th>
        </tr>
        </thead>
        <tbody>
        <#setting locale="en_US">
        <#list result.tasks() as task>
            <#if task.value??>
                <#assign color=task.altScore()?has_content?then("table-success", "")>
                <tr class="${color}">
                    <td>${task.name()}</td>
                    <td>${task.value()?string["0.##"]} ${task.unit()!}</td>
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
                        <form action="/result/topic/updateTaskResult/${userId}/${result.topicResult().id?c}/${task.taskResultId()?c}"
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