/**
 * Created by elisei on 07.06.2017.
 */

var errorsDiv, warningsDiv;

var c;

function showWarnings(warnings){
    warningsDiv.empty();
    warningsDiv.fadeIn("fast");
    for(var i=0;i<warnings.length;i++){
        var m = warnings[i];

        var $p = $("<p></p>");
        $p.html(m);
        warningsDiv.append($p);
    }
    setTimeout(function(){
        warningsDiv.fadeOut();
    }, 5000);
}


function showErrors(errors){
    errorsDiv.empty();
    errorsDiv.fadeIn("fast");
    for(var i=0;i<errors.length;i++){
        var m = errors[i];

        var p = $("<p></p>");
        p.html(m);
        errorsDiv.append(p);
    }
    setTimeout(function(){
        errorsDiv.fadeOut();
    }, 5000);
}

function clearMessages(){
    errorsDiv.empty();
    warningsDiv.empty();
}

function isEmpty(val){
    if(val === ""){
        return true;
    }
    return false;
}

function getLoginCredidentials(){
    var username = $("input[name='username']").val();
    var password = $("input[name='password']").val();

    if(username === "" || password === ""){

        showErrors(["You must put values for each field!"]);
        throw  new Error();
    }

    return new User(0, username, "", "", "", "",  password);
}

function getRegisterData(){
    var name = $("input[name='fname']").val();
    var affiliation = $("input[name='affiliation']").val();
    var email = $("input[name='email']").val();
    //todo verifica daca exista campul website
    var website = $("input[name='website']").val();
    var username = $("input[name='username']").val();
    var password = $("input[name='password']").val();

    if(name === "" || affiliation === "" || email === "" || username === ""
    || password === "" || website === ""){
        showErrors(["You must put values for each field!"]);
        throw new Error();
    }
    return new User(0, name, username,email, affiliation, website, password, false );
}

function showPopup(id){
    $("#" + id.toString()).fadeIn();
}
function closeButtonClick(e){
    $(this).parent().parent().fadeOut();
}

function showSystemPopp(e){
    e.preventDefault();
    var a = $(this).children().first();
    var id = a.attr("href");
    showPopup(id);
}

function fillModalGrantSteering(){
    function populate(users){
        function grantSteering(e){
            var tds = $(this).parent().parent().children();
            var username = tds.get(0);
            var name = tds.get(1);
            var affiliation = tds.get(2);
            var website = tds.get(3);
            var email = tds.get(4);
            var isCometeeMember = true;
            c.updateUser(new User(0, username, name, email, affiliation, website, "", undefined, isCometeeMember));
        }

        for(var i in users){
            var tr = $("<tr></tr>");
            var td;

            td = $("<td></td>");
            td.html(users[i].username);
            tr.append(td);

            td = $("<td></td>");
            td.html(users[i].name);
            tr.append(td);

            td = $("<td></td>");
            td.html(users[i].affiliation);
            tr.append(td);


            td = $("<td></td>");
            td.html(users[i].website);
            tr.append(td);


            td = $("<td></td>");
            td.html(users[i].email);
            tr.append(td);

            td = $("<td></td>");
            if(users[i].isCometeeMember){
                td.html("<p>Already granted</p>")
            }
            else{
                td.html("<button>Grant</button>");
                td.children("button").click(grantSteering);
            }
            tr.append(td);
            table.append(tr);

        }
    }

    var table = $("#steeringUsersTable");
    console.log(table);
    table.find("tr").filter(function(){
        return $(this).children('th').length === 0
    }).remove();

    var users = c.getAllUsers(populate);


}

function assureCreateConfCommands(){
    //called on load

    $("#createCreateConfButton").click(function(){
        var parent = $(this).parent();
        var  confname = parent.children("#conferenceNameCreateConfModal").val();
        var startDate = parent.children("#conferenceStartDateCreateConfModal").val();
        var endDate  =  parent.children("#conferenceEndDateCreateConfModal").val();

        var abstractStartDate = parent.children("#callForAbstractStartDateCreateConfModal").val();
        var abstractEndDate = parent.children("#callForAbstractEndDateCreateConfModal").val();

        var proposalStartDate = parent.children("#callForProposalStartDateCreateConfModal").val();
        var proposalEndDate = parent.children("#callForProposalEndDateCreateConfModal").val();

        var biddingValue = parent.children("#biddingDeadlineCreateConfModal").val();
        console.log(confname, startDate, endDate, abstractEndDate, abstractStartDate, proposalEndDate, proposalStartDate, biddingValue);
        if(isEmpty(biddingValue) || isEmpty(confname) || isEmpty(startDate)
            || isEmpty(endDate)
            || isEmpty(abstractStartDate)
            || isEmpty(abstractEndDate)
            || isEmpty(proposalStartDate)
            || isEmpty(proposalEndDate)){
            showErrors(["You must insert values in all inputs!"]);
            return;
        }
        c.saveConference(new Conference(0, confname, new TimeSpan(startDate, endDate),
        new TimeSpan(abstractStartDate, abstractEndDate), new TimeSpan(proposalStartDate, proposalEndDate), biddingValue), function(){
            $("#myModalCreateConf").fadeOut();
        });
    });
}

