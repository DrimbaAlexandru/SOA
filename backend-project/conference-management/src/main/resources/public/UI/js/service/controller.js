/**
 * Created by elisei on 15.05.2017.
 * !!! DON'T FORGET TO IMPORT utils
 */

function isUndefined(v){
    if(typeof v !== "undefined")
    {
        return false;
    }
    return true;
}

function Controller(onError= undefined, onWarning = undefined) {

    this.onError = onError;
    this.onWarning = onWarning;
    const applicationJSON = "application/json";
    const HOST = "/";
    const COMMANDS = {
        "users_login": "users/login",//"users/users_login",
        "users_logout": "users/logout",//"users/logout",
        "users_loggedIn": "users/loggedIn",//"users/loggedIn",
        "users_getPriviledges": "users/{0}/privileges?conferenceId={1}",//"users/priviledges?conferenceId={0}",
        "users_setPrivilegdes": "users/{0}/{1}",
        "users_getAll": "users",
        "users_getOne": "users/{0}",
        "users_save": "users",
        "users_update": "users/{0}",
        "users_getSubmittedPapers": "users/{0}/submittedPapers",
        "users_getAcceptedSubmittedPapers": "users/{0}/submittedPapers?status=accepted/decline",
        "users_getSubmittedPapersReviews": "users/{0}/submittedPapers/{1}/reviews",
        "users_setSubmittedPapersPresetation": "users/{0}/submittedPapers/{1}/presetation",
        "users_getPaperBid": "users/{0}/bids/{1}",
        "users_setPaperBid": "users/{0}/bids/{1}",
        "users_getAssignedForReview": "users/{0}/asignedForReview",
        "users_getPaperReview": "users/{0}/reviews/{1}",
        "users_setPaperReview": "users/{0}/reviews/{1}",
        "users_setPaperAssignement": "users/{0}/assignedPapers/{1}",

        "papers_getId":"papers/{0}",//todo to be implemented
        "papers_getAll": "papers",
        "papers_save": "papers",
        "papers_update": "papers/{0}",
        "papers_uploadFull": "papers/{0}/full",
        "papers_getFull": "papers/{0}/full",
        "papers_uploadAbs": "papers/{0}/abstract",
        "papers_getAbs": "papers/{0}/abstract",

        "papers_getAllAccepted": "papers/acceptedProposals",
        "papers_getPotentialReviewers": "papers/{0}/potentialReviewers",
        "papers_getAssignedReviewers": "papers/{0}/assignedReviewers",
        "papers_reevaluatePaper": "papers/{0}/reevaluate",
        "papers_finalEvaluator": "papers/{0}/finalEvaluator/{1}",
        "papers_finalStatus": "papers/{0}/finalStatus",

        "reviews_getOthersReviews": "papers/{0}/reviews",
        "papers_bids": "papers/{0}/bid",

        //SESSIONS
        "conferences_save": "conferences",
        "conferences_getAll": "conferences",
        "conferences_getOne": "conferences/{0}",
        "conferences_update": "conferences/{0}",

        "conferences_getSessions": "conferences/{0}/sessions",

        "sessions_getOne": "sessions/{0}",
        "sessions_save": "sessions",
        "sessions_getPotentialChairs": "conferences/{0}/PCMembers",
        "sessions_setSessionChiar": "sessions/{0}/sessionChair",
        "sessions_participate": "sessions/{0}/participate"
    };


    function typeOf(object) {
        return Object.prototype.toString.call(object);
    }

    function isArray(object) {
        return object.constructor === Array;
    }

    function errorFunction(result) {
        onError(["Error receiving response:\n" + result.responseText]);
    }

    function hasError(result) {
        if ("errors" in result && isArray(result["errors"]) &&
            result["errors"].length > 0) {
            onError(result["errors"]);
            return true;
        }
        return false;
    }


    function hasWarning(result) {
        if ("warnings" in result && isArray(result["warnings"])
            && result["warnings"].length > 0) {
            onWarning(result["warnings"]);
            return true;
        }
        return false;
    }

    function hasNoResp(result) {
        if (!("resp" in result)) {
            onError(["Invalid response: field 'resp' not found!"]);
            return true;
        }
        return false;
    }

    function validateAndRun(result, run) {
        try {
            console.log(typeOf(result));
            if(typeOf(result) === "[object String]")
                result = JSON.parse(result);
        }
        catch (e) {
            console.log(e);
            onError(["Invalid JSON received!"]);
            return;
        }

        if (hasError(result)) return;
        hasWarning(result);
        if (hasNoResp(result)) return;

        if (run) run(result['resp']);
    }

    /**
     * add a user
     * @param username
     * @param password
     * @param onResultArrived
     */
    this.login = function (username, password, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_login"],
                data: JSON.stringify({
                    username: username,
                    password: password
                }),
                type: "POST",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }

    this.logout = function (onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_logout"],
                //data: JSON.stringify({}),
                type: "POST",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }

    this.loggedIn = function (onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_loggedIn"],
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    try {
                        console.log(typeOf(result));
                        if(typeOf(result) === "[object String]")
                            result = JSON.parse(result);
                    }
                    catch (e) {
                        onError(["Invalid JSON received!"]);
                        return;
                    }

                    if (hasError(result)){
                        onResultArrived(false, undefined);
                        return;
                    }
                    hasWarning(result);
                    if (hasNoResp(result)){
                        onResultArrived(false, undefined);
                        return;
                    }

                    var response = result["resp"];
                    var isSuperUser = response.isSuperUser;
                    var isCommiteeMember = response.isCometeeMember;
                    onResultArrived(true, new SystemPriviledge(isSuperUser, isCommiteeMember))

                },
                error: errorFunction
            }
        )
    }

    this.getPriviledges = function (username, conferinceId, onResultArrived) {
        $.ajax(
            {
                url: (HOST + COMMANDS["users_getPriviledges"]).format(username, conferinceId),
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var isAuthor = response.isAuthor;
                        var isPCMember = response.isPCMember;
                        var isChair = response.isChair;
                        var isCoChair = response.isCoChair;
                        if (onResultArrived) onResultArrived(new Priviledge(isAuthor, isPCMember, isChair, isCoChair));
                    });
                },
                errorFunction: errorFunction

            }
        )

    }

    this.getAllUsers = function (onResultArrived) {
        $.ajax(
            {
                url: (HOST + COMMANDS["users_getAll"]),
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var users = [];
                        for(var i in response){
                            var username = response[i].username;
                            var name = response[i].name;
                            var affiliation = response[i].affiliation;
                            var email = response[i].email;
                            var website = response[i].website;
                            var isSuperUser = response[i].isSuperUser;
                            var isCommiteeMember = response[i].isCometeeMember;
                            users.push(new User(0, username, name, email, affiliation, website, "", isSuperUser, isCommiteeMember));
                        }
                        if (onResultArrived) onResultArrived(users);
                    });
                },
                errorFunction: errorFunction
            }
        )
    }

    this.getOneUser = function (username, onResultArrived) {
        $.ajax(
            {
                url: (HOST + COMMANDS["users_getOne"]).format(username),
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var username = response.username;
                        var name = response.name;
                        var affiliation = response.affiliation;
                        var email = response.email;
                        var website = response.website;
                        var isSuperUser = response.isSuperUser;
                        var isCommiteeMember = response.isCometeeMember;
                        if (onResultArrived) onResultArrived(new User(0, username, name, affiliation, email, website, "", isSuperUser, isCommiteeMember));
                    });
                },
                errorFunction: errorFunction
            }
        )
    }

    this.saveUser = function (user, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_save"],
                data: JSON.stringify({
                    username: user.username,
                    name: user.name,
                    affiliation: user.affiliation,
                    email: user.email,
                    website: user.website,
                    password: user.password
                }),
                type: "POST",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }

    this.updateUser = function (user, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_update"].format(user.username),
                data: JSON.stringify({
                    username: user.username,
                    name: user.name,
                    affiliation: user.affiliation,
                    email: user.email,
                    website: user.website,
                    password: user.password,
                    isCometeeMember: user.isCometeeMember
                }),
                type: "PUT",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }

    this.getSubmittedPapers = function (username, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_getSubmittedPapers"].format(username),
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var papers = [];
                        for(var i in response){

                            var id = response[i].id;
                            var name = response[i].name;
                            var subjects = response[i].subjects;
                            var keywords = response[i].keywords;
                            var authors = response[i].authors;
                            papers.push(new Proposal(id, name, keywords, subjects, authors));
                        }

                        if (onResultArrived) onResultArrived(papers)
                    });
                },
                error: errorFunction
            }
        )
    }

    this.getAcceptedSubmittedPapers = function (username, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_getAcceptedSubmittedPapers"].format(username),
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var papers = [];
                        for(var i in response){
                            var id = response[i].id;
                            var name = response[i].name;
                            var subjects = response[i].subjects;
                            var keywords = response[i].keywords;
                            var authors = response[i].authors;
                            papers.push(new Proposal(id, name, keywords, subjects, authors))
                        }

                        if (onResultArrived) onResultArrived(papers)
                    });
                },
                error: errorFunction
            }
        )
    }

    this.getSubmittedPapersReviews = function (username, paperId, onResultArrived) {
        $.ajax(
            {
                url: (HOST + COMMANDS["users_getSubmittedPapersReviews"]).format(username, paperId),
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var reviews = [];
                        for(var i in response){
                            var status = response[i].status;
                            var justification = response[i].justification;
                            reviews.push(new Review(status, justification));
                        }
                        if (onResultArrived) onResultArrived(reviews)
                    });
                },
                error: errorFunction
            }
        )
    }

    this.setSubmittedPapersPresetation = function (username, paperId, data, type, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_setSubmittedPapersPresetation"].format(username, paperId),
                data: JSON.stringify({
                    data: data,
                    type: type
                }),
                type: "POST",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }

    this.getPaperBid = function (username, paperId, onResultArrived) {
        $.ajax(
            {
                url: (HOST + COMMANDS["users_getPaperBid"]).format(username, paperId),
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var status = response.status;
                        if (onResultArrived) onResultArrived(BidType[status])
                    });
                },
                error: errorFunction
            }
        )
    }

    this.setPaperBid = function (username, paperId, status, onResultArrived) {
        $.ajax(
            {
                url: (HOST + COMMANDS["users_setPaperBid"]).format(username, paperId),
                data: JSON.stringify({
                    status: status
                }),
                type: "PUT",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }

    this.getAssignedForReview = function (username, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_getAssignedForReview"].format(username),
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var papers = [];
                        for(var i in response)
                        {
                            var id = response[i].id;
                            var name = response[i].name;
                            var subjects = response[i].subjects;
                            var keywords = response[i].keywords;
                            var authors = response[i].authors;
                            papers.push(new Proposal(id, name, keywords, subjects, authors));
                        }
                        if (onResultArrived) onResultArrived(papers)
                    });
                },
                error: errorFunction
            }
        )
    }

    this.getPaperReview = function (paperId, onResultArrived) {
        $.ajax(
            {
                url: (HOST + COMMANDS["users_getPaperReview"]).format(username, paperId),
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var status = ReviewType[response.status];
                        var justification = response.justification;
                        if (onResultArrived) onResultArrived(new Review(status, justification))
                    });
                },
                error: errorFunction
            }
        )
    }

    this.setPaperReview = function (paperId, review, onResultArrived) {
        $.ajax(
            {
                url: (HOST + COMMANDS["users_setPaperReview"]).format(username, paperId),
                data: JSON.stringify({
                    status: review.status,
                    justification: review.justification,
                }),
                type: "PUT",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }

    this.setPrivilegdes = function (user, conferenceId, priviledge, onResultArrived) {
        var p = {};
        if(!isUndefined(priviledge.isPCMember) ) p.isPCMember = priviledge.isPCMember;
        if(!isUndefined(priviledge.isChair)) p.isChair = priviledge.isChair;
        if(!isUndefined(priviledge.isCoChair)) p.isCoChair = priviledge.isCoChair;

        $.ajax(
            {
                url: HOST + COMMANDS["users_setPrivilegdes"].format(user.username, conferenceId),
                data: JSON.stringify(p),
                type: "PUT",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }

    this.setPaperAssignement = function (username, paperId, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_setPaperAssignement"].format(username, paperId),
                //data: JSON.stringify({}),
                type: "PUT",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }

    this.getOnePaper = function (paperId, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_getId"].format(paperId),
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {

                        var id = response.id;
                        var name = response.name;
                        var subjects = response.subjects;
                        var keywords = response.keywords;
                        var authors = response.authors;
                        var paper = new Proposal(id, name, keywords, subjects, authors);

                        if (onResultArrived) onResultArrived(paper)
                    });
                },
                error: errorFunction
            }
        )
    }

    this.getAllPapers = function (onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_getAll"],
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {

                        var papers = [];
                        for(var i in response) {
                            var id = response[i].id;
                            var name = response[i].name;
                            var subjects = response[i].subjects;
                            var keywords = response[i].keywords;
                            var authors = response[i].authors;
                            papers.push(new Proposal(id, name, keywords, subjects, authors));
                        }
                        if (onResultArrived) onResultArrived(papers)
                    });
                },
                error: errorFunction
            }
        )
    }

    this.savePapers = function (conferenceId, paper, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_save"],
                data: JSON.stringify({
                     name : paper.name,
                     subjects : paper.subjects,
                     keywords : paper.keywords,
                     authors : paper.authors,
                    conferenceId:conferenceId
                }),
                type: "POST",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }

    this.updatePapers = function (conferenceId, paper,  onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_update"].format(paper.paperId),
                data: JSON.stringify({
                    name: paper.name,
                    subjects: paper.subjects,
                    keywords: paper.keywords,
                    authors: paper.authors
                }),
                type: "PUT",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }

    this.uploadFullPaper = function (conferenceId, paperId, type, data, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_uploadFull"].format(paperId),
                data: JSON.stringify({
                    type: type,
                    data : data
                }),
                type: "POST",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }

    this.getFullPaper =  function (conferenceId, paperId, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_getFull"].format(paperId),
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var RAW_DATA = response;
                        if (onResultArrived) onResultArrived(RAW_DATA)
                    });
                },
                error: errorFunction
            }
        )
    }

    this.uploadAbsPaper = function (conferenceId, paperId, type, data, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_uploadAbs"].format(paperId),
                data: JSON.stringify({
                    type: type,
                    data : data
                }),
                type: "POST",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }

    this.getAbsPapers =  function (conferenceId, paperId, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_getAbs"].format(paperId),
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var RAW_DATA = response;
                        if (onResultArrived) onResultArrived(RAW_DATA)
                    });
                },
                error: errorFunction
            }
        )
    }

    this.getAllAcceptedPapers = function (conferenceId, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_getAllAccepted"],
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var papers = [];
                        for(var i in response)
                        {
                            var id = response[i].id;
                            var name = response[i].name;
                            var subjects = response[i].subjects;
                            var keywords = response[i].keywords;
                            var authors = response[i].authors;
                            papers.push(new Proposal(id, name, keywords, subjects, authors));
                        }
                        if (onResultArrived) onResultArrived(papers)
                    });
                },
                error: errorFunction
            }
        )
    }

    this.getPotentialReviewers = function (conferenceId, paperId, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_getPotentialReviewers"].format(paperId),
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var users = [];
                        for(var i in response){
                            var username = response.username;
                            users.push(new User(0, username, "", "", "", ""));
                        }
                        if (onResultArrived) onResultArrived(users)
                    });
                },
                error: errorFunction
            }
        )
    }

    this.getAssignedReviewers = function (conferenceId, paperId, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_getAssignedReviewers"].format(paperId),
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var users = [];
                        for(var i in response){
                            var username = response.username;
                            users.push(new User(0, username, "", "", "", ""));
                        }
                        if (onResultArrived) onResultArrived(users)
                    });
                },
                error: errorFunction
            }
        )
    }
    //todo trebuie sa faca sa imi iau lucrarile din conferinta

    this.setReevaluatePaper = function (conferenceId, paperId,   onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_reevaluatePaper"].format(paperId),
                //data: JSON.stringify({}),
                type: "PUT",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }

    this.setFinalEvaluator = function (conferenceId, paperId, userid,  onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_finalEvaluator"].format(paperId, userid),
                //data: JSON.stringify({}),
                type: "PUT",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }
