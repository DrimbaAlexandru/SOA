/**
 * Created by elisei on 15.05.2017.
 * !!! DON'T FORGET TO IMPORT utils
 */


function Controller(onError= undefined, onWarning = undefined) {

    this.onError = onError;
    this.onWarning = onWarning;
    const applicationJSON = "application/json";
    const HOST = "";
    const COMMANDS = {
        "users_login": "users.login.php",//"users/users_login",
        "users_logout": "users.logout.php",//"users/logout",
        "users_loggedIn": "users.loggedIn.php",//"users/loggedIn",
        "users_getMyPriviledges": "users.priviledges.php?conferenceId={0}",//"users/priviledges?conferenceId={0}",
        "users_getAll": "users.php",
        "users_getOne": "users.php?username={0}",
        "users_save": "users.php",
        "users_update": "users.php?username={0}",
        //parametrul username va fi luat din sesiune
        "users_getSubmittedPapers": "users.submittedPapers.php",
        "users_getAcceptedSubmittedPapers": "users.submittedPapers.php?status=accepted",
        "users_getSubmittedPapersReviews": "users.submittedPapers.reviews.php?idPaper={0}",
        "users_setSubmittedPapersPresetation": "users.submittedPapers.presentation.php?idPaper={0}",
        "users_getPaperBid": "users.bids.php?paperId={0}",
        "users_setPaperBid": "users.bids.php?paperId={0}",
        "users_getAssignedForReview": "users.asignedForReview.php",
        "users_getPaperReview": "users.reviews.php?paperId={0}",
        "users_setPaperReview": "users.reviews.php?paperId={0}",
        //parametrul username e explicit
        "users_setPrivilegdes": "users.php?username={0}&conferenceId={1}",
        "users_setPaperAssignement": "users.assignedPapers.php?username={0}&paperId={1}",

        "papers_getAll": "papers.php?conferenceId={0}",
        "papers_save": "papers.php?conferenceId={0)",
        "papers_update": "papers.php?conferenceId={0}&paperId={1}",
        "papers_uploadFull": "papers.full.php?conferenceId={0}&paperId={1}",
        "papers_getFull": "papers.full.php?conferenceId={0}&paperId={1}",
        "papers_uploadAbs": "papers.abs.php?conferenceId={0}&paperId={1}",
        "papers_getAbs": "papers.abs.php?conferenceId={0}&paperId={1}",

        "papers_getAllAccepted": "papers.acceptedProposals.php?conferenceId={0}",
        "papers_getPotentialReviewers": "papers.potentialReviewers.php?conferenceId={0}&paperId={1}",
        "papers_getAssignedReviewers": "papers.assignedReviewers.php?conferenceId={0}&paperId={1}",
        "papers_reevaluatePaper": "papers.reevaluate.php?conferenceId={0}&paperId={1}",
        "papers_finalEvaluator": "papers.finalEvaluator.php?conferenceId={0}&paperId={1}&username={2}",
        "papers_finalStatus": "papers.finalStatus.php?conferenceId={0}&paperId={1}",

        "reviews_getOthersReviews": "reviews.php?paperId={0}",

        //SESSIONS
        "conferences_save": "conferences.php",
        "conferences_getAll": "conferences.php",
        "conferences_getOne": "conferences.php?conferenceId={0}",
        "conferences_update": "conferences.php?conferenceId={0}",

        "conferences_getSessions": "conferences.sessions.php?conferenceId={0}",

        "sessions_getOne": "sessions.php?sessionId={0}",
        "sessions_save": "sessions.php",
        "sessions_getPotentialChairs": "sessions.potentialChairs.php?sessionId={0}",
        "sessions_setSessionChiar": "sessions.sessionChair.php?sessionId={0}",
        "sessions_participate": "sessions.participate.php?sessionId={0}"
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
            result = JSON.parse(result);
        }
        catch (e) {
            onError(["Invalid JSON received!"]);
            return;
        }

        if (hasError(result)) return;
        hasWarning(result);
        if (hasNoResp(result)) return;

        if (run) run(result['resp']);
    }

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
                data: JSON.stringify({}),
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
                data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var isSuperUser = response.isSuperUser;
                        var isCommiteeMember = response.isCommiteeMember;
                        if (onResultArrived) onResultArrived(new SystemPriviledge(isSuperUser, isCommiteeMember))
                    });
                },
                error: errorFunction
            }
        )
    }

    this.getMyPriviledges = function (conferinceId, onResultArrived) {
        $.ajax(
            {
                url: (HOST + COMMANDS["users_getMyPriviledges"]).format(conferinceId),
                data: JSON.stringify({}),
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
                data: JSON.stringify({}),
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
                        var isCommiteeMember = response.isCommiteeMember;
                        if (onResultArrived) onResultArrived(new User(0, username, name, affiliation, email, website, "", isSuperUser, isCommiteeMember));
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
                data: JSON.stringify({}),
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
                        var isCommiteeMember = response.isCommiteeMember;
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
                    isCommiteeMember: user.isCommiteeMember
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

    this.getSubmittedPapers = function (onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_getSubmittedPapers"],
                data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var id = response.id;
                        var name = response.name;
                        var subjects = response.subjects;
                        var keywords = response.keywords;
                        var authors = response.authors;
                        if (onResultArrived) onResultArrived(new Proposal(id, name, subjects, keywords, authors))
                    });
                },
                error: errorFunction
            }
        )
    }

    this.getAcceptedSubmittedPapers = function (onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_getAcceptedSubmittedPapers"],
                data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var name = response.name;
                        var subjects = response.subjects;
                        var keywords = response.keywords;
                        var authors = response.authors;
                        if (onResultArrived) onResultArrived(new Proposal(0, name, subjects, keywords, authors))
                    });
                },
                error: errorFunction
            }
        )
    }

    this.getSubmittedPapersReviews = function (onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_getSubmittedPapersReviews"],
                data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var status = response.status;
                        var justification = response.justification;
                        if (onResultArrived) onResultArrived(new Review(status, justification))
                    });
                },
                error: errorFunction
            }
        )
    }

    this.setSubmittedPapersPresetation = function (data, type, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_setSubmittedPapersPresetation"],
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

    this.getPaperBid = function (onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_getPaperBid"],
                data: JSON.stringify({}),
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

    this.setPaperBid = function (status, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_setPaperBid"],
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

    this.getAssignedForReview = function (onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_getAssignedForReview"],
                data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var id = response.id;
                        var name = response.name;
                        var subjects = response.subjects;
                        var keywords = response.keywords;
                        var authors = response.authors;
                        if (onResultArrived) onResultArrived(new Proposal(id, name, subjects, keywords, authors))
                    });
                },
                error: errorFunction
            }
        )
    }

    this.getPaperReview = function (onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_getPaperReview"],
                data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var status = response.status;
                        var justification = response.justification;
                        if (onResultArrived) onResultArrived(new Review(status, justification))
                    });
                },
                error: errorFunction
            }
        )
    }

    this.setPaperReview = function (status, justification, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_setPaperReview"],
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

    this.setPrivilegdes = function (user, conferenceId, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_setPrivilegdes"].format(user.username, conferenceId),
                data: JSON.stringify({
                    isPCMember: user.isPCMember,
                    isChair: user.isChair,
                    isCoChair: user.isCoChair
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

    this.setPaperAssignement = function (username, paperId, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_setPaperAssignement"].format(username, paperId),
                data: JSON.stringify({}),
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

    this.getAllPapers = function (conferenceId, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_getAll"].format(conferenceId),
                data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var id = response.id;
                        var name = response.name;
                        var subjects = response.subjects;
                        var keywords = response.keywords;
                        var authors = response.authors;
                        if (onResultArrived) onResultArrived(new Proposal(id, name, subjects, keywords, authors))
                    });
                },
                error: errorFunction
            }
        )
    }

    this.savePapers = function (conferenceId, papers, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_save"].format(conferenceId),
                data: JSON.stringify({
                     name : papers.name,
                     subjects : papers.subjects,
                     keywords : papers.keywords,
                     authors : papers.authors
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

    this.updatePapers = function (conferenceId, papers,  onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_update"].format(conferenceId, papers.paperId),
                data: JSON.stringify({
                    name: papers.name,
                    subjects: papers.subjects,
                    keywords: papers.keywords,
                    authors: papers.authors
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
                url: HOST + COMMANDS["papers_uploadFull"].format(conferenceId, paperId),
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

    this.getFullPapers =  function (conferenceId, paperId, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_getFull"].format(conferenceId, paperId),
                data: JSON.stringify({}),
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
                url: HOST + COMMANDS["papers_uploadAbs"].format(conferenceId, paperId),
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
                url: HOST + COMMANDS["papers_getAbs"].format(conferenceId, paperId),
                data: JSON.stringify({}),
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
                url: HOST + COMMANDS["papers_getAllAccepted"].format(conferenceId),
                data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var id = response.id;
                        var name = response.name;
                        var subjects = response.subjects;
                        var keywords = response.keywords;
                        var authors = response.authors;
                        if (onResultArrived) onResultArrived(new Proposal(id, name, subjects, keywords, authors))
                    });
                },
                error: errorFunction
            }
        )
    }

    this.getPotentialReviewers = function (conferenceId, paperId, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_getPotentialReviewers"].format(conferenceId, paperId),
                data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var username = response.username;
                        if (onResultArrived) onResultArrived(username)
                    });
                },
                error: errorFunction
            }
        )
    }

    this.getAssignedReviewers = function (conferenceId, paperId, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_getAssignedReviewers"].format(conferenceId, paperId),
                data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var username = response.username;
                        if (onResultArrived) onResultArrived(username)
                    });
                },
                error: errorFunction
            }
        )
    }

    this.setReevaluatePaper = function (conferenceId, paperId,   onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_reevaluatePaper"].format(conferenceId, paperId),
                data: JSON.stringify({}),
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

    this.setFinalEvaluator = function (conferenceId, paperId, username,  onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_finalEvaluator"].format(conferenceId, paperId, username),
                data: JSON.stringify({}),
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

    this.getOthersReviews = function (paperId, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["papers_getAssignedReviewers"].format(paperId),
                data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var reviews = [];
                        for(var i= 0 ; i<response.length;i++){
                            var status = response.status;
                            var justification = response.justification;
                            var username = response.username;
                            reviews.push(new PaperReview(0, new Review(status, justification),
                                new User(0, username, "", "", "", "", "", "", false, false)));
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
                data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var id = response.id;
                        var name = response.name;
                        var startDateTimeSpan = response.eventTimeSpan.startDate;
                        var endDateTimeSpan = response.eventTimeSpan.endDate;
                        var startDateAbstract = response.callFroAbstractTimeSpan.startDate;
                        var endDateAbstract = response.callFroAbstractTimeSpan.endDate;
                        var startDateProposal = response.callForProposalsTimeSpan.startDate;
                        var endDateProposal = response.callForProposalsTimeSpan.endDate;
                        var biddingDeadline = response.biddingDeadline;
                        if (onResultArrived) onResultArrived(new Conference(id, name, new TimeSpan(startDateTimeSpan, endDateTimeSpan), new TimeSpan(startDateAbstract, endDateAbstract), new TimeSpan(startDateProposal, endDateProposal), biddingDeadline));
                    });
                },
                errorFunction: errorFunction
            }
        )
    }

    this.getOneConference = function (onResultArrived) {
        $.ajax(
            {
                url: (HOST + COMMANDS["conferences_getOne"]),
                data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var name = response.name;
                        var startDateTimeSpan = response.eventTimeSpan.startDate;
                        var endDateTimeSpan = response.eventTimeSpan.endDate;
                        var startDateAbstract = response.callFroAbstractTimeSpan.startDate;
                        var endDateAbstract = response.callFroAbstractTimeSpan.endDate;
                        var startDateProposal = response.callForProposalsTimeSpan.startDate;
                        var endDateProposal = response.callForProposalsTimeSpan.endDate;
                        var biddingDeadline = response.biddingDeadline;
                        if (onResultArrived) onResultArrived(new Conference(name, new TimeSpan(startDateTimeSpan, endDateTimeSpan), new TimeSpan(startDateAbstract, endDateAbstract), new TimeSpan(startDateProposal, endDateProposal), biddingDeadline));
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
                    callFroAbstractTimeSpan:{
                        startDate: conference.callFroAbstractTimeSpan.startDate,
                        endDate:conference.callFroAbstractTimeSpan.endDate,
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
                    callFroAbstractTimeSpan:{
                        startDate: conference.callFroAbstractTimeSpan.startDate,
                        endDate:conference.callFroAbstractTimeSpan.endDate,
                    },
                    callForProposalsTimeSpan:{
                        startDate: conference.callForProposalsTimeSpan.startDate,
                        endDate:conference.callForProposalsTimeSpan.endDate,
                    },biddingDeadline: conference.biddingDeadline
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


    this.getSessionConference = function (conferenceId ,onResultArrived) {
        $.ajax(
            {
                url: (HOST + COMMANDS["conferences_getSessions"]).format(conferenceId),
                data: JSON.stringify({}),
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
                        if (onResultArrived) onResultArrived(new Section(id, name, new ScheduleItem(sessionId, paperId, presentationStartTime, presentationEndTime, username)));
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
                data: JSON.stringify({}),
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
                        if (onResultArrived) onResultArrived(new Section(id, name, new ScheduleItem(sessionId, paperId, presentationStartTime, presentationEndTime, username)));
                    });
                },
                errorFunction: errorFunction
            }
        )
    }

    this.saveSession = function (session, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["sessions_save"],
                data: JSON.stringify({
                    name : session.name,
                    sessionId : session.schedule.sessionId,
                    paperId : session.schedule.paperId,
                    presentationStartTime : session.schedule.presentationStartTime,
                    presentationEndTime : session.schedule.presentationEndTime,
                    username : session.schedule.username
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


    this.getPotentialChairs = function (session, onResultArrived) {
        $.ajax(
            {
                url: (HOST + COMMANDS["sessions_getPotentialChairs"]).format(session.id),
                data: JSON.stringify({}),
                type: "GET",
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        var id = response.id;
                        var username = response.username;
                        var name = response.name;
                        var affiliation = response.affiliation;
                        var email = response.email;
                        var website = response.website;
                        if (onResultArrived) onResultArrived(new User(id, username, name, affiliation, email, website, "", "", ""));
                    });
                },
                errorFunction: errorFunction
            }
        )
    }

    this.setSessionChiar = function (sessionId, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["sessions_setSessionChiar"].format(sessionId),
                data: JSON.stringify({
                    id : sessionId
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

    this.participateSession = function (sessionId, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["sessions_participate"].format(sessionId),
                data: JSON.stringify({}),
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
