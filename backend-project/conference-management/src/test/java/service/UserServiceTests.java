package service;

import com.company.Application;
import com.company.domain.*;
import com.company.service.UserService;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Created by AlexandruD on 03-Jun-17.
 */
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT,
        classes = Application.class)
@DatabaseSetup(UserServiceTests.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL,
        value = { UserServiceTests.DATASET })
@DirtiesContext
public class UserServiceTests {

    public static final String DATASET = "classpath:datasets/users-items.xml";

    @Autowired
    private UserService service;

    @Test
    public void testBidOnPaper() {
        service.addBidForPaper("Alex-D-TC", 2, BidStatus.PLEASED);

        Optional<Bid> b = service.getBidOfPaper("Alex-D-TC", 2);

        Assert.assertTrue(b.isPresent() && b.get().getStatus().equals(BidStatus.PLEASED));
    }

    @Test
    public void testAssignPaper() {

        String username = "alex-trs";
        int paperId = 2;

        service.assignPaper(username, paperId);

        Optional<Iterable<Paper>> assOpt = service.getAssignedPapers(username);

        Assert.assertTrue(assOpt.isPresent());

        List<Paper> assPaperList =  StreamSupport
                .stream(assOpt.get().spliterator(), false)
                .collect(Collectors.toList());

        Assert.assertTrue(assPaperList.size() == 1 &&
                assPaperList.stream().filter(e -> e.getId().equals(paperId)).count() == 1);
    }

    @Test
    public void testUpdatePrivilegesOfConference() {
        String username = "Alex-D-TC";
        int confId = 1;
        Privileges privs = new Privileges();
        privs.setIsAuthor(false);
        privs.setIsChair(true);
        privs.setIsCoChair(false);
        privs.setIsPCMember(true);

        service.updatePrivilegesOfConference(username, confId, privs);

        Optional<Privileges> privOpt = service.getConferencePrivileges(username, confId);

        AppUser au = new AppUser();
        Conference onf = new Conference();

        au.setUsername("Alex-D-TC");
        onf.setId(confId);

        privs.setUser(au);
        privs.setConference(onf);

        Assert.assertTrue(privOpt.isPresent());
        Assert.assertTrue(privilegeEquals(privs, privOpt.get()));
    }

    @Test
    public void testUpdateUser() {
        String username = "Alex-D-TC",
                affiliation = "LOL",
                email = "alexandru.d.thcr@gmail.com";
        AppUser newData = new AppUser();
        newData.setAffiliation(affiliation);
        newData.setEmail(email);

        service.updateUser(username, newData);

        Optional<AppUser> user = service.getUser(username);

        Assert.assertTrue(user.isPresent());
        Assert.assertTrue(user.get().getUsername().equals(username) &&
                            user.get().getEmail().equals(email) &&
                            user.get().getAffiliation().equals(affiliation));
    }

    @Test
    public void testAddUser() {
        String password = "pass";
        AppUser newUser = new AppUser();
        newUser.setUsername("new-user");
        newUser.setName("alexei");
        newUser.setAffiliation("no affiliation");
        newUser.setEmail("no-email@gmail.com");
        newUser.setWebpage("https://www.pornhub.com");
        newUser.setPassword(password);
        newUser.setIsSuperUser(false);
        newUser.setIsCometeeMember(false);
        service.addUser(newUser);

        newUser.setPassword(password);

        Optional<AppUser> auOpt = service.getUser(newUser.getUsername());

        Assert.assertTrue(auOpt.isPresent());

        AppUser found = auOpt.get();

        Assert.assertTrue(appUserEqual(newUser, found));
    }

    @Test
    public void getPapersOfStatusTestNoneOfStatus() {
        Optional<Iterable<Paper>> assOpt =
                service.getPapersOfStatus("Alex-D-TC", PaperStatus.DECLINED);

        Assert.assertTrue(assOpt.isPresent());
        Assert.assertTrue(StreamSupport.stream(assOpt.get().spliterator(), false)
                .count() == 0);
    }

    @Test
    public void getPaperOfStatusTest() {
        Optional<Iterable<Paper>> assOpt =
                service.getPapersOfStatus("Alex-D-TC", PaperStatus.SUBMITTED);

        Assert.assertTrue(assOpt.isPresent());

        List<Paper> assPaperList = StreamSupport.stream(assOpt.get().spliterator(), false)
                .collect(Collectors.toList());

        Assert.assertTrue(assPaperList.size() == 2);
        Assert.assertTrue(assPaperList.stream()
                .filter(e -> e.getId().equals(1) || e.getId().equals(3))
                .count() == 2);
    }

    @Test
    public void getPapersOfSTatusTestNoUser() {
        Assert.assertFalse(service.getPapersOfStatus("RANDOM", PaperStatus.SUBMITTED)
                .isPresent());
    }

    @Test
    public void getSubmittedPapersNoUser() {
        Assert.assertFalse(service.getSubmittedPapers("RANDOM").isPresent());
    }


    @Test
    public void getSubmittedPapersTest() {
        Optional<Iterable<Paper>> assOpt =
                service.getSubmittedPapers("Alex-D-TC");

        Assert.assertTrue(assOpt.isPresent());
        Assert.assertTrue(StreamSupport.stream(assOpt.get().spliterator(), false)
                .count() == 2);
        Assert.assertTrue(StreamSupport.stream(assOpt.get().spliterator(), false)
                .filter(e -> e.getId().equals(1) || e.getId().equals(3))
                .count() == 2);
    }

    @Test
    public void getReviewsOfPaperNoUser() {
        Assert.assertFalse(service.getReviewsOfPaper("RANDOM", 3).isPresent());
    }

    @Test
    public void getReviewsOfPaperNoPaper() {
        Assert.assertFalse(service.getReviewsOfPaper("Alex-D-TC", 4).isPresent());
    }

    @Test
    public void getReviewsOvPaperPaperNotSubmittedByUser() {
        Assert.assertFalse(service.getReviewsOfPaper("Alex-D-TC", 2).isPresent());
    }

    @Test
    public void getReviewsOfPaper() {
        Optional<Iterable<Review>> reviews = service.getReviewsOfPaper("Alex-D-TC", 1);

        Assert.assertTrue(reviews.isPresent());

        List<Review> revList = StreamSupport.stream(reviews.get().spliterator(), false)
                .collect(Collectors.toList());

        Assert.assertTrue(revList.size() == 1);
        Assert.assertTrue(revList.stream().filter(e -> e.getId().equals(1)).count() == 1);
    }

    private boolean appUserEqual(AppUser a, AppUser b) {
        return a.getUsername().equals(b.getUsername()) &&
                a.getAffiliation().equals(b.getAffiliation()) &&
                a.getEmail().equals(b.getEmail()) &&
                a.getWebpage().equals(b.getWebpage()) &&
                a.getIsSuperUser().equals(b.getIsSuperUser()) &&
                a.getIsCometeeMember().equals(b.getIsCometeeMember()) &&
                service.login(a.getUsername(), a.getPassword());
    }

    private boolean privilegeEquals(Privileges a, Privileges b) {

        return a.getIsAuthor().equals(b.getIsAuthor()) &&
                a.getIsChair().equals(b.getIsChair()) &&
                a.getIsCoChair().equals(b.getIsCoChair()) &&
                a.getIsPCMember().equals(b.getIsPCMember()) &&
                a.getUser().getUsername().equals(b.getUser().getUsername()) &&
                a.getConference().getId().equals(b.getConference().getId());

    }

}
