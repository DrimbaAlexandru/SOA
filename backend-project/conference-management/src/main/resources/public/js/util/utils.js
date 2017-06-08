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
    "index":"index.html",
    "login":"login.html",
    "register":"register.html",

    "conferences":"conferences.html",
    "conference":"conference.html",

    "bid":"bidProposals.html",
    "review":"reviewProposals.html",

    "asignPaper":"asignPapersToReviewers.html",
    "manageConflicts":"manageConflictProposals.html",
    "sessionChairs":"sessionChairs.html",
    "deadlines":"manageDeadlines.html",

    "addPaper":"autorManageProposals.html",
    "viewResult":"autorProposalsResults.html",

    "sessions":"sessions.html",

    "grantReviewer":"grantReviewer.html",
    "grantChair":"grantReviewer.html",

    "createConference":"createConf.html",
    "grantSteering":"grantSteering.html",
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
        ["bid", "review", "asignPaper", "manageConflicts", "sessionChairs", "deadlines"],
    "chair":
        ["bid", "review", "asignPaper", "manageConflicts", "sessionChairs", "deadlines"],
    "reviewer":
        ["bid", "review"],
    "author":
        ["addPaper", "viewResult"],
    "steering":[],
    "master":[],
    "all":["sessions", "conference"]
}

SYSTEM_PAGES_FOR_USERS = {
    "master":
        ["grantSteering"],
    "steering":
        ["createConference"]
}

PAGE_LOADS = {
    "conferences":loadConferences,
    "conference":loadConference,
    "deadlines":loadDeadlines,
    "addPaper":loadAddPaper,
}