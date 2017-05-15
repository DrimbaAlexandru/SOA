var FORConference = function(name, date){
	this.name = name;
	this.date = date;
};

var Member = function(name, date, email, affiliation, website){
	this.name = name;
	this.email = email;
	this.affiliation = affiliation;
	this.date = date;
	this.webite = website;
	
};


var Deadline = function(name, date){
	this.name = name;
	this.date = date;
};

var Author = function(name, metadata){
	this.name = name;
	this.metadata = metadata;
};


var ProgramItem = function(speakerName, metadata){
	this.speakerName = speakerName;
	this.metadata = metadata;
};


var Person = function(id, name){
	this.name = name;
	this.id = id;
};

var Section = function(id, name, program){
	this.id = id;
    this.name = name;
	this.program = program || [];
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

	
var Review = function(reviewType, recommendations){
	this.reviewType = reviewType;
	this.recommendations = recommendations || [];
};

var Proposal  = function(id, title, keywords, subjects, authors){
	this.id = id;
	this.title = title;
	this.keywords = keywords || [];
	this.subjects = subjects || [];
	this.authors = authors || [];
};


var AcceptedProposal = function(id, title, keywords,subjects, authors, recommendations){
	this.id = id;
	this.title = title;
	this.keywords = keywords || [];
	this.subjects = subjects || [];
	this.authors = authors || [];
	this.recommendations = recommendations || [];
};


AcceptedProposal.prototype = Object.create(Proposal);
