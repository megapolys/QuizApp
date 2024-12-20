function switchTab(el, tab_id, path) {
    $("#taskTabs").children('li').children('a').removeClass("active");
    $("#taskTypeTabs").children().hide();

    $(el).addClass("active");
    $(tab_id).show();

    $("#taskForm").attr("action", path);
}

// using in quiz.ftlh
function chooseDecision(chosenDecisions, name, groupId, decisionId) {
    console.log("start chooseDecision");
    let exist = false;
    let decisions = $(chosenDecisions);
    let list = decisions.children('div');
    if (list.length === 0) {
        console.log("empty list of chosenDecisions");
        decisions.html("");
    }
    let id = 'decision-chosen-' + decisionId;
    list.each(function () {
        if ($(this).attr('id') === id) {
            $(this).remove();
            exist = true;
        }
    });
    if (!exist) {
        decisions.append(chosenElement(groupId, decisionId, name));
    }
}

function chosenElement(groupId, decisionId, name) {
    return $(`
                <div id="decision-chosen-` + decisionId + `">
                    <div class="p-2 mb-2 rounded-3 position-relative" style="background-color: #efadce">
                        <a onclick=decisionFocus('` + groupId + `','` + decisionId + `')>` + name + `</a>
                        <button onclick="$('#` + decisionId + `')[0].click()" type="button" class="btn-close position-absolute top-50 end-0 translate-middle-y" aria-label="Close"></button>
                    </div>
                </div>
            `);
}

function decisionFocus(groupId, decisionId) {
    let group = $('#' + groupId);
    let collapsed = group.attr("class").includes("collapsed");
    if (collapsed) {
        group[0].click();
    }

    if (collapsed) {
        setTimeout(function () {
            goToDecision(decisionId);
        }, 400);
    } else {
        goToDecision(decisionId);
    }
}

function goToDecision(decisionId) {
    let decisionLink = $("<a href='#" + decisionId + "'></a>");
    $("body").append(decisionLink);
    decisionLink[0].click(); // click() work with dom model, not with jquery
    decisionLink.remove();
}

function clickByChosen(inputFieldId, listId) {
    let decisionValue = $('#' + inputFieldId).val();
    $('#' + listId).children('option').each(function () {
        if ($(this).attr('value') === decisionValue) {
            $($(this).attr('for'))[0].click();
            $('#' + inputFieldId).val('');
            return
        }
    })
}

async function getGroupDecisions() {
    return getRequestWithError("/api/decisions", 'Ошибка при загрузке решений с группами');
}

async function getUngroupedDecisions() {
    return getRequestWithError("/api/decisions/ungrouped", 'Ошибка при загрузке решений');
}

function listOfChosenDecisions(decisionsName, chosenDecisions, groups, ungroupedDecisions) {
    let result = $(`
                <div class="mb-3">
                    <div class="row mb-2">
                        <div class="col-4">
                            Выбранные решения:
                        </div>
                        <div class="col-4">
                            <input class="form-control" list="` + decisionsName + `-decisionList" id="` + decisionsName + `-list" placeholder="Вводи решение"/>
                            <datalist id="` + decisionsName + `-decisionList">
                            </datalist>
                        </div>
                        <div class="col-4">
                            <button type="button" class="btn btn-primary" onclick="clickByChosen('` + decisionsName + `-list', '` + decisionsName + `-decisionList')">Клик</button>
                        </div>
                    </div>
                    <div id="chosen-` + decisionsName + `">
                    </div>
                </div>
            `);
    let datalist = result.find('#' + decisionsName + '-decisionList');
    let chosen = result.find('#chosen-' + decisionsName);
    groups.forEach(group => {
        group.decisions.forEach(decision => {
            datalist.append('<option for="#' + decisionsName + '-' + decision.id + '" value="' + group.name + ' _ ' + decision.name + '"/>');
            if (chosenDecisions.has(decision.id)) {
                chosen.append(chosenElement(
                    'group-' + group.id + '-' + decisionsName,
                    decisionsName + '-' + decision.id,
                    group.name + ' _ ' + decision.name
                ));
            }
        })
    })
    ungroupedDecisions.forEach(decision => {
        datalist.append('<option for="#' + decisionsName + '-' + decision.id + '" value="' + decision.name + '"/>');
        if (chosenDecisions.has(decision.id)) {
            chosen.append(chosenElement(
                'group-nonGroup-' + decisionsName,
                decisionsName + '-' + decision.id,
                decision.name
            ));
        }
    })
    return result;
}

