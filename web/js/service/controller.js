/**
 * Created by elisei on 15.05.2017.
 * !!! DON'T FORGET TO IMPORT utils
 */


function Controller(onError= undefined, onWarning = undefined){

    this.onError = onError;
    this.onWarning = onWarning;
    const applicationJSON = "application/json";
    const HOST = "";
    const COMMANDS = {
        "users_login": "users.login.php",//"users/users_login",
        "users_logout": "users.logout.php",//"users/logout",
        "users_loggedIn": "users.loggedIn.php",//"users/loggedIn",
        "users_getMyPriviledges":"users.priviledges.php?conferenceId={0}",//"users/priviledges?conferenceId={0}",
        "users_getAll":"users.php",
        "users_getOne":"users.php?username={0}",
        "users_save":"users.php",
        "users_update":"users.php?username={0}",
        //parametrul username va fi luat din sesiune
        "users_getSubmittedPapers":"users.submittedPapers.php",
        "users_getAcceptedSubmittedPapers":"users.submittedPapers.php?status=accepted",
        "users_getSubmittedPapersReviews":"users.submittedPapers.reviews.php?idPaper={0}",
        "users_setSubmittedPapersPresetation":"users.submittedPapers.presentation.php?idPaper={0}",
        "users_getPaperBid":"users.bids.php?paperId={0}",
        "users_setPaperBid":"users.bids.php?paperId={0}",
        "users_getAssignedForReview":"users.asignedForReview.php",
        "users_getPaperReview":"users.reviews.php?paperId={0}",
        "users_setPaperReview":"users.reviews.php?paperId={0}",
        //parametrul username e explicit
        "users_setPrivilegdes":"users.php?username={0}&conferenceId={1}",
        "users_setPaperAssignement":"users.assignedPapers.php?username={0}&paperId={1}",

        "papers_getAll":"papers.php?conferenceId={0}",
        "papers_save":"papers.php?conferenceId={0)",
        "papers_update":"papers.php?conferenceId={0}&paperId={1}",
        "papers_uploadFull":"papers.full.php?conferenceId={0}&paperId={1}",
        "papers_getFull":"papers.full.php?conferenceId={0}&paperId={1}",
        "papers_uploadAbs":"papers.abs.php?conferenceId={0}&paperId={1}",
        "papers_getAbs":"papers.abs.php?conferenceId={0}&paperId={1}",

        "papers_getAllAccepted":"papers.acceptedProposals.php?conferenceId={0}",
        "papers_getPotentialReviewers":"papers.potentialReviewers.php?conferenceId={0}&paperId={1}",
        "papers_getAssignedReviewers":"papers.assignedReviewers.php?conferenceId={0}&paperId={1}",
        "papers_reevaluatePaper":"papers.reevaluate.php?conferenceId={0}&paperId={1}",
        "papers_finalEvaluator":"papers.finalEvaluator.php?conferenceId={0}&paperId={1}&username={2}",
        "papers_finalStatus":"papers.finalStatus.php?conferenceId={0}&paperId={1}",

        "reviews_getOthersReviews":"reviews.php?paperId={0}",

        //SESSIONS
        "conferences_save":"conferences.php",
        "conferences_getAll":"conferences.php",
        "conferences_getOne":"conferences.php?conferenceId={0}",
        "conferences_update":"conferences.php?conferenceId={0}",

        "conferences_getSessions":"conferences.sessions.php?conferenceId={0}",

        "sessions_getOne":"sessions.php?sessionId={0}",
        "sessions_save":"sessions.php",
        "sessions_getPotentialChairs":"sessions.potentialChairs.php?sessionId={0}",
        "sessions_setSessionChiar":"sessions.sessionChair.php?sessionId={0}",
        "sessions_participate":"sessions.participate.php?sessionId={0}"
    };


    function typeOf(object){
        return Object.prototype.toString.call(object);
    }

    function isArray(object){
        return object.constructor === Array;
    }

    function errorFunction(result){
        onError(["Error receiving response:\n" +result.responseText]);
    }

    function hasError(result){
        if ("errors" in result && isArray(result["errors"]) &&
            result["errors"].length > 0){
            onError(result["errors"]);
            return true;
        }
        return false;
    }


    function hasWarning(result){
        if("warnings" in result && isArray(result["warnings"])
            && result["warnings"].length > 0){
            onWarning(result["warnings"]);
            return true;
        }
        return false;
    }

    function hasNoResp(result){
        if(!("resp" in result)){
            onError(["Invalid response: field 'resp' not found!"]);
            return true;
        }
        return false;
    }

    function validateAndRun(result, run){
        try{
            result = JSON.parse(result);
        }
        catch (e){
            onError(["Invalid JSON received!"]);
            return;
        }

        if(hasError(result)) return;
        hasWarning(result);
        if (hasNoResp(result)) return;

        if(run) run(result['resp']);
    }

    this.login = function (username, password, onResultArrived){
        $.ajax(
            {
                url:HOST + COMMANDS["users_login"],
                data:JSON.stringify({
                    username:username,
                    password:password
                }),
                type:"POST",
                contentType:applicationJSON,
                success:function(result){
                    validateAndRun(result, function(response){
                        if(onResultArrived) onResultArrived();
                    });
                },
                error:errorFunction
            }
        )
    }

    this.logout = function(onResultArrived){
        $.ajax(
            {
                url:HOST+COMMANDS["users_logout"],
                data:JSON.stringify({}),
                type:"POST",
                contentType:applicationJSON,
                success:function(result){
                    validateAndRun(result, function(response){
                        if(onResultArrived) onResultArrived();
                    });
                },
                error:errorFunction
            }
        )
    }
    
    this.loggedIn = function (onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_loggedIn"],
                data:JSON.stringify({}),
                type:"GET",
                contentType:applicationJSON,
                success:function(result) {
                    validateAndRun(result, function(response){
                       var isSuperUser = response.isSuperUser;
                       var isCommiteeMember = response.isCommiteeMember;
                       if(onResultArrived) onResultArrived(new SystemPriviledge(isSuperUser, isCommiteeMember))
                    });
                },
                error:errorFunction
            }
        )
    }

    this.getMyPriviledges= function (conferinceId, onResultArrived) {
        $.ajax(
            {
                url:(HOST + COMMANDS["users_getMyPriviledges"]).format(conferinceId),
                data:JSON.stringify({}),
                type:"GET",
                contentType:applicationJSON,
                success:function (result) {
                    validateAndRun(result, function(response){
                        var isAuthor = response.isAuthor;
                        var isPCMember = response.isPCMember;
                        var isChair = response.isChair;
                        var isCoChair = response.isCoChair;
                        if(onResultArrived) onResultArrived(new Priviledge(isAuthor, isPCMember, isChair, isCoChair));
                    });
                },
                errorFunction:errorFunction

            }
        )

    }

}