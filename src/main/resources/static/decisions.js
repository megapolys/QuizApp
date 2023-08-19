function switchTab(el, tab_id, path) {
    $("#taskTabs").children('li').children('a').removeClass("active");
    $("#taskTypeTabs").children().hide();

    $(el).addClass("active");
    $(tab_id).show();

    $("#taskForm").attr("action", path);
}

function chooseDecision(id, group, name, groupId, decisionId) {
    let exist = false;
    let decisions = $("#chosen-decisions");
    let list = decisions.children('a');
    let size = list.length;
    if (size === 0) {
        decisions.html("");
    }
    let cur = -1;
    let i = 0;
    list.each(function () {
        if ($(this).attr('id') === id) {
            $(this).remove();
            exist = true;
            size--;
            cur = i;
        }
        i++;
    });
    let delimiter = size === 0 ? "" : ", ";
    group = group ? group + "." : "";
    if (!exist) {
        let newElement = $("<a id=" + id + " onclick=decisionFocus('" + groupId + "','" + decisionId + "')>" + delimiter + group + name + "</a>");
        newElement.appendTo("#chosen-decisions");
        size++;
    }
    if (cur === 0 && size > 0) {
        $(list.get(1)).html($(list.get(1)).html().substring(1))
    }
    if (size === 0) {
        decisions.html("Нет выбранных решений");
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
