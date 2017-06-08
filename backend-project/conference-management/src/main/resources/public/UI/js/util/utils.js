/**
 * Created by elisei on 05.06.2017.
 */


String.prototype.format = function () {
    var args = [].slice.call(arguments);
    return this.replace(/(\{\d+\})/g, function (a){
        return args[+(a.substr(1,a.length-2))||0];
    });
};

PAGES = {
    "index":"index.html",//DONE
    "login":"login.html",//DONE
    "register":"register.html",//DONE

    "conferences":"conferences.html",//DONE
    "conference":"conference.html",//DONE

    "bid":"bidProposals.html",
    "review":"reviewProposals.html",

    "asignPaper":"asignPapersToReviewers.html",
    "manageConflicts":"manageConflictProposals.html",
    "sessionChairs":"sessionChairs.html",//DONE
    "deadlines":"manageDeadlines.html",//DONE

    "addPaper":"autorManageProposals.html",//DONE
    "viewResult":"autorProposalsResults.html",//DONE

    "sessions":"sessions.html",//DONE

    "grantReviewer":"grantReviewer.html",//DONE
    "grantChair":"grantReviewer.html",//DONE

    "createConference":"createConf.html",//DONE
    "grantSteering":"grantSteering.html",//DONE
};

PAGES_REVERSE ={};

for(var key in PAGES){
    PAGES_REVERSE[PAGES[key]] = key;
}

WITHOUT_SESSION = ["index", "login", "register"]

SYSTEM_PAGES = ["conferences"]
SYSTEM_POPUPS = ["grantSteering", "createConference"];
SYSTEM_POPUPS_ID = {
    "grantSteering":"myModalGrantSteering",
    "createConference":"myModalCreateConf"
}

CONFERENCE_PAGES_FOR_USERS = {
    "cochair":
        ["bid", "review", "asignPaper", "manageConflicts", "sessionChairs", "deadlines", "grantReviewer"],
    "chair":
        ["bid", "review", "asignPaper", "manageConflicts", "sessionChairs", "deadlines", "grantReviewer"],
    "reviewer":
        ["bid", "review"],
    "author":
        ["addPaper", "viewResult"],
    "steering":["grantChair"],
    "master":[],
    "all":["sessions", "conference"]
}

SYSTEM_PAGES_FOR_USERS = {
    "master":
        ["grantSteering"],
    "steering":
        ["createConference"],
    "all":
        ["conferences"]

}

PAGE_LOADS = {
    "conferences":loadConferences,
    "conference":loadConference,
    "deadlines":loadDeadlines,
    "addPaper":loadAddPaper,
    "viewResult":loadViewResult,

    "manageConflicts":loadMangedConflicts,
    "asignPaper":loadAsignPaper,
    "bid":loadBids,
    "review":loadReviews,


    "grantReviewer":loadGrantReviewer,
    "grantChair":loadGrantChair,

    "sessions":loadSessions,
    "sessionChairs":loadSessionChairs,
}