function decisionsCheckbox(decisionsName, chosenDecisions, groups, ungroupedDecisions) {
    let result = $('<div class="accordion" id="accordion_groups"></div>');
    groups.forEach(group => {
        result.append(accordionGroup(
            decisionsName,
            'group-' + group.id + '-' + decisionsName,
            group.name,
            group.decisions,
            chosenDecisions,
            false
        ));
    })
    result.append(accordionGroup(
        decisionsName,
        'group-nonGroup-' + decisionsName,
        'Не в группе',
        ungroupedDecisions,
        chosenDecisions,
        true
    ));
    return result;
}

function accordionGroup(decisionsName, groupId, groupName, decisions, chosenDecisions, ungrouped) {
    return $(`
                <div class="accordion-item">
                    <h2 class="accordion-header">
                        <button id="` + groupId + `" class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#panelsStayOpen-` + groupId + `" aria-expanded="true" aria-controls="panelsStayOpen-` + groupId + `">
                            ` + groupName + `
                        </button>
                    </h2>
                    <div id="panelsStayOpen-` + groupId + `" class="accordion-collapse collapse">
                        <div class="row">
                            <div class="accordion-body col-12">
                                ` + (decisions.length === 0 ? '<p>Нет решений</p>' : `
                                    <table class="table table-striped">
                                        <thead>
                                        <tr>
                                            <th style="width: 60%"></th>
                                            <th style="width: 20%"></th>
                                            <th style="width: 20%"></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        ` + decisions.map(decision => {
        let decisionId = decisionsName + `-` + decision.id;
        return `
                                            <tr data-id="` + decision.name + `">
                                                <td>
                                                    <div class="form-check">
                                                        <input class="form-check-input" type="checkbox" name="` + decisionsName + `"
                                                               value="` + decision.id + `"
                                                               id="` + decisionId + `" ` + (chosenDecisions.has(decision.id) ? 'checked' : '') + `
                                                               onclick="chooseDecision(
                                                                       '#chosen-` + decisionsName + `',
                                                                       '` + (ungrouped ? '' : groupName + ' _ ') + decision.name + `',
                                                                       '` + groupId + `',
                                                                       '` + decisionId + `'
                                                                       )"
                                                        />
                                                        <label class="form-check-label" for="` + decisionId + `">
                                                            ` + decision.name + `
                                                        </label>
                                                    </div>
                                                </td>
                                                <td colspan="2">
                                                    ` + (decision.description ? decisionWithDescription(decision, 'Описание', decisionId) : '') + `
                                                </td>
                                            </tr>
                                            `;
    }).join('') + `
                                        </tbody>
                                    </table>
                                `) + `
                            </div>
                        </div>
                    </div>
                </div>
            `);
}

function decisionWithDescription(decision, name, id) {
    if (decision.description) {
        return `
                <a href="##" type="button" class="icon-link icon-link-hover text-decoration-none p-0" style="--bs-icon-link-transform: translate3d(0, -.125rem, 0);"
                   data-bs-toggle="modal" data-bs-target="#decision-modal-` + id + `" >
                    <i class="bi bi-clipboard me-1"></i>
                    ` + name + `
                </a>
                <div class="modal fade" id="decision-modal-` + id + `" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                ` + decision.description + `
                            </div>
                        </div>
                    </div>
                </div>
                `;
    } else {
        return '<a href="##" class="text-decoration-none" aria-disabled="true">' + name + '</a>';
    }
}