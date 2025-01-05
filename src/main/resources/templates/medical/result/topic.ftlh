<#import "../../parts/common.ftlh" as c>
<@c.page>
    <div class="row mb-3">
        <div class="col-2">
            <a class="btn btn-primary" href="/result/topic/${userId?c}">Назад</a>
        </div>
        <div class="col-10">
            <h3 id="medical-name"></h3>
        </div>
    </div>
    <h4 class="mb-3">Сводная таблица решений</h4>
    <table id="decisions-table" class="table mb-5">
        <thead>
        <tr>
            <th style="width: 30%">Группа</th>
            <th style="width: 50%">Решение</th>
            <th style="width: 5%">Баллы</th>
            <th style="width: 10%">Баллы/Вопросы</th>
            <th style="width: 5%">Вопросы</th>
        </tr>
        </thead>
        <tbody></tbody>
    </table>
    <h4 class="mb-3">Список заполненных анализов</h4>
    <table id="medical-task-result-table" class="table mb-5">
        <thead>
        <tr>
            <th style="width: 40%">Название анализа</th>
            <th style="width: 10%"><i class="bi bi-speedometer"></i></th>
            <th style="width: 10%">Решения</th>
            <th style="width: 5%">Левый оптимум</th>
            <th style="width: 5%">Правый оптимум</th>
            <th style="width: 5%">Значение</th>
            <th style="width: 20%"></th>
            <th style="width: 5%">Балл</th>
        </tr>
        </thead>
        <tbody></tbody>
    </table>

    <script>

        $(async function () {
            await loadContent();
        });

        async function loadContent() {
            let userId = ${userId};
            let topicResultId = ${topicResultId};
            let medicalResultInfo = await getMedicalResultInfo(topicResultId);
            $('#medical-name').html(medicalResultInfo.topicName);
            fillDecisionsTable(medicalResultInfo.decisions);
            fillTaskTable(medicalResultInfo.results);
        }

        function fillDecisionsTable(decisions) {
            let decisionsTable = $('#decisions-table tbody');
            decisionsTable.html('');
            decisions.forEach(decision => {
                decisionsTable.append(`
                <tr>
                    <td>` + (decision.decision.groupName ? decision.decision.groupName : '') + `</td>
                    <td>` + decisionWithDescription(decision.decision, decision.decision.name, decision.decision.id) + `</td>
                    <td>` + round(decision.score) + `</td>
                    <td>` + round(decision.score / decision.count) + `</td>
                    <td>` + decision.count + `</td>
                </tr>
                `);
            })
        }

        function fillTaskTable(results) {
            let taskTable = $('#medical-task-result-table tbody');
            taskTable.html('');
            results.forEach(result => {
                taskTable.append(`
                <tr>
                    <td>` + result.task.name + `</td>
                    <td>` + result.task.unit + `</td>
                    <td>` + getDecisionModalByTask(result) + `</td>
                    <td>` + result.task.leftMid + `</td>
                    <td>` + result.task.rightMid + `</td>
                    <td>` + round(result.value) + `</td>
                    <td>` + getAnalyseHistogram(result) + `</td>
                    <td>` + result.score + `</td>
                </tr>
                `);
            })
        }

        function getDecisionModalByTask(resultTask) {
            if (resultTask.decisions.length > 0) {
                return `
            <a href="##" data-bs-toggle="modal" data-bs-target="#medical-task-` + resultTask.task.id + `" >
                Посмотреть
            </a>
            <div class="modal fade" id="medical-task-` + resultTask.task.id + `" tabindex="-1" aria-hidden="true">
                <div class="modal-xl modal-fullscreen-lg-down modal-dialog modal-dialog-centered modal-dialog-scrollable">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h2>` + resultTask.task.name + `</h2>
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
                                ` + resultTask.decisions.map(decision => {
                    return `
                                    <tr>
                                        <td>` + (decision.groupName ? decision.groupName + ' _ ' : '') + decision.name + `</td>
                                        <td>` + (decision.description ? decision.description : '') + `</td>
                                    </tr>
                                    `;
                }).join('') + `
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            `;
            } else {
                return 'Решений нет';
            }
        }

        function getAnalyseHistogram(result) {
            let value = result.value;
            let task = result.task;
            let gap = task.rightRight - task.leftLeft;
            let percent = 0.1;
            let min = Math.min(value, task.leftLeft) - gap * percent;
            let max = Math.max(value, task.rightRight) + gap * percent;
            let len = max - min;
            return `
            <div class="progress-stacked">
                <div class="progress" role="progressbar"
                     style="width: ` + ((task.leftLeft - min) / len * 100) + `%">
                    <div class="progress-bar bg-danger"></div>
                </div>
                <div class="progress" role="progressbar"
                     style="width: ` + ((task.leftMid - task.leftLeft) / len * 100) + `%">
                    <div class="progress-bar bg-warning"></div>
                </div>
                <div class="progress" role="progressbar"
                     style="width: ` + ((task.rightMid - task.leftMid) / len * 100) + `%">
                    <div class="progress-bar bg-success"></div>
                </div>
                <div class="progress" role="progressbar"
                     style="width: ` + ((task.rightRight - task.rightMid) / len * 100) + `%">
                    <div class="progress-bar bg-warning"></div>
                </div>
                <div class="progress" role="progressbar"
                     style="width: ` + ((max - task.rightRight) / len * 100) + `%">
                    <div class="progress-bar bg-danger"></div>
                </div>
            </div>
            <div class="fs-3" style="transform: translate(` + ((value - min) / len * 100) + `%, -72%)">I</div>
            `;
        }

        async function getMedicalResultInfo(topicResultId) {
            return getRequestWithError('/api/medical/result/' + topicResultId, 'Ошибка при загрузки данных результата топика анализов');
        }
    </script>
</@c.page>