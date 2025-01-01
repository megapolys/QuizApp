<#import "../../parts/common.ftlh" as c>
<@c.page>
    <a class="btn btn-primary mb-3" href="/quiz/result/${userId?c}/${quizResultId?c}">Назад</a>
    <div id="task-card" class="card"></div>
    <script>

        $(async function () {
            await loadContent();
        });

        async function loadContent() {
            let quizTaskResultId = ${quizTaskResultId};
            let taskResult = await getTaskResult(quizTaskResultId);
            fillTaskResult(taskResult, quizTaskResultId);
        }

        function fillTaskResult(taskResult, quizTaskResultId) {
            let preQuestionText = taskResult.fiveVariantTask ? taskResult.fiveVariantTask.preQuestionText : taskResult.yesOrNoTask.preQuestionText;
            let questionText = taskResult.fiveVariantTask ? taskResult.fiveVariantTask.questionText : taskResult.yesOrNoTask.questionText;
            $('#task-card').html(`
            <h3 class="card-header">` + preQuestionText + `</h3>
            <div class="card-body">
                <p class="card-text">` + questionText + `</p>
                <div id="task-form"></div>
            </div>
            `);
            let fileName;
            if (taskResult.fiveVariantTask) {
                fileName = taskResult.fiveVariantTask.fileName;
            } else {
                fileName = taskResult.yesOrNoTask.fileName;
            }
            $('#task-form').html((fileName ? modalImageString("Показать картинку пример", "/img/" + fileName, "model-image-five-variant") : '') +
                `
            <div class="mb-3">
                <label for="inputText" class="form-label">Дополнительное пояснение</label>
                <textarea id="inputText" class="form-control" name="text" disabled>` + (taskResult.text ? taskResult.text : 'Не заполнено') + `</textarea>
            </div>
            <div class="input-group mb-3">
                <span class="input-group-text">Текущий балл, высчитанный автоматически:</span>
                <input type="number" name="score" class="form-control" step="0.1" value="` + taskResult.score + `" disabled>
                <label class="input-group-text">Балл</label>
                <input type="number" name="altScore" class="form-control" step="0.1" value="` + taskResult.altScore + `">
            </div>
            <button class="btn btn-primary" onclick="saveAltScore(` + quizTaskResultId + `)">Сохранить</button>
            `);
        }

        async function saveAltScore(quizTaskResultId) {
            clearMessage();
            let form = $('#task-form');
            let body = {
                quizTaskResultId: quizTaskResultId,
                altScore: form.find("[name|='altScore']").val()
            };
            let response = await fetch("/api/quiz/task/result", {
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
                showSuccess('Балл успешно обновлен')
                loadContent();
            } else {
                response.json().then(json => {
                    fillFormInvalidFields(json, form);
                })
            }
        }

        async function getTaskResult(quizTaskResultId) {
            return getRequestWithError('/api/quiz/task/result/' + quizTaskResultId, 'Ошибка при загрузки результата вопросов');
        }
    </script>
</@c.page>