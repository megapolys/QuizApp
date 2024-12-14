<#import "../../parts/common.ftlh" as c>
<#import "../../parts/modal.ftlh" as modal>
<@c.page>
    <h3 class="mb-3">Список анализов</h3>
    <div id="medical-form" class="mb-3"></div>
    <table id="medical-topic-table" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 60%">Наименование</th>
            <th style="width: 10%">Размер</th>
            <th style="width: 10%"></th>
            <th style="width: 10%"></th>
            <th style="width: 10%"></th>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>

    <script>

        $(async function () {
            await loadContent();
        });

        async function loadContent() {
            fillMedicalForm();
            let medicalList = await getMedicalList();
            fillMedicalListTable(medicalList);
        }

        function fillMedicalForm() {
            $('#medical-form').append(`
                <div class="row">
                    <div class="col-8">
                        <input class="form-control" type="text" name="name" placeholder="Наименование анализа" aria-describedby="validationMedicalFormName"/>
                        <div id="validationMedicalFormName" class="invalid-feedback"></div>
                    </div>
                    <div class="col-2">
                        <button class="btn btn-primary" onclick="addMedicalTopic()">Добавить</button>
                    </div>
                </div>
            `);
        }

        function fillMedicalListTable(medicalList) {
            let medicalListBody = $('#quiz-table tbody');
            medicalListBody.html('');
            medicalList.forEach(medicalTopic => {
                medicalListBody.append(`
                    <tr>
                        <td>` + medicalTopic.name + `</td>
                        <td>` + (medicalTopic.size === 0 ? 'пусто' : medicalTopic.size) + `</td>
                        <td><a href="#!" onclick="cloneMedicalTopic(` + medicalTopic.id + `)">Клонировать</a></td>
                        <td><a href="/medical/` + medicalTopic.id + `">Открыть</a></td>
                        <td>` + deleteConfirmString('deleteMedical(' + medicalTopic.id + ')', 'confirm-delete-medical-' + medicalTopic.id, 'Вы точно уверены, что хотите удалить список анализов? Это необратимая операция, которая приведет к тому же к удалению всех заполненных анкет анализов.') + `</td>
                    </tr>
                `);
            })
        }

        async function addMedicalTopic() {
            let form = $('#medical-form');
            let medical = {
                name: form.find("[name|='name']").val()
            };
            let response = await fetch("/api/medical", {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    '${_csrf.headerName}': '${_csrf.token}'
                },
                body: JSON.stringify(medical)
            });
            if (response.ok) {
                showSuccess('Анализ ' + medical.name + ' успешно добавлен');
                await loadContent();
            } else {
                response.json().then(json => {
                    fillFormInvalidFields(json, form);
                })
            }
        }

        async function cloneMedicalTopic(id) {
            let response = await fetch("/api/medical/clone/" + id, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    '${_csrf.headerName}': '${_csrf.token}'
                }
            });
            if (response.ok) {
                showSuccess('Анализ успешно клонирован');
                await loadContent();
            } else {
                response.json().then(json => {
                    fillFormInvalidFields(json, form);
                })
            }
        }

        async function getMedicalList() {
            return getRequestWithError('/api/medical', 'Ошибка при загрузки списка анализов');
        }

        async function deleteMedical(id) {
            await deleteByPath('/api/medical/' + id, 'Анализ успешно удален');
            await loadContent();
        }

    </script>
</@c.page>