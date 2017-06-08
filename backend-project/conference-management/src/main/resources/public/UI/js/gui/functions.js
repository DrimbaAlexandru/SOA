/**
 * Created by elisei on 07.06.2017.
 */

var errorsDiv, warningsDiv;

var c, data;

function formatPage(page){
    return page.substr(0, 1).toUpperCase() + page.substr(1).replace(/([A-Z])/g, function(a){
        return " "+a;
    })
}

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

function getVariableFromCookie(v, convertToInt = false){
    var cookie = document.cookie;
    var s = cookie.split(';');
    for(var i in s){
        var k, v;
        if(s[i].indexOf(v) > -1 )
        {
            if(convertToInt){
                try {
                    return parseInt(s[i].split('=')[1].trim());
                }
                catch (e){

                }
            }
            else{
                return s[i].split('=')[1].trim();
            }

        }
    }
    return undefined;
}

function getConferenceIdFromCookie(){
    return getVariableFromCookie("conferenceId", true);
}

function getUsernameFromCookie(){
    return getVariableFromCookie("username");
}

function tableClearLetHeader(table){
    table.find("tr").filter(function(){
        return $(this).children('th').length === 0
    }).remove();
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

function listToCommaSeparatedString(list){
    var keywords = "";
    for(var key in list){
        key = list[key];
        if(key.indexOf(";") > -1){
            key = key.replace(/;/g, function(a){
                return "\\;";
            })
        }
        keywords = keywords + key+";";
    }
    return keywords;
}

function commaSeparatedStringToList(string){
    var split = string.split(";");
    var toRemove = [];
    var i = 0;
    while(i < split.length){
        if(split[i][split[i].length - 1] == '\\' && i < split.length){
            split[i] = split[i].substr(0, split.length - 2) + split[i+1];
            split.remove(i+1, 1);
        }
        else{
            i++;
        }
    }
    return split;
}


function loadGrantReviewer(){
    function populate(users){
        function grantReviewer(e){
            var tds = $(this).parent().parent().children();
            var username = tds.get(0);
            c.getPriviledges(username, getConferenceIdFromCookie(), function(priviledge){
                priviledge.isReviewer = true;
                c.setPrivilegdes(username, getConferenceIdFromCookie(), priviledge, function(){

                });
            })  ;
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

            function putGrant(username, el){
                c.getPriviledges(username, getConferenceIdFromCookie(), function(priviledge){
                    if(priviledge.isReviewer){
                        el.html("<p>Already granted</p>")
                    }
                    else{
                        el.html("<button>Grant</button>");
                        el.children("button").click(grantReviewer);
                    }
                });
            }
            putGrant(users.username, td.clone());

            tr.append(td);
            table.append(tr);

        }
    }

    var table = $("#reviewerUsersTable");
    tableClearLetHeader(table);

    c.getAllUsers(populate);
}


function loadGrantChair(){
    function populate(users){

        function grantCoChair(e){
            var tds = $(this).parent().parent().children();
            var username = tds.get(0);
            c.getPriviledges(username, getConferenceIdFromCookie(), function(priviledge){
                priviledge.isCoChair= true;
                c.setPrivilegdes(username, getConferenceIdFromCookie(), priviledge, function(){

                });
            })  ;
        }

        function grantChair(e){
            var tds = $(this).parent().parent().children();
            var username = tds.get(0);
            c.getPriviledges(username, getConferenceIdFromCookie(), function(priviledge){
                priviledge.isChair = true;
                c.setPrivilegdes(username, getConferenceIdFromCookie(), priviledge, function(){

                });
            })  ;
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

            tr.append(td);

            function putGrant(username, el1, el2){
                c.getPriviledges(username, getConferenceIdFromCookie(), function(priviledge){
                    if(priviledge.isChair){
                        el1.html("<p>Already granted</p>")
                    }
                    else{
                        el1.html("<button>Grant</button>");
                        el1.children("button").click(grantChair);
                    }

                    if(priviledge.isCoChair){
                        el1.html("<p>Already granted</p>")
                    }
                    else{
                        el1.html("<button>Grant</button>");
                        el1.children("button").click(grantCoChair);
                    }

                });
            }

            var td1 = $("<td></td>");

            tr.append(td1);
            putGrant(users.username, td.clone(), td1.clone());

            table.append(tr);

        }
    }

    var table = $("#reviewerUsersTable");
    tableClearLetHeader(table);

    c.getAllUsers(populate);
}

function fillModalGrantSteering(){
    function populate(users){
        function grantSteering(e){
            var tds = $(this).parent().parent().children();
            var username = tds.get(0).innerHTML;
            var name = tds.get(1).innerHTML;
            var affiliation = tds.get(2).innerHTML;
            var website = tds.get(3).innerHTML;
            var email = tds.get(4).innerHTML;
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
    tableClearLetHeader(table);
    c.getAllUsers(populate);
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
    function populate(conference){
        $("#conferenceName").html(conference.name);
        $("#startDateSpan").html(conference.eventTimeSpan.startDate);
        $("#endDateSpan").html(conference.eventTimeSpan.endDate);
        $("#callForPapersStartDateSpan").html(conference.callForProposalsTimeSpan.startDate);
        $("#callForPapersEndDateSpan").html(conference.callForProposalsTimeSpan.endDate);
        $("#callForAbstractsStartDateSpan").html(conference.callForAbstractTimeSpan.startDate);
        $("#callForAbstractsEndDateSpan").html(conference.callForAbstractTimeSpan.endDate);
        $("#biddingDeadlineSpan").html(conference.biddingDeadline);

    }

    c.getOneConference(getConferenceIdFromCookie(), populate);
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



function loadAddPaper(){
    var fullPop = $("#myModalFull");
    var absPop = $("#myModalAbs");
    var editPop = $("#myModalEdit");
    var buttonAdd = $(".buttonAdd");
    var path;

    function clearEditPop(){

        editPop.find("#nameEditModal").val("");
        editPop.find("#keywordsEditModal").val("");
        editPop.find("#topicsEditModal").val("");
        editPop.find("#authorsEditModal").val("");
    }
    function submitEditPaperClick(e){
        e.preventDefault(true);
        var id = editPop.find("#idEditModal").val();
        var name = editPop.find("#nameEditModal").val();
        var keywords = editPop.find("#keywordsEditModal").val();
        var subjects = editPop.find("#topicsEditModal").val();
        var authors = editPop.find("#authorsEditModal").val();

        if(isEmpty(id)
            || isEmpty(name)
            || isEmpty(keywords)
            || isEmpty(subjects)
            || isEmpty(authors)){
            showErrors(["You must insert values on all fields!"]);
            return;
        }
        if(data == "save"){
            c.savePapers(getConferenceIdFromCookie(), new Proposal(id, name, keywords, subjects, authors),
                function(){window.location.reload();});

        }
        else if(data == "update"){
            c.updatePapers(getConferenceIdFromCookie(), new Proposal(id, name, keywords, subjects, authors),
                function(){window.location.reload();});
        }
    }

    function editClick(e){
        var parent = $(this).parent().parent();
        var id = parent.children().get(0).innerHTML;
        data = "update";
        var name = parent.children().get(1).innerHTML;
        var keywords = commaSeparatedStringToList(parent.children().get(2).innerHTML);
        var subjects = commaSeparatedStringToList(parent.children().get(3).innerHTML);
        var authors = commaSeparatedStringToList(parent.children().get(4).innerHTML);

        editPop.find("#idEditModal").val(id);
        editPop.find("#nameEditModal").val(name);
        editPop.find("#keywordsEditModal").val(keywords);
        editPop.find("#topicsEditModal").val(subjects);
        editPop.find("#authorsEditModal").val(authors);

        editPop.fadeIn();
    }

    function uploadAbsClick(e){
        e.preventDefault(true);
        var parent = $(this).parent().parent();
        var id = parent.children().get(0).innerHTML;
        absPop.find("#idAbsModal").val(id);
        absPop.fadeIn();
    }

    function uploadFullClick(e){
        e.preventDefault(true);
        var parent = $(this).parent().parent();
        var id = parent.children().get(0).innerHTML;
        fullPop.find("#idFullModal").val(id);
        fullPop.fadeIn();
    }


    function populate(papers){
        var table = $("#sentPapersTable");
        tableClearLetHeader(table);
        for(var i in papers){
            var tr = $("<tr></tr>");
            var td;

            td = $("<td></td>");
            td.html(papers[i].id);
            tr.append(td);

            td = $("<td></td>");
            td.html(papers[i].name);
            tr.append(td);

            var keywords = listToCommaSeparatedString(papers[i].keywords);
            var subjects= listToCommaSeparatedString(papers[i].subjects);
            var authors= listToCommaSeparatedString(papers[i].authors);

            td = $("<td></td>");
            td.html(keywords);
            tr.append(td);

            td = $("<td></td>");
            td.html(subjects);
            tr.append(td);

            td = $("<td></td>");
            td.html(authors);
            tr.append(td);

            td = $("<td></td>");
            td.html('<button class="editPaper">Edit</button>');
            td.children().first().click(editClick);
            tr.append(td);

            td = $("<td></td>");
            td.html('<button class="uploadAbs">Upload</button>');
            td.children().first().click(uploadAbsClick)
            tr.append(td);

            td = $("<td></td>");
            td.html('<button class="uploadFull">Upload</button>');
            td.children().first().click(uploadFullClick);
            tr.append(td);

            table.append(tr);
        }
    }

    c.getAllPapers(populate);


    $("#submitEditPaper").click(submitEditPaperClick);
    $("#submitAbsPaper").click(function(e){
        e.preventDefault(true);
        if(isUndefined(path ) || isEmpty(path)){
            showErrors(["You must select a file!"]);
            return;
        }
        var reader = new FileReader();
        var id = absPop.find("#idAbsModal").val();

        reader.onload = function(e){
            data = e.target.result;
            c.uploadAbsPaper(getConferenceIdFromCookie(), id,"type", data, function(){
                absPop.fadeOut();
            } );
        }
        reader.readAsText(path);
    });
    $("#submitFullPaper").click(function(e){
        e.preventDefault(true);
        if(isUndefined(path ) || isEmpty(path)){
            showErrors(["You must select a file!"]);
            return;
        }
        var reader = new FileReader();
        var id = fullPop.find("#idFullModal").val();

        reader.onload = function(e){
            data = e.target.result;
            c.uploadAbsPaper(getConferenceIdFromCookie(), id,"type", data, function(){
                fullPop.fadeOut();
            } );
        }
        reader.readAsText(path);
    });


    $("#absFileUpload").change(function(e){
        path = e.target.files[0];

    })

    $("#fullFileUpload").change(function(e){
        path = e.target.files[0];

    })

    buttonAdd.click(function(){
        data = "save";
        clearEditPop();
        editPop.fadeIn();
    });

}

function loadViewResult(){
    var reco = $("#myModalReco");

    function proposalClick(e){
        var id = $(this).children().get(0).innerHTML;

        function populateReviews(reviews){
            var table = reco.find("#recommandationsTable");
            tableClearLetHeader(table);

            for(var i in reviews){
                var tr = $("<tr></tr>");
                var td;

                td = $("<td></td>");
                td.html(reviews[i].status);
                tr.append(td);

                td = $("<td></td>");
                td.html(papers[i].justification);
                tr.append(td);

                table.append(tr);
            }
            reco.fadeIn();
        }

        c.getSubmittedPapersReviews(getUsernameFromCookie(), id, populateReviews);
    }

    function populate(papers){
        var table = $("#sentPapersTable");
        tableClearLetHeader(table);
        for(var i in papers){
            var tr = $("<tr></tr>");
            var td;

            td = $("<td></td>");
            td.html(papers[i].id);
            tr.append(td);

            td = $("<td></td>");
            td.html(papers[i].name);
            tr.append(td);

            var keywords = listToCommaSeparatedString(papers[i].keywords);
            var subjects= listToCommaSeparatedString(papers[i].subjects);
            var authors= listToCommaSeparatedString(papers[i].authors);

            td = $("<td></td>");
            td.html(keywords);
            tr.append(td);

            td = $("<td></td>");
            td.html(subjects);
            tr.append(td);

            td = $("<td></td>");
            td.html(authors);
            tr.append(td);

            table.append(tr);
            tr.click(proposalClick);

        }
    }

    c.getAcceptedSubmittedPapers(getUsernameFromCookie(), populate);
}

function loadDeadlines(){
    var pop = $("#myModalDeadlines");
    function deadlineClicked(e){
        e.preventDefault(true);
        showPopup("myModalDeadlines");
        var name = $(this).children().get(0).innerHTML;
        var date = $(this).children().get(1).innerHTML;
        pop.find("#nameDeadlinesModal").val(name);
        pop.find("#dateDeadlinesModal").val(date);
    }

    function saveDeadline(e) {
        e.preventDefault(true);
        var name = pop.find("#nameDeadlinesModal").val();
        var date = pop.find("#dateDeadlinesModal").val();
        if(isEmpty(name) || isEmpty(date)){
            showErrors(["You must provide a date!"]);
            return;
        }
        if(name.indexOf("biddingDeadline")> -1){
            data[name] = date;
        }
        else{
            data[name+"TimeSpan"].endDate = date;
        }

        c.saveConference(data, function(){
            pop.fadeOut();
            location.reload();
        });
    }

    function populate(conference){

        data = conference;
        var table = $("#deadlinesTable")
        tableClearLetHeader(table);

        for(var key in conference){
            if(key.indexOf("TimeSpan") > -1 || key.indexOf("biddingDeadline") >-1){
                var tr = $("<tr></tr>");
                var td;

                td = $("<td></td>");
                if(key.indexOf("TimeSpan") > -1 ){
                    td.html(key.replace("TimeSpan", ""));
                }
                else{
                    td.html(key);
                }
                tr.append(td);

                td = $("<td></td>");
                if(key.indexOf("TimeSpan") > -1 ){
                    td.html(conference[key].endDate);
                }
                else{
                    td.html(conference[key]);
                }

                tr.append(td);
                table.append(tr);
                tr.hover(function(){
                    $(this).addClass("selected");
                },
                function(){
                    $(this).removeClass("selected");
                })

                tr.click(deadlineClicked);
            }
        }
    }

    $("#submitDeadlinesModal").click(saveDeadline);
    c.getOneConference(getConferenceIdFromCookie(), populate);
}

function loadAsignPaper(){

    function loadPaper(tr, paperId){
        c.getOnePaper(paperId, function(paper){
            var td;

            td = $("<td></td>");
            td.html(paper.id);
            tr.append(td);

            td = $("<td></td>");
            td.html(paper.name);
            tr.append(td);

            td = $("<td></td>");
            td.html(listToCommaSeparatedString(paper.keywords));
            tr.append(td);


            td = $("<td></td>");
            td.html(listToCommaSeparatedString(paper.subjects));
            tr.append(td);

            td = $("<td></td>");
            td.html(listToCommaSeparatedString(paper.authors));
            tr.append(td);
        });
    }

    function colectSessions(sessions){
        for(var i in sessions){

            for(var j in sessions[i].schedule){
                var paperId = sessions[i].schedule[j].paperId;
                var tr = $("<tr></tr>");
                table.appendChild(tr);
                loadPaper(tr.clone(), paperId);
            }
        }
    }



    var table = $("#enrolledPapers");
    tableClearLetHeader(table);
    c.getConferenceSessions(getConferenceIdFromCookie(), colectSessions);
}

function loadBids(){



}

function loadReviews(){

}

function loadMangedConflicts(){
    
}


function loadSessionChairs(){
    var pop = $("#myModalSession");

    function sessionClick(e){
        var id = $(this).children().get(0).innerHTML;

        function setSessionClick(e){
            var username = $(this).parent().parent().get(2).innerHTML;

            c.setSessionChiar(id, username, function(){
                pop.fadeOut();
            });
        }

        function populateUsers(users){
            var table = $("#potetialSessionChairs");
            tableClearLetHeader(table);

            for(var i in users){
                var tr = $("<tr></tr>");
                var td;

                td = $("<td></td>");
                td.html(users[i].id);
                tr.append(td);

                td = $("<td></td>");
                td.html(users[i].username);
                tr.append(td);

                td = $("<td></td>");
                td.html(users[i].name);
                tr.append(td);

                td = $("<td></td>");
                td.html("<button>Set session</button>");
                tr.append(td);
                td.children().first().click(setSessionClick);
                table.append(tr);
            }

        }


        c.getPotentialChairs(id, populateUsers);
        pop.fadeIn();
    }

    function populate(sessions){
        var table = $("#sessionsTable");
        tableClearLetHeader(table);

        for(var i in sessions){
            var session = sessions[i];

            var tr = $("<tr></tr>");
            var td;

            td = $("<td></td>");
            td.html(session.id);
            tr.append(td);

            td = $("<td></td>");
            td.html(session.name);
            tr.append(td);

            td = $("<td></td>");
            var table1 = $("<table></table>");
            for(var j in session.schedule){
                var tr1 = $("<tr></tr>");

                function complete(tr1, paperId, schedule){
                    c.getOnePaper(paperId, function(paper){
                        var td1;

                        td1 = $("<td></td>");
                        td1.html(paper.id);
                        tr1.append(td1);

                        td1 = $("<td></td>");
                        td1.html(paper.name);
                        tr1.append(td1);


                        td1 = $("<td></td>");
                        td1.html(listToCommaSeparatedString(paper.keywords));
                        tr1.append(td1);


                        td1 = $("<td></td>");
                        td1.html(listToCommaSeparatedString(paper.subjects));
                        tr1.append(td1);


                        td1 = $("<td></td>");
                        td1.html(listToCommaSeparatedString(pape.authors));
                        tr1.append(td1);

                        td1 = $("<td></td>");
                        td1.html(schedule.presentationStartTime);
                        tr1.append(td1);


                        td1 = $("<td></td>");
                        td1.html(schedule.presentationEndTime);
                        tr1.append(td1);

                        td1 = $("<td></td>");
                        td1.html(schedule.username);
                        tr1.append(td1);

                    });
                }
                complete(tr.clone(), session.schedule[j].paperId, jQuery.extend(true, {}, session.schedule[j]));

                table1.append(tr1);

            }
            td.append(table1);
            tr.append(td);

            tr.click(sessionClick);
        }
    }


    c.getConferenceSessions(getConferenceIdFromCookie(), populate);
}

function loadSessions(){
    var pop = $("#myModalSection");

    function sessionClick(e){
        var id = $(this).children().get(0).innerHTML;
        pop.find("#idModalSection").val(id);
        pop.fadeIn();
    }

    function confirmClick(e){
        e.preventDefault(true);
        var answer = pop.find('input[name="answer"]:checked').val();
        var id = pop.find("#idModalSection").val();
        if(answer == 'yes'){
            c.participateSession(sessionId, getUsernameFromCookie(), function(){
                pop.fadeOut();
            });
            return;
        }
        pop.fadeOut();
    }

    function populate(sessions){
        var table = $("#sectionsTable");
        tableClearLetHeader(table);

        for(var i in sessions){
            var session = sessions[i];

            var tr = $("<tr></tr>");
            var td;

            td = $("<td></td>");
            td.html(session.id);
            tr.append(td);

            td = $("<td></td>");
            td.html(session.name);
            tr.append(td);

            td = $("<td></td>");
            var table1 = $("<table></table>");
            for(var j in session.schedule){
                var tr1 = $("<tr></tr>");

                function complete(tr1, paperId, schedule){
                    c.getOnePaper(paperId, function(paper){
                        var td1;

                        td1 = $("<td></td>");
                        td1.html(paper.id);
                        tr1.append(td1);

                        td1 = $("<td></td>");
                        td1.html(paper.name);
                        tr1.append(td1);


                        td1 = $("<td></td>");
                        td1.html(listToCommaSeparatedString(paper.keywords));
                        tr1.append(td1);


                        td1 = $("<td></td>");
                        td1.html(listToCommaSeparatedString(paper.subjects));
                        tr1.append(td1);


                        td1 = $("<td></td>");
                        td1.html(listToCommaSeparatedString(pape.authors));
                        tr1.append(td1);

                        td1 = $("<td></td>");
                        td1.html(schedule.presentationStartTime);
                        tr1.append(td1);


                        td1 = $("<td></td>");
                        td1.html(schedule.presentationEndTime);
                        tr1.append(td1);

                        td1 = $("<td></td>");
                        td1.html(schedule.username);
                        tr1.append(td1);

                    });
                }
                complete(tr.clone(), session.schedule[j].paperId, jQuery.extend(true, {}, session.schedule[j]));

                table1.append(tr1);

            }
            td.append(table1);
            tr.append(td);

            tr.click(sessionClick);
        }
    }

    $('#confirmSectionsButton').click(confirmClick);
    c.getConferenceSessions(getConferenceIdFromCookie(), populate);
}

function loadSystemPages(page, priviledge){
    var menu = $("#menu ul");
    menu.empty();

    var s = new Set();
    for(var i in SYSTEM_PAGES_FOR_USERS["all"]){
        s.add(SYSTEM_PAGES_FOR_USERS["all"][i])
    }

    if(priviledge.isCometeeMember){
        for(var i in SYSTEM_PAGES_FOR_USERS["steering"]){
            s.add(SYSTEM_PAGES_FOR_USERS["steering"][i])
        }
    }

    if(priviledge.isSuperUser){
        for(var i in SYSTEM_PAGES_FOR_USERS["master"]){
            s.add(SYSTEM_PAGES_FOR_USERS["master"][i])
        }
    }

    console.log(s);
    for(var spage in SYSTEM_PAGES){
        if(!(s.has(SYSTEM_PAGES[spage])))
            continue;

        var menuItem = $("<li></li>");
        menuItem.html("<a href='"+PAGES[SYSTEM_PAGES[spage]]+"'>"+formatPage(SYSTEM_PAGES[spage]) +"</a>");
        menu.append(menuItem);
    }

    for(var spop in SYSTEM_POPUPS){
        if(!(s.has(SYSTEM_POPUPS[spage])))
            continue;

        var menuItem = $("<li></li>");
        menuItem.html("<a href='"+ SYSTEM_POPUPS_ID[SYSTEM_POPUPS[spop]]+ "'>"+formatPage(SYSTEM_POPUPS[spop]) +"</a>");
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


function loadConferencePages(page, sysPrivilegde){
    function populate(priviledge){
        var s = new Set();
        for(var i in CONFERENCE_PAGES_FOR_USERS["all"]){
            s.add(CONFERENCE_PAGES_FOR_USERS["all"][i])
        }

        if(priviledge.isAuthor){
            for(var i in CONFERENCE_PAGES_FOR_USERS["author"]){
                s.add(CONFERENCE_PAGES_FOR_USERS["author"][i])
            }
        }

        if(priviledge.isChair){
            for(var i in CONFERENCE_PAGES_FOR_USERS["chair"]){
                s.add(CONFERENCE_PAGES_FOR_USERS["chair"][i])
            }
        }

        if(priviledge.isCoChair){
            for(var i in CONFERENCE_PAGES_FOR_USERS["cochair"]){
                s.add(CONFERENCE_PAGES_FOR_USERS["cochair"][i])
            }
        }

        if(priviledge.isPCMember){
            for(var i in CONFERENCE_PAGES_FOR_USERS["reviewer"]){
                s.add(CONFERENCE_PAGES_FOR_USERS["reviewer"][i])
            }
        }

        if(sysPrivilegde.isCometeeMember){
            for(var i in CONFERENCE_PAGES_FOR_USERS["steering"]){
                s.add(CONFERENCE_PAGES_FOR_USERS["steering"][i])
            }
        }

        if(sysPrivilegde.isSuperUser){
            for(var i in CONFERENCE_PAGES_FOR_USERS["master"]){
                s.add(CONFERENCE_PAGES_FOR_USERS["master"][i])
            }
        }

        for(var page of s){
            var menuItem = $("<li></li>");
            menuItem.html("<a href='"+PAGES[page]+"'>"+formatPage(page) +"</a>");
            menu.append(menuItem);
        }
    }
    var menu = $("#menu ul");
    menu.empty();

    c.getPriviledges(getUsernameFromCookie(), getConferenceIdFromCookie(), populate);
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

                if(SYSTEM_PAGES.indexOf(page) > -1) loadSystemPages(page, priviledge);
                else loadConferencePages(page, priviledge);

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
                //document.cookie  = "username =" + user.username + "; path=/";
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

        $(".closeButton").click(closeButtonClick);



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
