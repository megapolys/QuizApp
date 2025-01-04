<#import "../../parts/common.ftlh" as c>
<@c.page>
    <h3 class="mb-3">Список анализов</h3>
    <table id="medical-invoke-table" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 30%">Наименование</th>
            <th style="width: 10%">Действие</th>
            <th style="width: 10%">Дата завершения</th>
            <th style="width: 10%">Дата последнего изменения</th>
        </tr>
        </thead>
        <tbody></tbody>
    </table>
    <script>

        $(async function () {
            await loadContent();
        });

        async function loadContent() {
            let medicalResultList = await getMedicalResultList();
            fillMedicalResultTable(medicalResultList);
        }

        function fillMedicalResultTable(medicalResultList) {
            let table = $('#medical-invoke-table tbody');
            table.html('');
            medicalResultList.forEach(medicalResult => {
                table.append(`
                <tr>
                    <td colspan="4"><h5>` + medicalResult.topic.name + `</h5></td>
                </tr>
                `);
                medicalResult.results.forEach(result => {
                    table.append(`
                    <tr>
                        <td></td>
                        <td><a href="/medical/invoke/` + result.id + `">` + (result.complete ? 'Скорректировать' : (result.countCompleted > 0 ? "Продолжить " + result.countCompleted + "/" + result.taskCount : "Заполнить")) + `</a></td>
                        <td>` + (result.complete ? result.completeDate : '') + `</td>
                        <td>` + (result.lastUpdateDate ? result.lastUpdateDate : '') + `</td>
                    </tr>
                    `);
                })
            })
        }

        async function getMedicalResultList() {
            return getRequestWithError('/api/medical/invoke', 'Ошибка при загрузки назначенных топиков анализов');
        }
    </script>
</@c.page>