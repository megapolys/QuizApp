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
    let list = decisions.children('div');
    let size = list.length;
    if (size === 0) {
        decisions.html("");
    }
    let i = 0;
    list.each(function () {
        if ($(this).attr('id') === id) {
            $(this).remove();
            exist = true;
            size--;
        }
        i++;
    });
    group = group ? group + " _ " : "";
    if (!exist) {
        let newElement = $("<div class=\"col-md-6\" id=" + id + ">\n" +
            "                    <div class=\"p-2 mb-2 rounded-3 position-relative\" style=\"background-color: #efadce\">\n" +
            "                       <a onclick=decisionFocus('" + groupId + "','" + decisionId + "')>" + group + name + "</a>\n" +
            "                       <button onclick=\"$('#" + decisionId + "')[0].click()\" type=\"button\" class=\"btn-close position-absolute top-50 end-0 translate-middle-y\" aria-label=\"Close\"></button>\n" +
            "                    </div>\n" +
            "               </div>");
        newElement.appendTo("#chosen-decisions");
        size++;
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

function copyToClipboard(content) {
    navigator.clipboard.writeText(content);
}
