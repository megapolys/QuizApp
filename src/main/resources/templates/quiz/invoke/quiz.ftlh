<#import "../../parts/common.ftlh" as c>
<@c.page>
    <div class="progress mb-3" role="progressbar" aria-label="Basic example" aria-valuenow="0" aria-valuemin="0"
         aria-valuemax="100">
        <div id="progress-bar" class="progress-bar" style="width: 0%"></div>
    </div>
    <div id="task-card" class="card"></div>

    <script>

        let taskList;

        let filledTaskMap = new Map();

        $(async function () {
            await loadContent();
        });

        async function loadContent() {
            let quizResultId = ${quizResultId};
            taskList = await getQuizTaskList(quizResultId);
            fillTask(null);
        }

        function fillTask(taskIndex) {
            let taskCount = taskList.length;
            let completeCount = 0;
            let task;
            for (let i = 0; i < taskList.length; i++) {
                task = taskList[i];
                if (task.complete) {
                    completeCount = completeCount + 1;
                } else {
                    if (!taskIndex) {
                        taskIndex = i;
                    }
                    break;
                }
            }
            if (completeCount === taskCount) {
                window.location.replace("/quiz/invoke");
            }
            task = taskList[taskIndex];
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
            <div class="card-footer text-body-secondary text-center">
                <div id="pagination-nav">
                    <nav aria-label="Пример навигации по страницам">
                        <ul class="pagination justify-content-center mb-0">
                        </ul>
                    </nav>
                </div>
            </div>
            `);

            let paginationNav = $('#pagination-nav nav ul');
            let navLen = 3;
            let startIndex = Math.max(0, taskIndex - navLen + 1);
            let endIndex = Math.min(taskList.length, taskIndex + navLen);
            for (let i = startIndex; i < endIndex; i++) {
                paginationNav.append('<li class="page-item' + (i === taskIndex ? ' active' : '') + '"><a class="page-link" href="#" onclick="fillTask(' + i + ')">' + (i + 1) + '</a></li>');
            }
            paginationNav.append('<li class="page-item"><a class="page-link" href="#" onclick="fillTask(null)">Следующий вопрос</a></li>');

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

            let filledTask = filledTaskMap.get(task.id);
            if (filledTask) {
                taskInvokeForm.find("[name|='variant']").each(function () {
                    let checkbox = $(this);
                    if (checkbox.val() === filledTask.variant) {
                        checkbox.prop('checked', true);
                    }
                })
                taskInvokeForm.find("[name|='text']").val(filledTask.text);
            }
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
            let variant;
            form.find("[name|='variant']").each(function () {
                let checkbox = $(this);
                if (checkbox.is(':checked')) {
                    variant = checkbox.val();
                }
            })
            let body = {
                quizTaskResultId: task.id,
                variant: variant,
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
                filledTaskMap.set(task.id, body);
                fillTask(null);
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