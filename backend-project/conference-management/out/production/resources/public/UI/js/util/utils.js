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

    "homepage":"homepage.html",//DONE
};

PAGES_REVERSE ={};

for(var key in PAGES){
    PAGES_REVERSE[PAGES[key]] = key;
}

WITHOUT_SESSION = ["index", "login", "register"]
