package repository;

import com.company.Application;
import com.company.domain.Paper;
import com.company.repository.UserRepository;
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Created by AlexandruD on 02-Jun-17.
 */
/*@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT,
        classes = Application.class)
@DatabaseSetup(UserRepositoryTests.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL,
        value = { UserRepositoryTests.DATASET })
@DirtiesContext
*/
public class UserRepositoryTests {

    protected static final String DATASET = "classpath:datasets/users-items.xml";

    @Test
    public void trueTrue() {

    }

    /*
    @Autowired
    private UserRepository repo;

    @Test
    public void testFindUser() {
        Assert.assertNotNull(repo.findByUsername("Alex-D-TC"));
    }

    @Test
    public void testFindUserNoUser() {
        Assert.assertNull(repo.findByUsername("RANDOM"));
    }

    @Test
    public void testGetSubmittedPapers() {
        Iterable<Paper> assPapers = repo.getSubmittedPapers("Alex-D-TC");
         Assert.assertNotNull(assPapers);
        List<Paper> assPapersList = StreamSupport.stream(assPapers.spliterator(), false)
                .collect(Collectors.toList());
        Assert.assertEquals(2, assPapersList.size());
        Assert.assertEquals(2,
                assPapersList.stream()
                        .filter(e -> e.getId() == 1 || e.getId() == 3)
                        .collect(Collectors.toList())
                        .size());
    }

    @Test
    public void testGetBidsOfPaper() {
        Assert.assertNull(repo.getBidOfPaper("Alex-D-TC", 1));
        Assert.assertNull(repo.getBidOfPaper("Alex-D-TC", 3));
        Assert.assertNull(repo.getBidOfPaper("alex-trs", 2));
        Assert.assertNotNull(repo.getBidOfPaper("Alex-D-TC", 2));
        Assert.assertNotNull(repo.getBidOfPaper("alex-trs", 1));
        Assert.assertNotNull(repo.getBidOfPaper("alex-trs", 3));
    }

    @Test
    public void testUserExistsTrue() {
        Assert.assertTrue(repo.userExists("Alex-D-TC"));
    }

    @Test
    public void testUserExistsFalse() {
        Assert.assertFalse(repo.userExists("RANDOM"));
    }
    */
}
