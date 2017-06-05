/**
 * Created by elisei on 15.05.2017.
 */
function Controller(onError= undefined, onWarning = undefined){

    this.onError = onError;
    this.onWarning = onWarning;

    this.HOST = "";
    this.COMMANDS = {
        "login": "users/login",
        "logout": "users/logout",
        "loggedIn": "users/loggedIn",
        "priviledges":"users/priviledges?conferenceId={0}",
        "users_getAll":"users",
        "users_getOne":"users/{0}",
        "users_save":"users",
        "users_update":"users/{0}",
        ""

    };

    function hasError(result){
        if ("errors" in result){
            onError(result["errors"]);
            return true;
        }
        return false;
    }

    function hasWarning(result){
        if("warnings" in result){
            onWarning(result["warnings"]);
            return true;
        }
        return false;
    }


    this.login
}