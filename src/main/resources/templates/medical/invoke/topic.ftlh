<#import "../../parts/common.ftlh" as c>
<@c.page>
    <div class="row mb-3">
        <div class="col-2">
            <a class="btn btn-primary" href="/medical/invoke">Назад</a>
        </div>
        <div class="col-10">
            <h3 id="medical-name" class="mb-3"></h3>
        </div>
    </div>
    <div id="medical-topic-result-form"></div>
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
            let taskList = [];
            form.find("[name]").each(function () {
                let task = $(this);
                taskList.push({"taskResultId": task.attr('name'), "value": task.val()});
            })
            let body = {
                topicResultId: topicResultId,
                results: taskList
            };
            console.log(body);
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