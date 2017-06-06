package com.company.controller;

import com.company.domain.*;
import com.company.service.UserService;
import com.company.utils.ResponseJSON;
import com.company.utils.exception.Exceptional;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.MultiPixelPackedSampleModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by AlexandruD on 02-Jun-17.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private UserService service;

    public UserController(@Autowired UserService service) {
        this.service = service;
    }

    private void setCookie(String name, String value, int expiry, HttpServletResponse response) {
        Cookie c=new Cookie(name,value);
        c.setMaxAge(expiry);
        response.addCookie(c);
    }

    @RequestMapping(path = "/login",
                   method = RequestMethod.POST)
    public ResponseEntity<ResponseJSON<String>> login
            (@RequestBody loginRequest body,
             HttpServletResponse response)
    {
        ResponseJSON<String> resp=new ResponseJSON<>();
        resp.setResp("HARDCORE GAY PORN");
        Exceptional<AppUser> ex = service.getUser(body.getUsername());
        ex.error(e -> resp.addError(e.getMessage()))
            .ok(e -> {
                if(!service.login(body.getUsername(),body.getPassword()))
                    resp.addError("Incorrect password");
            });

        if(resp.getErrors().size()==0) {
            setCookie("username",body.getUsername(),3600,response);
            setCookie("password",body.getPassword(),3600,response);
        }
        else {
            setCookie("username","",0,response);
            setCookie("password","",0,response);
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @RequestMapping(path = "/loggedIn", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<loggedInResponse>> handle_loggedIn(
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie)
    {
        ResponseJSON<loggedInResponse> resp=new ResponseJSON<>();
        loggedInResponse lir=new loggedInResponse();
        if(service.login(usernameCookie,passwordCookie))
        {
            Exceptional<AppUser> au = service.getUser(usernameCookie);
            au.error(e -> resp.addError(e.getMessage()))
                .ok(e -> {
                    lir.setCommiteeMember(e.getIsCometeeMember());
                    lir.setSuperUser(e.getIsSuperUser());
                });
        }
        else
        {
            resp.addError("User is not logged in or username and password are incorrect");
        }
        resp.setResp(lir);
        return new  ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path = "/privileges", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<ConferencePrivileges>> handle_getConferencePrivileges(
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie,
            @RequestParam("conferenceId") Integer id)
    {
        ResponseJSON<ConferencePrivileges> resp=new ResponseJSON<>();
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        resp.getWarnings().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getWarnings());
        if(resp.getErrors().size()==0)
        {
            Exceptional<Privileges> privs = service.getConferencePrivileges(usernameCookie, id);
            privs.error(e -> {
                resp.addError(e.getMessage());
            }).ok(e -> {
                resp.setResp(new ConferencePrivileges(e));
            });
        }
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path = "/mine", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<UserResponse>> handle_get_mine(
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie)
    {
        ResponseJSON<UserResponse> resp=new ResponseJSON<>();
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        resp.getWarnings().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getWarnings());
        if(resp.getErrors().size()==0)
        {
            Exceptional<AppUser> u = service.getUser(usernameCookie);
            u.error(e -> {
                resp.addError(e.getMessage());
            }).ok(e -> {
                resp.setResp(new UserResponse(e));
            });
        }
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<Iterable<UserResponse>>> handle_get_all()
    {
        List<UserResponse> users=new ArrayList<>();
        for(AppUser u:service.getUsers())
        {
            users.add(new UserResponse(u));
        }
        ResponseJSON<Iterable<UserResponse>> resp=new ResponseJSON<>();
        resp.setResp(users);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity<ResponseJSON<String>> handle_create_user(@RequestBody updateUserRequest body)
    {
        ResponseJSON<String> resp=new ResponseJSON<>("");
        service.getUser(body.getUsername()).error(a->{
            service.addUser(new AppUser(body.getUsername(),
                body.getName(),body.getAffiliation(),body.getEmail(),body.getWebsite(),body.getPassword())).error(b-> {
                    resp.addError("Unexpected error. The given email may be already be used by another user");});}
        ).ok(a->{resp.addError("An user with this username already exists");});
        return new ResponseEntity<ResponseJSON<String>>(resp,HttpStatus.OK);
    }

    @RequestMapping(path = "/{username}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseJSON<String>> handle_update_user(
            @PathVariable("username") String username,
            @RequestBody updateUserRequest body,
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie,
            HttpServletResponse response)
    {
        ResponseJSON<String> resp=new ResponseJSON<>("");
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        if(service.getUser(username).isException())
            resp.addError("The username you want to make changes to doesn't exist");
        if(resp.getErrors().size()==0) {
            Exceptional<AppUser> au = service.getUser(usernameCookie);
            au.error(e -> {
                resp.addError(e.getMessage());
            }).ok(e -> {
                if ((username.equals(usernameCookie) && (body.getUsername()==null || service.getUser(body.getUsername()).isException())) ||
                        (e.getIsSuperUser())) {

                    service.updateUser(username, new AppUser(
                            body.getUsername(), body.getName(), body.getAffiliation(), body.getEmail(), body.getWebsite(),
                            body.getPassword(), null, body.getisCommiteeMember()));
                    if (usernameCookie.equals(username)) {
                        if(body.getUsername()!=null)
                            setCookie("username", body.getUsername(), 3600, response);
                        if(body.getPassword()!=null)
                            setCookie("password", body.getPassword(), 3600, response);
                    }
                } else {
                    resp.addError("You don't have the permissions to do these changes!");
                }
            });
        }

        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path = "/{username}/{conferenceId}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseJSON<String>> handle_grant_permissions(
            @PathVariable("username") String username,
            @PathVariable("conferenceId") Integer conferenceId,
            @RequestBody grantPermissionsRequest body,
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie)
    {
        ResponseJSON<String> resp=new ResponseJSON<>("");
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        if(resp.getErrors().size()==0) {
            Exceptional<AppUser> au = service.getUser(usernameCookie);
            au.error(e -> {
                resp.addError(e.getMessage());
            }).ok(e -> {
                if (username.equals(usernameCookie) ||
                        e.getIsSuperUser()) {
                    Exceptional<Privileges> priv = service.getConferencePrivileges(username, conferenceId);
                    priv.error(f -> {
                        resp.addError(f.getMessage());
                    }).ok(f -> {
                        if (body.getIsChair() != null)
                            f.setIsChair(body.getIsChair());
                        if (body.getIsCoChair() != null)
                            f.setIsCoChair(body.getIsCoChair());
                        if (body.getIsPCMember() != null)
                            f.setIsPCMember(body.getIsPCMember());
                        service.updatePrivilegesOfConference(username, conferenceId, f);
                    });
                } else {
                    resp.addError("You don't have the permissions to do these changes!");
                }
            });
        }
        else {
            resp.addError("You don't have the permissions to do these changes!");
        }
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path="/submittedPapers", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<List<submittedPaperResponse>>> handle_get_submitted_papers(
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie,
            @RequestParam(name="status", required = false) String status)
    {
        ResponseJSON<List<submittedPaperResponse>> resp=new ResponseJSON<>();
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        List<submittedPaperResponse> papers=new ArrayList<>();
        resp.setResp(papers);
        if(resp.getErrors().size()==0) {
            Exceptional<Iterable<Paper>> paps = service.getSubmittedPapers(usernameCookie);
            paps.error(e -> {
                resp.addError(e.getMessage());
            }).ok(e -> {
                for (Paper p : e)
                    if (p.getStatus().toString().equals(status) || status == null)
                        papers.add(new submittedPaperResponse(p));
            });
        }
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path="/submittedPapers/{idPaper}/reviews", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<List<reviewResponse>>> handle_get_paper_reviews(
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie,
            @PathVariable("idPaper") int paperId)
    {
        ResponseJSON<List<reviewResponse>> resp=new ResponseJSON<>();
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        List<reviewResponse> reviews=new ArrayList<>();
        resp.setResp(reviews);
        boolean isMyPaper=false;

        if(resp.getErrors().size()==0) {
            Exceptional<Iterable<Review>> revsOfPaper =
                    service.getReviewsOfPaper(usernameCookie, paperId);
            revsOfPaper.error(e -> {
                resp.addError(e.getMessage());
            }).ok(e -> {
                for (Review r : e)
                    reviews.add(new reviewResponse(r));
            });
        }
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path="/bids/{idPaper}", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<bidResponse>> handle_get_my_bid_for_paper(
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie,
            @PathVariable("idPaper") int paperId)
    {
        ResponseJSON<bidResponse> resp=new ResponseJSON<>();
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        bidResponse body=new bidResponse();
        body.setStatus("NONE");
        resp.setResp(body);

        Exceptional<Bid> bid = service.getBidOfPaper(usernameCookie,paperId);

        if(resp.getErrors().size()==0)
            bid.error(e -> {
                resp.addError(e.getMessage());
            }).ok(e -> {
                body.setStatus(e.getStatus().name());
            });

        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path="/bids/{idPaper}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseJSON<String>> handle_put_my_bid_for_paper(
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie,
            @PathVariable("idPaper") int paperId,
            @RequestBody bidResponse request)
    {
        ResponseJSON<String> resp=new ResponseJSON<>();
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        resp.setResp("");

        Exceptional<Bid> bid = service.getBidOfPaper(usernameCookie,paperId);
        if(resp.getErrors().size()==0)
            bid.error(e -> {
                Exceptional<Void> res = service.addBidForPaper(usernameCookie, paperId,
                                BidStatus.valueOf(request.getStatus()));
                res.error(f -> {
                    resp.addError(f.getMessage());
                });
            }).ok(e -> {
                service.addBidForPaper(usernameCookie, paperId,
                        BidStatus.valueOf(request.getStatus()));
            });

        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path="/assignedForReview", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<Iterable<submittedPaperResponse>>> handle_get_assigned(
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie)
    {
        ResponseJSON<Iterable<submittedPaperResponse>> resp=new ResponseJSON<>();
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        List<submittedPaperResponse> body=new ArrayList<>();
        resp.setResp(body);
        if(resp.getErrors().size()==0)
        {
            Exceptional<Iterable<Paper>> pps = service.getAssignedPapers(usernameCookie);
            pps.error(e -> {
                resp.addError(e.getMessage());
            }).ok(e -> {
               for(Paper p: e) {
                   body.add(new submittedPaperResponse(p));
               }
            });
        }
        return new ResponseEntity<ResponseJSON<Iterable<submittedPaperResponse>>>(resp,HttpStatus.OK);
    }

    @RequestMapping(path="/reviews/{paperId}", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<reviewResponse>> handle_get_review(
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie,
            @PathVariable("paperId") int paperId)
    {
        ResponseJSON<reviewResponse> resp=new ResponseJSON<>();
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        if(resp.getErrors().size()==0)
        {
            Exceptional<Iterable<Review>> reviews=service.getReviewsOfPaper(usernameCookie,paperId);

            reviews.ok(e -> {
                if(e.iterator().hasNext()) {
                    resp.setResp(new reviewResponse(e.iterator().next()));
                } else {
                    resp.addError("The paper with the given ID doesn't have a review from this user");
                }
            }).error(e -> {
                resp.addError(e.getMessage());
            });
        }
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path="/reviews/{idPaper}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseJSON<String>> handle_put_my_review_for_paper(
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie,
            @PathVariable("idPaper") int paperId,
            @RequestBody reviewResponse request)
    {
        ResponseJSON<String> resp=new ResponseJSON<>();
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        resp.setResp("");

        if(resp.getErrors().size()==0) {
            service.addReviewToPaper(usernameCookie,paperId,ReviewStatus.valueOf(request.getStatus()),request.getJustification()).
                    error(e->{resp.addError(e.getMessage());});
        }

        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path="/{id}/assignPaper/{paperId}",method = RequestMethod.PUT)
    public ResponseEntity<ResponseJSON<String>> handle_assign_paper(
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie,
            @PathVariable("id") String reviewerUsername,
            @PathVariable("paperId") int paperId)
    {
        ResponseJSON<String> resp=new ResponseJSON<>();
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        resp.setResp("");

        if(resp.getErrors().size()==0) {
            service.assignPaper(reviewerUsername,paperId).error(e->{resp.addError(e.getMessage());});
        }

        return new ResponseEntity<>(resp,HttpStatus.OK);
    }
//------------------------------------------------------------------

    @RequestMapping(path = "/submittedPapers/{paperId}/presentation",
            method = RequestMethod.POST)
    public ResponseEntity<ResponseJSON<String>> uploadPresentation(
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie,
            @PathVariable("paperId") Integer paperId,
            @RequestParam("type") String type,
            @RequestParam("file") MultipartFile file) {

        try {
            byte[] fileData = file.getBytes();

            ResponseJSON<String> resp = new ResponseJSON<>();
            resp.getErrors().addAll(handle_loggedIn(usernameCookie, passwordCookie).getBody().getErrors());
            if (resp.getErrors().size() == 0)
                service.uploadPresentation(usernameCookie,
                        paperId,
                        "." + type,
                        fileData);
            return new ResponseEntity<ResponseJSON<String>>(resp, HttpStatus.OK);
        }catch(IOException e) {
            return new ResponseEntity<ResponseJSON<String>>(new ResponseJSON<>("Error occured on file upload"),
                    HttpStatus.BAD_REQUEST);
        }
    }

}

class loginRequest
{
    private String username;
    private String password;
    public loginRequest(){}
    public String getPassword() { return password; }
    public String getUsername() { return username; }
    public void setPassword(String password) { this.password = password; }
    public void setUsername(String username) { this.username = username; }
}

class loggedInResponse
{
    private boolean isSuperUser=false;
    private boolean isCommiteeMember=false;
    public loggedInResponse(){}
    public boolean isCommiteeMember() { return isCommiteeMember; }
    public boolean isSuperUser() { return isSuperUser; }

    public void setCommiteeMember(boolean commiteeMember) {
        isCommiteeMember = commiteeMember;
    }

    public void setSuperUser(boolean superUser) {
        isSuperUser = superUser;
    }
}

class PresentationPostDTO
{
    private String type;
    private MultipartFile data;

    public PresentationPostDTO() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MultipartFile getData() {
        return data;
    }

    public void setData(MultipartFile data) {
        this.data = data;
    }
}

class UserResponse
{
    private String username="";
    private String name="";
    private String affiliation="";
    private String email="";
    private String website="";
    public UserResponse(){}
    public UserResponse(AppUser u)
    {
        username=u.getUsername();
        name=u.getName();
        affiliation=u.getAffiliation();
        email=u.getEmail();
        website=u.getWebpage();
    }
    public UserResponse(String _username,String _name,String _affiliation, String _email,String _website)
    {
        username=_username;
        name=_name;
        affiliation=_affiliation;
        email=_email;
        website=_website;
    }

    public String getUsername() {
        return username;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }
}

class ConferencePrivileges
{
    private boolean isAuthor=false;
    private boolean isChair=false;
    private boolean isCoChair=false;
    private boolean isPCMember=false;
    public ConferencePrivileges(){}
    public ConferencePrivileges(Privileges p)
    {
        isAuthor=p.getIsAuthor();
        isChair=p.getIsChair();
        isCoChair=p.getIsCoChair();
        isPCMember=p.getIsPCMember();
    }

    public boolean getIsPCMember() {
        return isPCMember;
    }

    public boolean getIsCoChair() {
        return isCoChair;
    }

    public boolean getIsChair() {
        return isChair;
    }

    public boolean getIsAuthor() {
        return isAuthor;
    }

    public void setIsAuthor(boolean author) {
        isAuthor = author;
    }

    public void setIsChair(boolean chair) {
        isChair = chair;
    }

    public void setIsCoChair(boolean coChair) {
        isCoChair = coChair;
    }

    public void setIsPCMember(boolean PCMember) {
        isPCMember = PCMember;
    }
}

class updateUserRequest
{
    private String username;
    private String name;
    private String affiliation;
    private String email;
    private String website;
    private String password;
    private Boolean isCommiteeMember=null;
    public updateUserRequest(){}

    public void setIsCommiteeMember(boolean commiteeMember) {
        isCommiteeMember = commiteeMember;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWebsite() {
        return website;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Boolean getisCommiteeMember() {
        return isCommiteeMember;
    }
}

class grantPermissionsRequest
{
    private Boolean isPCMember=null;
    private Boolean isChair=null;
    private Boolean isCoChair=null;

    public grantPermissionsRequest(){}

    public void setIsChair(boolean chair) {
        isChair = chair;
    }

    public void setIsCoChair(boolean coChair) {
        isCoChair = coChair;
    }

    public void setIsPCMember(boolean PCMember) {
        isPCMember = PCMember;
    }

    public Boolean getIsChair() {
        return isChair;
    }

    public Boolean getIsCoChair() {
        return isCoChair;
    }

    public Boolean getIsPCMember() {
        return isPCMember;
    }
}

class submittedPaperResponse
{
    private int id;
    private String name;
    private List<String> subjects=new ArrayList<>();
    private List<String> keywords=new ArrayList<>();
    private List<String> authors;

    public submittedPaperResponse(Paper p)
    {
        id=p.getId();
        name=p.getNume();
        authors=p.getAuthors().stream().map((u)->{return u.getUsername();}).collect(Collectors.toList());
    }
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public List<String> getSubjects() {
        return subjects;
    }
}

class reviewResponse
{
    private String status="";
    private String justification="";
    public reviewResponse(Review r)
    {
        status=r.getStatus().name();
        justification=r.getJustification();
    }

    public reviewResponse(){}
    public String getJustification() {
        return justification;
    }

    public String getStatus() {
        return status;
    }
}

class bidResponse
{
    private String status;
    public bidResponse(){}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}