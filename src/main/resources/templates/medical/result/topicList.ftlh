<#import "../../parts/common.ftlh" as c>
<@c.page>
    <a class="btn btn-primary mb-3" href="/user/list">К списку пользователей</a>
    <h2 id="user-card" class="mb-3"></h2>
    <h3 class="mb-3">Активные списки</h3>
    <table id="medical-result-table" class="table">
        <thead>
        <tr>
            <th style="width: 10%">Дата завершения</th>
            <th style="width: 10%">Дата последнего обновления</th>
            <th style="width: 10%">Балл</th>
            <th style="width: 10%">Статус</th>
            <th style="width: 10%"></th>
            <th style="width: 10%"></th>
        </tr>
        </thead>
        <tbody></tbody>
    </table>

    <h3 class="mb-3">Список анализов</h3>
    <table id="medical-table" class="table">
        <thead>
        <tr>
            <th style="width: 60%">Наименование</th>
            <th style="width: 10%">Размер</th>
            <th style="width: 30%">Действие</th>
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
            let user = await getUserById(userId);
            fillUserCard(user);
            let medicalResultList = await getMedicalResultList(userId);
            let colorRatioProperty = await getColorRatioProperty();
            fillMedicalResultTable(medicalResultList, colorRatioProperty, userId);
            let medicalList = await getMedicalList();
            fillMedicalTable(medicalList, medicalResultList, userId);
        }

        function fillUserCard(user) {
            $('#user-card').html(user.lastName + ' ' + user.firstName + ' ' + user.birthday);
        }

        function fillMedicalResultTable(medicalResultList, colorRatioProperty, userId) {
            let table = $('#medical-result-table tbody');
            table.html('');
            medicalResultList.forEach(medicalResult => {
                table.append(`
                <tr>
                    <td colspan="6"><h5>` + medicalResult.topic.name + `</h5></td>
                </tr>
                `);
                medicalResult.results.forEach(result => {
                    let colorClass = "";
                    if (result.complete) {
                        if (result.score > colorRatioProperty.red) {
                            colorClass = "table-danger";
                        } else if (result.score > colorRatioProperty.yellow) {
                            colorClass = "table-warning";
                        } else {
                            colorClass = "table-success";
                        }
                    }
                    table.append(`
                    <tr class="` + colorClass + `">
                        <td>` + (result.complete ? result.completeDate : "") + `</td>
                        <td>` + (result.complete ? result.lastUpdateDate : "") + `</td>
                        <td>` + (result.complete ? round(result.score) : "") + `</td>
                        <td>` + (result.complete ? "Заполнен" : "В работе " + result.countCompleted + "/" + result.taskCount) + `</td>
                        <td>` + (result.complete ? '<a href="/topic/result/' + userId + '/' + result.id + '">Результаты</a>' : '') + `</td>
                        <td>` + deleteConfirmString('deleteMedicalResult(' + result.id + ')', 'confirm-delete-medical-result-' + result.id, 'Точно удалить результаты анализов?') + `</td>
                    </tr>
                    `);
                })
            })
        }

        function fillMedicalTable(medicalList, medicalResultList, userId) {
            let assignedMedicalSet = new Set();
            let completedMedicalSet = new Set();
            medicalResultList.forEach(medicalResult => {
                assignedMedicalSet.add(medicalResult.topic.id);
                let allCompleted = true;
                medicalResult.results.forEach(result => {
                    if (!result.complete) {
                        allCompleted = false;
                    }
                })
                if (allCompleted) {
                    completedMedicalSet.add(medicalResult.topic.id);
                }
            })
            let table = $('#medical-table tbody');
            table.html('');
            medicalList.forEach(topic => {
                table.append(`
                <tr>
                    <td>` + topic.name + `</td>
                    <td>` + (topic.size > 0 ? topic.size : 'пусто') + `</td>
                    <td>` + (completedMedicalSet.has(topic.id) ?
                    '<button class="btn btn-link p-0 mb-1" onclick="createMedicalResult(' + userId + ', ' + topic.id + ')">Назначить еще раз</button>'
                    : (assignedMedicalSet.has(topic.id) ? 'Уже назначено' : '<button class="btn btn-link p-0 mb-1" onclick="createMedicalResult(' + userId + ', ' + topic.id + ')">Назначить</button>')) + `
                    </td>
                </tr>
                `);
            })
        }

        async function getUserById(userId) {
            return getRequestWithError('/api/user/' + userId, 'Ошибка при загрузке данных пользователя');
        }

        async function getMedicalResultList(userId) {
            return getRequestWithError('/api/medical/result?userId=' + userId, 'Ошибка при загрузке данных результатов анализов');
        }

        async function getMedicalList() {
            return getRequestWithError('/api/medical', 'Ошибка при загрузки списка анализов');
        }

        async function getColorRatioProperty() {
            return getRequestWithError('/api/property/color-ratio', 'Ошибка при загрузке настроек цветов');
        }

        async function deleteMedicalResult(id) {
            await deleteByPath('/api/medical/result/' + id, 'Результат анализов успешно удален');
            await loadContent();
        }

        async function createMedicalResult(userId, topicId) {
            let response = await fetch('/api/medical/result?userId=' + userId + '&topicId=' + topicId, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    '${_csrf.headerName}': '${_csrf.token}'
                }
            });
            if (response.ok) {
                showSuccess('Анализы успешно назначены пользователю');
                await loadContent();
            } else {
                showError('Ошибка при попытке назначить анализы пользователю');
            }
        }
    </script>
</@c.page>