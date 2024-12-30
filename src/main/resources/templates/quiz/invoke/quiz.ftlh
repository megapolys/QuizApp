<#import "../../parts/common.ftlh" as c>
<#import "../../parts/invoke.ftlh" as invoke>
<@c.page>
    <div class="progress mb-3" role="progressbar" aria-label="Basic example" aria-valuenow="0" aria-valuemin="0"
         aria-valuemax="100">
        <div id="progress-bar" class="progress-bar" style="width: 0%"></div>
    </div>
    <div id="task-card" class="card"></div>

    <script>

        let taskList;

        $(async function () {
            await loadContent();
        });

        async function loadContent() {
            let quizResultId = ${quizResultId};
            taskList = await getQuizTaskList(quizResultId);
            fillTask();

        }

        function fillTask() {
            let taskCount = taskList.length;
            let completeCount = 0;
            let task;
            let taskIndex;
            for (let i = 0; i < taskList.length; i++) {
                task = taskList[i];
                if (task.complete) {
                    completeCount = completeCount + 1;
                } else {
                    taskIndex = i;
                    break;
                }
            }
            if (completeCount === taskCount) {
                window.location.replace("/quiz/invoke");
            }
            console.log((taskIndex + 1) + '/' + taskCount);
            let lastTask = completeCount === taskCount - 1;
            $('#progress-bar').css({"width": (completeCount / taskCount * 100) + '%'});
            let preQuestionText = task.fiveVariantTask ? task.fiveVariantTask.preQuestionText : task.yesOrNoTask.preQuestionText;
            let questionText = task.fiveVariantTask ? task.fiveVariantTask.questionText : task.yesOrNoTask.questionText;
            $('#task-card').html(`
            <h3 class="card-header">` + preQuestionText + `</h3>
            <div class="card-body">
                <p class="card-text">` + questionText + `</p>
                <div id="task-invoke-form"></div>
            </div>
            <div class="card-footer text-body-secondary text-center">` + (taskIndex + 1) + `/` + taskCount + `</div>
            `);
            let taskInvokeForm = $('#task-invoke-form');
            if (task.fiveVariantTask) {
                let fileName = task.fiveVariantTask.fileName;
                taskInvokeForm.html(
                    (fileName ? modalImageString("Показать картинку пример", "/img/" + fileName, "model-image-five-variant") : '') +
                    `
                <div class="input-group mb-3">
                    <div class="form-check me-3">
                        <input class="form-check-input" type="radio" name="variant" value="1" id="radio-1" aria-describedby="validation-task-card-variant">
                        <label class="form-check-label" for="radio-1">Никогда</label>
                        <div id="validation-task-card-variant" class="invalid-feedback"></div>
                    </div>
                    <div class="form-check me-3">
                        <input class="form-check-input" type="radio" name="variant" value="2" id="radio-2" aria-describedby="validation-task-card-variant">
                        <label class="form-check-label" for="radio-2">Редко</label>
                    </div>
                    <div class="form-check me-3">
                        <input class="form-check-input" type="radio" name="variant" value="3" id="radio-3" aria-describedby="validation-task-card-variant">
                        <label class="form-check-label" for="radio-3">Иногда</label>
                    </div>
                    <div class="form-check me-3">
                        <input class="form-check-input" type="radio" name="variant" value="4" id="radio-4" aria-describedby="validation-task-card-variant">
                        <label class="form-check-label" for="radio-4">Часто</label>
                    </div>
                    <div class="form-check me-3">
                        <input class="form-check-input" type="radio" name="variant" value="5" id="radio-5" aria-describedby="validation-task-card-variant">
                        <label class="form-check-label" for="radio-5">Всегда</label>
                    </div>
                </div>
                `);
            } else {
                let fileName = task.yesOrNoTask.fileName;
                taskInvokeForm.html(
                    (fileName ? modalImageString("Показать картинку пример", "/img/" + fileName, "model-image-yes-or-no") : '') +
                    `
                <div class="input-group mb-3">
                    <div class="form-check me-3">
                        <input class="form-check-input" type="radio" name="variant" value="1" id="radio-1" aria-describedby="validation-task-card-variant">
                        <label class="form-check-label" for="radio-1">Да</label>
                        <div id="validation-task-card-variant" class="invalid-feedback"></div>
                    </div>
                    <div class="form-check me-3">
                        <input class="form-check-input" type="radio" name="variant" value="2" id="radio-2" aria-describedby="validation-task-card-variant">
                        <label class="form-check-label" for="radio-2">Нет</label>
                    </div>
                </div>
                `);
            }
            taskInvokeForm.append(`
                <div class="mb-3">
                    <label for="inputText" class="form-label">Дополнительное пояснение</label>
                    <textarea id="inputText" class="form-control" name="text"
                              placeholder="можно оставить пустым"></textarea>
                </div>
            `);
            taskInvokeForm.append(lastTask ?
                '<button class="btn btn-success" onclick="finishQuiz(' + taskIndex + ')">Завершить тестирование</button>' :
                '<button class="btn btn-primary" onclick="nextTask(' + taskIndex + ')">Следующий вопрос</button>');
        }

        async function finishQuiz(index) {
            await taskCompletePost(taskList[index]);
        }

        async function nextTask(index) {
            await taskCompletePost(taskList[index]);
        }

        async function taskCompletePost(task) {
            clearMessage();
            let form = $('#task-invoke-form');
            let body = {
                quizTaskResultId: task.id,
                variant: form.find("[name|='variant']").val(),
                text: form.find("[name|='text']").val()
            };
            let response = await fetch("/api/quiz/task/invoke", {
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
                task.complete = true;
                fillTask();
            } else {
                response.json().then(json => {
                    fillFormInvalidFields(json, form);
                })
            }
        }

        async function getQuizTaskList(quizResultId) {
            return getRequestWithError('/api/quiz/task/invoke?quizResultId=' + quizResultId, 'Ошибка при загрузки списка вопросов теста');
        }

    </script>
</@c.page>