function switchTab(el, tab_id, path) {
    $("#taskTabs").children('li').children('a').removeClass("active");
    $("#taskTypeTabs").children().hide();

    $(el).addClass("active");
    $(tab_id).show();

    $("#taskForm").attr("action", path);
}

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
        let newElement = $("<div id=" + id + ">\n" +
            "                    <div class=\"p-2 mb-2 rounded-3 position-relative\" style=\"background-color: #efadce\">\n" +
            "                       <a onclick=decisionFocus('" + groupId + "','" + decisionId + "')>" + name + "</a>\n" +
            "                       <button onclick=\"$('#" + decisionId + "')[0].click()\" type=\"button\" class=\"btn-close position-absolute top-50 end-0 translate-middle-y\" aria-label=\"Close\"></button>\n" +
            "                    </div>\n" +
            "               </div>");
        newElement.appendTo(chosenDecisions);
    }
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
