var Conference = function(id = undefined, name= undefined, eventTimeSpan = undefined, callForAbstract = undefined, callForProposals = undefined, biddingDeadline = undefined){
	this.id = id;
	this.name = name;
	this.eventTimeSpan = eventTimeSpan;
	this.callForAbstractTimeSpan = callForAbstract;
	this.callForProposalsTimeSpan = callForProposals;
	this.biddingDeadline = biddingDeadline;
};

var TimeSpan = function(startDate = undefined, endDate= undefined){
    this.startDate = startDate;
    this.endDate = endDate;
}

var User = function(id = undefined, username, name, email, affiliation, website, password=undefined, isSuperUser=undefined, isCommiteeMember=undefined){
    this.id = id;
    this.username = username;
	this.name = name;
	this.email = email;
	this.affiliation = affiliation;
	this.website = website;
	this.password = password;
	this.isSuperUser = isSuperUser;
	this.isCometeeMember= isCommiteeMember;
};


var Deadline = function(name, date){
	this.name = name;
	this.date = date;
};

var Priviledge = function (isAuthor = false, isPCMember = false, isChair = false,
    isCoChair = false){
    this.isAuthor = isAuthor;
    this.isPCMember = isPCMember;
    this.isChair = isChair;
    this.isCoChair = isCoChair;
}

var SystemPriviledge= function(isSuperUser= false, isCommiteeMember = false){
	this.isCometeeMember = isCommiteeMember;
	this.isSuperUser = isSuperUser;
}

var ScheduleItem = function(sessionId, paperId, presentationStartTime, presentationEndTime, username){
	this.sessionId = sessionId;
	this.paperId = paperId;
	this.presentationStartTime = presentationStartTime;
	this.presentationEndTime = presentationEndTime;
	this.username = username;
};


var Section = function(id, name, schedule){
	this.id = id;
    this.name = name;
	this.schedule = schedule || [];
};

var ReviewType = {
	NONE: 'NONE', 
	STRONG_ACCEPT: 'STRONG_ACCEPT',
	WEAK_ACCEPT: 'WEAK_ACCEPT', 
	BORDERLINE_PAPER: 'BORDERLINE_PAPER', 
	WEAK_REJECT: 'WEAK_REJECT', 
	REJECT: 'REJECT', 
	STRONG_REJECT: 'STRONG_REJECT',
	};

var BidType = {
	ACCEPT_FOR_REVIEW: 'ACCEPT',
	COULD_REVIEW: 'PLEASED',
	REJECT_FROM_REVIEW: 'REJECT',
	NONE: 'NONE',
};

var Review = function(status, recommendation){
	this.status = status;
	//recommendations
	this.justification = recommendation;
};

var Proposal  = function(id, name, keywords, subjects, authors){
	this.id = id;
	this.name = name;
	this.keywords = keywords || [];
	this.subjects = subjects || [];
	this.authors = authors || [];
};

var PaperReview = function(id, review, reviewer){
	this.id = id;
	this.review = review || [];
	this.reviewer = reviewer || [];
};