function loadConference(){
    
}

function loadConferences(){

    function populate(conferences){
        function goToConference(e){
            e.preventDefault();

            var id = $(this).attr("href");
            document.cookie = "conferenceId = "+id.toString();
            window.location.href = "conference.html";
        }

        var confDiv = $("#conferinces-wrapper");
        confDiv.empty();
        for(var i in conferences){
            var conf = $("<div></div>");
            conf.text("Conference "+ conferences[i].id+ " " + conferences[i].name+ " ");
            confDiv.append(conf);
            var a = $("<a href='" + conferences[i].id + "'></a>");
            a.html("Go To");
            conf.append(a);

            a.click(goToConference);
        }
    }

    c.getAllConferences(populate);

}

function loadSystemPages(page){
    var menu = $("#menu ul");
    menu.empty();
    for(var spage in SYSTEM_PAGES){
        var menuItem = $("<li></li>");
        menuItem.html("<a href='"+PAGES[SYSTEM_PAGES[spage]]+"'>"+SYSTEM_PAGES[spage].toUpperCase() +"</a>");
        menu.append(menuItem);
    }

    for(var spop in SYSTEM_POPUPS){
        var menuItem = $("<li></li>");
        menuItem.html("<a href='"+ SYSTEM_POPUPS_ID[SYSTEM_POPUPS[spop]]+ "'>"+SYSTEM_POPUPS[spop].toUpperCase() +"</a>");
        menu.append(menuItem);
        $.get(PAGES[SYSTEM_POPUPS[spop]], function(data, status){
            if(status == "success")
            {
                var elem = $(data);
                $("body").append(elem);
                elem.find(".closeButton").click(closeButtonClick);
            }
            else{
                showErrors(["Can't load popup!"]);
            }
        });
        menuItem.click(showSystemPopp);
    }
}

function logout(){
    document.cookie = "username=;expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
}


function main(){
    $(function(){
        c = new Controller(showErrors, showWarnings);

        var pagename = location.pathname.split('/').reverse()[0];
        var page = PAGES_REVERSE[pagename];

        if(isUndefined(page) || WITHOUT_SESSION.indexOf(page) >=0 ){

        }
        else{
            //SESSION_PAGES
            c.loggedIn(function (logged, priviledge){
                if(!logged){
                    logout();
                    setTimeout(function(){
                        window.location.href = "index.html";
                    }, 2000);
                    return;
                }

                if(SYSTEM_PAGES.indexOf(page) > -1) loadSystemPages(page);
                else loadConferencePages();

                if(page in PAGE_LOADS){
                    PAGE_LOADS[page]();
                }

            });
        }

        errorsDiv = $("#errors");
        errorsDiv.click(function(){
            errorsDiv.fadeOut();
        });
        warningsDiv = $("#warnings");
        warningsDiv.click(function(){
            warningsDiv.fadeOut();
        });

        $("#loginButton").click(function(e){
            var user;
            clearMessages();
            e.preventDefault(true);
            try{
                user = getLoginCredidentials();
            }
            catch (c){
                return;
            }

            c.login(user.username, user.password, function(){
                alert(user.username);
                document.cookie  = "username =" + user.username + "; path=/";
                window.location.href = "conferences.html";
            });
        });

        $("#registerButton").click(function(e){
            var user;
            clearMessages();
            e.preventDefault(true);
            try{
                user = getRegisterData();
            }
            catch (e){
                return;
            }
            c.saveUser(user, function(){
                showWarnings(["Account created successfully!<br>You will be redirected to login.html!"] );
                setTimeout(function(){
                    window.location.href = "login.html";
                }, 2000);

            })
        });


        //todo 	--!!!! PLEASE USE THIS INSTEAD OF

    });
}

$.getScript("js/domain/entity.js", function(){
    $.getScript("js/service/controller.js", function(){
        $.getScript("js/util/utils.js", function(){
            main();
        })
    })
})
