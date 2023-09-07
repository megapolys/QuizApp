<#import "../../parts/common.ftlh" as c>
<#include "../../parts/security.ftlh">
<@c.page>
    <h3 class="mb-3">${topicResult.medicalTopic.name}</h3>
    <form action="/completeTopic/${topicResult.id?c}" method="post">
        <#setting locale="en_US">
        <#list topicResult.results as result>
            <div class="row mb-1">
                <div class="col-4">
                    <label for="colFormLabel-${result.id?c}" class="col-form-label">${result.medicalTask.name}</label>
                </div>
                <div class="col-2">
                    <input id="colFormLabel-${result.id?c}" name="${result.id?c}" type="number" class="form-control" step="0.01" value="${(result.value?string("0.##"))!}">
                </div>
                <div class="col-1">
                    <label class="col-form-label">${result.medicalTask.unit}</label>
                </div>
            </div>
        </#list>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <div class="mb-2">
            <button class="btn btn-primary" type="submit">Сохранить</button>
        </div>
    </form>
</@c.page>