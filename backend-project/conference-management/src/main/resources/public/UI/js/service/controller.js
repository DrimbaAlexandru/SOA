/**
 * Created by elisei on 15.05.2017.
 * !!! DON'T FORGET TO IMPORT utils
 */

function isUndefined(v) {
    if (typeof v !== "undefined") {
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
        "users_save": "users",
        "users_update": "users/{0}",
        "users_getSearchItems": "users/searches",
        "users_postSearchItem": "users/searches",
    };


    function typeOf(object) {
        return Object.prototype.toString.call(object);
    }

    function isArray(object) {
        return object.constructor === Array;
    }

    function errorFunction(result) {
        console.log(result.responseText);
        o = JSON.parse(result.responseText);
        if (!hasError(o))
            onError(["Error receiving response:\n"] + o.errors);
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
            if (typeOf(result) === "[object String]")
                result = JSON.parse(result);
        } catch (e) {
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
                xhrFields: {
                    withCredentials: true
                },
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
                xhrFields: {
                    withCredentials: true
                },
                success: function (result) {
                    try {
                        console.log(typeOf(result));
                        if (typeOf(result) === "[object String]")
                            result = JSON.parse(result);
                    } catch (e) {
                        onError(["Invalid JSON received!"]);
                        return;
                    }

                    onResultArrived(true);

                },
                error: function (result) {
                    try {
                        console.log(typeOf(result));
                        if(typeOf(result) === "[object String]")
                            resultO = JSON.parse(result);
                    }
                    catch (e) {
                        onError(["Invalid JSON received!"]);
                        return;
                    }
                    errorFunction(result);
                    onResultArrived(false);
                }
            }
        )
    }


    this.saveUser = function (user, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_save"],
                data: JSON.stringify({
                    username: user.username,
                    email: user.email,
                    discogsToken: user.discogsToken,
                    password: user.password
                }),
                xhrFields: {
                    withCredentials: true
                },
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
                xhrFields: {
                    withCredentials: true
                },
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

    this.getSearchResults = function (onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_getSearchItems"],
                type: "GET",
                xhrFields: {
                    withCredentials: true
                },
                contentType: applicationJSON,
                success: function (result) {
                    validateAndRun(result, function (response) {
                        console.log(response);
                        // var papers = [];
                        // for(var i in response){
                        //
                        //     var id = response[i].id;
                        //     var name = response[i].name;
                        //     var subjects = response[i].subjects;
                        //     var keywords = response[i].keywords;
                        //     var authors = response[i].authors;
                        //     papers.push(new Proposal(id, name, keywords, subjects, authors));
                        // }
                        if (onResultArrived) onResultArrived(response);
                    });
                },
                error: errorFunction
            }
        )
    }

    this.addSearchString = function (searchString, onResultArrived) {
        $.ajax(
            {
                url: HOST + COMMANDS["users_postSearchItem"],
                data: JSON.stringify({
                    searchString: searchString
                }),
                type: "POST",
                xhrFields: {
                    withCredentials: true
                },
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