/*
    this.setfinalStatus = function (conferenceId, paperId, status, justification, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_finalStatus"].format(conferenceId, paperId),
                data: JSON.stringify({
                    status: status,
                    justification: justification
                }),
                type: "PUT",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }
*/
    this.getOthersReviews = function (paperId, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_getAssignedReviewers"].format(paperId),
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var reviews = [];
                        for(var i= 0 ; i<response.length;i++){
                            var status = response[i].status;
                            var justification = response[i].justification;
                            var username = response[i].username;
                            reviews.push(new PaperReview(0, new Review(status, justification),
                                new User(0, username, "", "", "", "", "", false, false)));
                        }

                        if (onResultArrived) onResultArrived(reviews);
                    });
                },
                error: errorFunction
            }
        )
    }


    this.getAllConferences = function (onResultArrived) {
        $.ajax(
            {
                url: (HOST + COMMANDS["conferences_getAll"]),
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var conferences = [];
                        for(var i in response)
                        {
                            var id = response[i].id;
                            var name = response[i].name;
                            var startDateTimeSpan = response[i].eventTimeSpan.startDate;
                            var endDateTimeSpan = response[i].eventTimeSpan.endDate;
                            var startDateAbstract = response[i].callForAbstractTimeSpan.startDate;
                            var endDateAbstract = response[i].callForAbstractTimeSpan.endDate;
                            var startDateProposal = response[i].callForProposalsTimeSpan.startDate;
                            var endDateProposal = response[i].callForProposalsTimeSpan.endDate;
                            var biddingDeadline = response[i].biddingDeadline;
                            conferences.push(new Conference(id, name, new TimeSpan(startDateTimeSpan, endDateTimeSpan), new TimeSpan(startDateAbstract, endDateAbstract), new TimeSpan(startDateProposal, endDateProposal), biddingDeadline))
                        }
                        if (onResultArrived) onResultArrived(conferences);
                    });
                },
                errorFunction: errorFunction
            }
        )
    }

    this.getOneConference = function (conferenceId, onResultArrived) {
        $.ajax(
            {
                url: (HOST + COMMANDS["conferences_getOne"].format(conferenceId)),
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var name = response.name;
                        var startDateTimeSpan = response.eventTimeSpan.startDate;
                        var endDateTimeSpan = response.eventTimeSpan.endDate;
                        var startDateAbstract = response.callForAbstractTimeSpan.startDate;
                        var endDateAbstract = response.callForAbstractTimeSpan.endDate;
                        var startDateProposal = response.callForProposalsTimeSpan.startDate;
                        var endDateProposal = response.callForProposalsTimeSpan.endDate;
                        var biddingDeadline = response.biddingDeadline;
                        if (onResultArrived) onResultArrived(new Conference(conferenceId, name, new TimeSpan(startDateTimeSpan, endDateTimeSpan), new TimeSpan(startDateAbstract, endDateAbstract), new TimeSpan(startDateProposal, endDateProposal), biddingDeadline));
                    });
                },
                errorFunction: errorFunction
            }
        )
    }

    this.saveConference = function (conference, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["conferences_save"],
                data: JSON.stringify({
                    name: conference.name,
                    eventTimeSpan: {
                        startDate:conference.eventTimeSpan.startDate,
                        endDate:conference.eventTimeSpan.endDate,
                    },
                    callForAbstractTimeSpan:{
                        startDate: conference.callForAbstractTimeSpan.startDate,
                        endDate:conference.callForAbstractTimeSpan.endDate,
                    },
                    callForProposalsTimeSpan:{
                        startDate: conference.callForProposalsTimeSpan.startDate,
                        endDate:conference.callForProposalsTimeSpan.endDate,
                    },
                    biddingDeadline: conference.biddingDeadline
                }),
                type: "POST",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }

    this.updateConference = function (conference, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["conferences_update"].format(conference.id),
                data: JSON.stringify({
                    eventTimeSpan: {
                        startDate:conference.eventTimeSpan.startDate,
                        endDate:conference.eventTimeSpan.endDate,
                    },
                    callForAbstractTimeSpan:{
                        startDate: conference.callForAbstractTimeSpan.startDate,
                        endDate:conference.callForAbstractTimeSpan.endDate,
                    },
                    callForProposalsTimeSpan:{
                        startDate: conference.callForProposalsTimeSpan.startDate,
                        endDate:conference.callForProposalsTimeSpan.endDate,
                    },
                    biddingDeadline: conference.biddingDeadline
                }),
                type: "PUT",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }


    this.getConferenceSessions = function (conferenceId ,onResultArrived) {
        $.ajax(
            {
                url: (HOST + COMMANDS["conferences_getSessions"]).format(conferenceId),
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var sessions = [];
                        for(var i in response){
                            var id = response[i].id;
                            var name = response[i].name;
                            var schedule = [];
                            for(var j in response[i].schedule){
                                var sessionId = response[i].schedule[j].sessionId;
                                var paperId = response[i].schedule[j].paperId;
                                var presentationStartTime = response[i].schedule[j].presentationStartTime;
                                var presentationEndTime = response[i].schedule[j].presentationEndTime;
                                var username = response[i].schedule[j].username;
                                schedule.push(new ScheduleItem(sessionId, paperId, presentationStartTime, presentationStartTime, username));
                            }
                            sessions.push(new Section(id, name, schedule));
                        }
                        if (onResultArrived) onResultArrived(sessions);
                    });
                },
                errorFunction: errorFunction
            }
        )
    }

    this.getOneSessions = function (sessionId, onResultArrived) {
        $.ajax(
            {
                url: (HOST + COMMANDS["sessions_getOne"]).format(sessionId),
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var id = response.id;
                        var name = response.name;
                        var sessionId = response.schedule.sessionId;
                        var paperId = response.schedule.paperId;
                        var presentationStartTime = response.schedule.presentationStartTime;
                        var presentationEndTime = response.schedule.presentationEndTime;
                        var username = response.schedule.username;
                        if (onResultArrived) onResultArrived(new Section(id, name, [new ScheduleItem(sessionId, paperId, presentationStartTime, presentationEndTime, username)]));
                    });
                },
                errorFunction: errorFunction
            }
        )
    }

    this.saveSession = function (session, onResultArrived) {
        var sessionJSON  = {
            name : session.name,
            sessionId : session.schedule.sessionId,
            schedule :[]
        };
        for(var i in session.schedule){
            sessionJSON.schedule.push({
                sessionId:session.schedule[i].sessionId,
                paperId:session.schedule[i].paperId,
                presentationStartTime: session.schedule[i].presentationStartTime,
                presentationEndTime: session.schedule[i].presentationEndTime,
                username:session.schedule[i].username
            })
        }
        $.ajax(
            {
                url: HOST + COMMANDS["sessions_save"],
                data: JSON.stringify(sessionJSON),
                type: "POST",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }


    this.getPotentialChairs = function (sessionId, onResultArrived) {
        $.ajax(
            {
                url: (HOST + COMMANDS["sessions_getPotentialChairs"]).format(sessionId),
                //data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var users = [];
                        for(var i in response)
                        {
                            var id = response.id;
                            var username = response.username;
                            var name = response.name;
                            var affiliation = response.affiliation;
                            var email = response.email;
                            var website = response.website;
                            users.push(new User(id, username, name, email, affiliation, website, "", undefined, undefined));
                        }
                        if (onResultArrived) onResultArrived(new User(id, username, name, affiliation, email, website, "", "", ""));
                    });
                },
                errorFunction: errorFunction
            }
        )
    }

    this.setSessionChiar = function (sessionId,sessionChairUsername,  onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["sessions_setSessionChiar"].format(sessionId),
                data: JSON.stringify({
                    username : sessionChairUsername
                }),
                type: "PUT",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }

    this.participateSession = function (sessionId,username,  onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["sessions_participate"].format(sessionId),
                data: JSON.stringify({username:username}),
                type: "POST",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        if (onResultArrived) onResultArrived();
                    });
                },
                error: errorFunction
            }
        )
    }

}
