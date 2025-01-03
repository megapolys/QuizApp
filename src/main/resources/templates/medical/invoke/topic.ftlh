<#import "../../parts/common.ftlh" as c>
<@c.page>
    <h3 id="medical-name" class="mb-3">${topicResult.medicalTopic.name}</h3>
    <div id="medical-topic-result-form">
        <#setting locale="en_US">
        <#list topicResult.results as result>
            <div data-id="${result.medicalTask.name}" class="row mb-1">
                <div class="col-4">
                    <label for="colFormLabel-${result.id?c}" class="col-form-label">${result.medicalTask.name}</label>
                </div>
                <div class="col-2">
                    <input id="colFormLabel-${result.id?c}" name="${result.id?c}" type="number" class="form-control"
                           step="0.01" value="${(result.value?string("0.##"))!}"/>
                </div>
                <#if result.medicalTask.unit?has_content>
                    <div class="col-4">
                        <label class="col-form-label">${result.medicalTask.unit}</label>
                    </div>
                </#if>
            </div>
        </#list>
        <div class="mb-2">
            <button class="btn btn-primary" type="submit">Сохранить</button>
        </div>
    </div>
    <script>

        $(async function () {
            await loadContent();
        });

        async function loadContent() {
            let topicResultId = ${topicResultId};
            let medicalResult = await getMedicalResult(topicResultId);
            $('#medical-name').html(medicalResult.topicName);
            fillMedicalTopicResultTable(medicalResult, topicResultId);
        }

        async function getMedicalResult(topicResultId) {
            return getRequestWithError('/api/medical/invoke/' + topicResultId, 'Ошибка при загрузке топика анализов');
        }

        function fillMedicalTopicResultTable(medicalResult, topicResultId) {
            let form = $('#medical-topic-result-form');
            form.html('');
            medicalResult.results.forEach(result => {
                form.append(`
                <div class="row mb-1">
                    <div class="col-4">
                        <label for="colFormLabel-` + result.id + `" class="col-form-label">` + result.task.name + `</label>
                    </div>
                    <div class="col-2">
                        <input id="colFormLabel-` + result.id + `" name="` + result.id + `" type="number" class="form-control" step="0.01" value="` + (result.value ? round(result.value) : '') + `"/>
                    </div>
                    ` + (result.task.unit ? `
                    <div class="col-4">
                        <label class="col-form-label">` + result.task.unit + `</label>
                    </div>` : '') + `
                </div>
                `);
            })
            form.append(`
            <div class="mb-2">
                <button class="btn btn-primary" onclick="saveTopicResult(` + topicResultId + `)">Сохранить</button>
            </div>
            `);
        }

        async function saveTopicResult(topicResultId) {
            clearMessage();
            let form = $('#medical-topic-result-form');
            let taskMap = new Map();
            form.find("[name]").each(function () {
                let task = $(this);
                taskMap.set(task.attr('name'), task.val());
            })
            let body = {
                topicResultId: topicResultId,
                taskMap: taskMap
            };
            let response = await fetch("/api/medical/invoke", {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    '${_csrf.headerName}': '${_csrf.token}'
                },
                body: JSON.stringify(body)
            });
            if (response.ok) {
                clearFromInvalidFields(form);
                await loadContent();
            } else {
                response.json().then(json => {
                    fillFormInvalidFields(json, form);
                })
            }
        }
    </script>
</@c.page>