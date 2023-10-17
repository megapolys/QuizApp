<#macro image text path id>
    <button type="button" class="btn btn-outline-danger btn-sm mb-3" data-bs-toggle="modal" data-bs-target="#${id}">
        ${text}
    </button>
    <div class="modal fade" id="${id}" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-fullscreen-sm-down">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Картиночка</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <img class="img-thumbnail" src="${path}" width="100%">
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro deleteConfirm text path id>
    <a href="##" class="link-danger" data-bs-toggle="modal" data-bs-target="#${id}">
        Удалить
    </a>
    <div class="modal fade" id="${id}" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="staticBackdropLabel">Подтверждение удаления</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    ${text}
                </div>
                <div class="modal-footer">
                    <form action="${path}" method="post">
                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                        <button class="btn btn-danger" type="submit">Удалить</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro postButton path text>
    <form action="${path}" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button class="btn btn-link p-0 mb-1" type="submit">${text}</button>
    </form>
</#macro>