var User = function (username, email, password=undefined, discogsToken) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.discogsToken = discogsToken;
};

var SearchItem = function (searchString, resultsCount, newlyReported, searchLink) {
    this.searchString = searchStrin;
    this.resultsCount = resultsCount;
    this.newlyReported = newlyReported;
    this.searchLink = searchLink;
};
