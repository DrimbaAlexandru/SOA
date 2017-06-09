package com.company.controller;

import com.company.controller.DTOs.*;
import com.company.domain.*;
import com.company.service.UserService;
import com.company.utils.ResponseJSON;
import com.company.utils.exception.Exceptional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        c.setPath("/");
        response.addCookie(c);
    }

    @RequestMapping(path = "/login",
                   method = RequestMethod.POST)
    public ResponseEntity<ResponseJSON<String>> login
            (@RequestBody loginRequest body,
             HttpServletResponse response)
    {

        ResponseJSON<String> resp=new ResponseJSON<>();
        resp.setResp("GAY PORN" );
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
            setCookie("username","ccc",0,response);
            setCookie("password","ccc",0,response);
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @RequestMapping(path = "/logout",
            method = RequestMethod.POST)
    public ResponseEntity<ResponseJSON<String>> logout
            (HttpServletResponse response)
    {
        ResponseJSON<String> resp=new ResponseJSON<>();
        resp.setResp("GAY PORN");

        setCookie("username","",0,response);
        setCookie("password","",0,response);
        return new ResponseEntity<ResponseJSON<String>>(resp,HttpStatus.OK);
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
                    lir.setIsCometeeMember(e.getIsCometeeMember());
                    lir.setIsSuperUser(e.getIsSuperUser());
                });
        }
        else
        {
            resp.addError("User is not logged in or username and password are incorrect");
        }
        resp.setResp(lir);
        return new  ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path = "/{username}/privileges", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<ConferencePrivilegesDTO>> handle_getConferencePrivileges(
            @PathVariable("username") String username,
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie,
            @RequestParam("conferenceId") Integer id)
    {
        ResponseJSON<ConferencePrivilegesDTO> resp=new ResponseJSON<>();
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        resp.getWarnings().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getWarnings());
        if(resp.getErrors().size()==0)
        {
            Exceptional<Privileges> privs = service.getConferencePrivileges(username, id);
            privs.error(e -> {
                resp.addError(e.getMessage());
            }).ok(e -> {
                resp.setResp(new ConferencePrivilegesDTO(e));
            });
        }
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path = "/{username}", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<UserResponse>> handle_get_mine(
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie,
            @PathVariable("username") String username)
    {
        ResponseJSON<UserResponse> resp=new ResponseJSON<>();
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        resp.getWarnings().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getWarnings());
        if(resp.getErrors().size()==0)
        {
            Exceptional<AppUser> u = service.getUser(username);
            u.error(e -> {
                resp.addError(e.getMessage());
            }).ok(e -> {
                resp.setResp(new UserResponse(e));
            });
        }
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
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

    @RequestMapping(path = "", method = RequestMethod.POST)
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
                            body.getPassword(), null, body.getIsCometeeMember()));
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
            @RequestBody grantPermissionsDTO body,
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

    @RequestMapping(path="/{username}/submittedPapers", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<List<submittedPaperDTO>>> handle_get_submitted_papers(
            @PathVariable("username") String username,
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie,
            @RequestParam(name="status", required = false) String status)
    {
        ResponseJSON<List<submittedPaperDTO>> resp=new ResponseJSON<>();
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        List<submittedPaperDTO> papers=new ArrayList<>();
        resp.setResp(papers);
        if(resp.getErrors().size()==0) {
            Exceptional<Iterable<Paper>> paps = service.getSubmittedPapers(username);
            paps.error(e -> {
                resp.addError(e.getMessage());
            }).ok(e -> {
                for (Paper p : e)
                    if (p.getStatus().toString().equals(status) || status == null)
                        papers.add(new submittedPaperDTO(p));
            });
        }
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path="/{username}/submittedPapers/{idPaper}/reviews", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<List<reviewDTO>>> handle_get_paper_reviews(
            @PathVariable("username") String username,
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie,
            @PathVariable("idPaper") int paperId)
    {
        ResponseJSON<List<reviewDTO>> resp=new ResponseJSON<>();
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        List<reviewDTO> reviews=new ArrayList<>();
        resp.setResp(reviews);
        boolean isMyPaper=false;

        if(resp.getErrors().size()==0) {
            Exceptional<Iterable<Review>> revsOfPaper =
                    service.getReviewsOfPaper(username, paperId);
            revsOfPaper.error(e -> {
                resp.addError(e.getMessage());
            }).ok(e -> {
                for (Review r : e)
                    reviews.add(new reviewDTO(r));
            });
        }
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path="/{username}/bids/{idPaper}", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<bidDTO>> handle_get_my_bid_for_paper(
            @PathVariable("username") String username,
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie,
            @PathVariable("idPaper") int paperId)
    {
        ResponseJSON<bidDTO> resp=new ResponseJSON<>();
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());


        Exceptional<Bid> bid = service.getBidOfPaper(username,paperId);

        if(resp.getErrors().size()==0)
            bid.error(e -> {
                resp.addError(e.getMessage());
                resp.setResp(new bidDTO());
            }).ok(b -> {
                resp.setResp(new bidDTO(b));});

        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path="/{username}/bids/{idPaper}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseJSON<String>> handle_put_my_bid_for_paper(
            @PathVariable("username") String username,
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie,
            @PathVariable("idPaper") int paperId,
            @RequestBody bidDTO request)
    {
        ResponseJSON<String> resp=new ResponseJSON<>();
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        resp.setResp("");

        Exceptional<Bid> bid = service.getBidOfPaper(username,paperId);
        if(resp.getErrors().size()==0)
            bid.error(e -> {
                Exceptional<Void> res = service.addBidForPaper(username, paperId,
                                BidStatus.valueOf(request.getStatus()));
                res.error(f -> {
                    resp.addError(f.getMessage());
                });
            }).ok(e -> {
                service.addBidForPaper(username, paperId,
                        BidStatus.valueOf(request.getStatus()));
            });

        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path="/{username}/assignedForReview", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<Iterable<submittedPaperDTO>>> handle_get_assigned(
            @PathVariable("username") String username,
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie)
    {
        ResponseJSON<Iterable<submittedPaperDTO>> resp=new ResponseJSON<>();
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        List<submittedPaperDTO> body=new ArrayList<>();
        resp.setResp(body);
        if(resp.getErrors().size()==0)
        {
            Exceptional<Iterable<Paper>> pps = service.getAssignedPapers(username);
            pps.error(e -> {
                resp.addError(e.getMessage());
            }).ok(e -> {
               for(Paper p: e) {
                   body.add(new submittedPaperDTO(p));
               }
            });
        }
        return new ResponseEntity<ResponseJSON<Iterable<submittedPaperDTO>>>(resp,HttpStatus.OK);
    }

    @RequestMapping(path="/{username}/assignedPapers/{paperId}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseJSON<Void>> handle_put_assigned(
            @PathVariable("username") String username,
            @PathVariable("paperId") int paperId,
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie)
    {
        ResponseJSON<Void> resp=new ResponseJSON<>();
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        service.assignPaper(username,paperId).error(e->{resp.addError(e.getMessage());});
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path="/{username}/reviews/{paperId}", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<reviewDTO>> handle_get_review(
            @PathVariable("username") String username,
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie,
            @PathVariable("paperId") int paperId)
    {
        ResponseJSON<reviewDTO> resp=new ResponseJSON<>();
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        if(resp.getErrors().size()==0)
        {
            Exceptional<Review> reviews=service.getReviewOfPaper(username,paperId);

            reviews.ok(e -> {
                resp.setResp(new reviewDTO(e));
            }).error(e -> {
                resp.addError(e.getMessage());
            });
        }
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path="/{username}/reviews/{idPaper}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseJSON<String>> handle_put_my_review_for_paper(
            @PathVariable("username") String username,
            @CookieValue(value = "username", defaultValue = "") String usernameCookie,
            @CookieValue(value = "password", defaultValue = "") String passwordCookie,
            @PathVariable("idPaper") int paperId,
            @RequestBody reviewDTO request)
    {
        ResponseJSON<String> resp=new ResponseJSON<>();
        resp.getErrors().addAll(handle_loggedIn(usernameCookie,passwordCookie).getBody().getErrors());
        resp.setResp("");

        if(resp.getErrors().size()==0) {
            service.addReviewToPaper(username,paperId,ReviewStatus.valueOf(request.getStatus()),request.getJustification()).
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

