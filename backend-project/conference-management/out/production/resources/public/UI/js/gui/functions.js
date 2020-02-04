/**
 * Created by elisei on 07.06.2017.
 */

var errorsDiv, warningsDiv;

var c, data;

function formatPage(page) {
    return page.substr(0, 1).toUpperCase() + page.substr(1).replace(/([A-Z])/g, function (a) {
        return " " + a;
    })
}

function showWarnings(warnings) {
    warningsDiv.empty();
    warningsDiv.fadeIn("fast");
    for (var i = 0; i < warnings.length; i++) {
        var m = warnings[i];

        var $p = $("<p></p>");
        $p.html(m);
        warningsDiv.append($p);
    }
    setTimeout(function () {
        warningsDiv.fadeOut();
    }, 5000);
}


function showErrors(errors) {
    errorsDiv.empty();
    errorsDiv.fadeIn("fast");
    for (var i = 0; i < errors.length; i++) {
        var m = errors[i];

        var p = $("<p></p>");
        p.html(m);
        errorsDiv.append(p);
    }
    setTimeout(function () {
        errorsDiv.fadeOut();
    }, 5000);
}

function clearMessages() {
    errorsDiv.empty();
    warningsDiv.empty();
}

function isEmpty(val) {
    if (val === "") {
        return true;
    }
    return false;
}

function getVariableFromCookie(v, convertToInt = false) {
    var cookie = document.cookie;
    var s = cookie.split(';');
    for (var i in s) {
        var k, v;
        if (s[i].indexOf(v) > -1) {
            if (convertToInt) {
                try {
                    return parseInt(s[i].split('=')[1].trim());
                } catch (e) {

                }
            } else {
                return s[i].split('=')[1].trim();
            }

        }
    }
    return undefined;
}

function getUsernameFromCookie() {
    return getVariableFromCookie("username");
}

function tableClearLetHeader(table) {
    table.find("tr").filter(function () {
        return $(this).children('th').length === 0
    }).remove();
}

function getLoginCredidentials() {
    var username = $("input[name='username']").val();
    var password = $("input[name='password']").val();

    if (username === "" || password === "") {

        showErrors(["You must put values for each field!"]);
        throw new Error();
    }

    return new User(username, "", password, "");
}

function getRegisterData() {
    var email = $("input[name='email']").val();
    var username = $("input[name='username']").val();
    var password = $("input[name='password']").val();
    var discogsToken = $("input[name='discogsToken']").val();

    if (email === "" || username === ""
        || password === "") {
        showErrors(["You must put values for each field!"]);
        throw new Error();
    }
    return new User(username, email, password, discogsToken);
}

function showPopup(id) {
    $("#" + id.toString()).fadeIn();
}

function closeButtonClick(e) {
    $(this).parent().parent().fadeOut();
}

function showSystemPopp(e) {
    e.preventDefault();
    var a = $(this).children().first();
    var id = a.attr("href");
    showPopup(id);
}

function loadSearchItems() {
    function populate(searchItems) {

        for (var i in searchItems) {
            var tr = $("<tr></tr>");
            var td;

            td = $("<td></td>");
            td.html("" + i);
            tr.append(td);

            td = $("<td></td>");
            td.html(searchItems[i].searchString);
            tr.append(td);

            td = $("<td></td>");
            td.html(searchItems[i].resultsCount);
            tr.append(td);

            td = $("<td></td>");
            td.html(searchItems[i].newlyReported ? "NEW!" : "   ");
            tr.append(td);

            td = $("<td></td>");
            td.html("<a href='" + searchItems[i].searchLink + "'> Go to </a>");
            tr.append(td);

            table.append(tr);
        }
    }

var table = $("#searchItemsTable");
tableClearLetHeader(table);

c.getSearchResults(populate);
}

function logout() {
    document.cookie = "username=;expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    document.cookie = "password=;expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
}

function main() {
    $(function () {
        c = new Controller(showErrors, showWarnings);

        var pagename = location.pathname.split('/').reverse()[0];
        var page = PAGES_REVERSE[pagename];

        if (isUndefined(page) || WITHOUT_SESSION.indexOf(page) >= 0) {

        } else {
            //SESSION_PAGES
            c.loggedIn(function (logged, priviledge) {
                if (!logged) {
                    logout();
                    setTimeout(function () {
                        window.location.href = "index.html";
                    }, 2000);
                    return;
                }

                $("#addSearchButton").click(function (e) {
                    var searchString = $("input[name='searchString']").val();
                    clearMessages();
                    e.preventDefault(true);

                    c.addSearchString(searchString, function () {
                        loadSearchItems();
                    });
                });

                loadSearchItems();
            });
        }

        errorsDiv = $("#errors");
        errorsDiv.click(function () {
            errorsDiv.fadeOut();
        });
        warningsDiv = $("#warnings");
        warningsDiv.click(function () {
            warningsDiv.fadeOut();
        });

        $("#loginButton").click(function (e) {
            var user;
            clearMessages();
            e.preventDefault(true);
            try {
                user = getLoginCredidentials();
            } catch (c) {
                return;
            }

            c.login(user.username, user.password, function () {
                //document.cookie  = "username =" + user.username + "; path=/";
                window.location.href = "homepage.html";
            });
        });

        $("#registerButton").click(function (e) {
            var user;
            clearMessages();
            e.preventDefault(true);
            try {
                user = getRegisterData();
            } catch (e) {
                return;
            }
            c.saveUser(user, function () {
                showWarnings(["Account created successfully!<br>You will be redirected to login.html!"]);
                setTimeout(function () {
                    window.location.href = "login.html";
                }, 2000);

            })
        });

        $(".closeButton").click(closeButtonClick);

        //todo 	--!!!! PLEASE USE THIS INSTEAD OF

    });
}

$.getScript("js/domain/entity.js", function () {
    $.getScript("js/service/controller.js", function () {
        $.getScript("js/util/utils.js", function () {
            main();
        })
    })
})
