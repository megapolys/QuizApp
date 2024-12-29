<#import "../parts/common.ftlh" as c>
<@c.page>
    <h3>Список пользователей</h3>
    <table id="user-table" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 30%">Имя</th>
            <th style="width: 30%">Почта</th>
            <th style="width: 20%"></th>
            <th style="width: 20%"></th>
        </tr>
        </thead>
        <tbody></tbody>
    </table>
    <script>
        $(async function () {
            await loadContent();
        });

        async function loadContent() {
            let userList = await getUserList();
            fillUserTable(userList);
        }

        function fillUserTable(userList) {
            let userTableBody = $('#user-table tbody');
            userTableBody.html('');
            userList.forEach(user => {
                userTableBody.append(`
                    <tr>
                        <td>` + user.username + `</td>
                        <td>` + user.email + `</td>
                        <td><a href="/quiz/result/` + user.id + `">Управление тестами</a></td>
                        <td><a href="/topic/result/` + user.id + `">Управление анализами</a></td>
                    </tr>
                `);
            })
        }

        async function getUserList() {
            return getRequestWithError('/api/user/all', 'Ошибка при загрузки списка пользователей');
        }
    </script>
</@c.page>