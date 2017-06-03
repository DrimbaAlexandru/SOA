package service;

import com.company.Application;
import com.company.domain.Bid;
import com.company.domain.BidStatus;
import com.company.service.UserService;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
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

import java.util.Optional;

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

    public static final String DATASET = "/datasets/users-items.xml";

    @Autowired
    private UserService service;

    @Test
    public void testBidOnPaper() {
        service.addBidForPaper("Alex-D-TC", 2, BidStatus.PLEASED);

        Optional<Bid> b = service.getBidOfPaper("Alex-D-TC", 2);

        Assert.assertTrue(b.isPresent() && b.get().getStatus().equals(BidStatus.PLEASED));
    }
}
