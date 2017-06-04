package dropbox;

import com.company.Application;
import com.company.utils.RemoteFileManager;
import com.company.utils.dropbox.DropboxManagerRemote;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Created by AlexandruD on 04-Jun-17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT,
        classes = Application.class)
public class DropboxManagerTest {

    @Autowired
    private RemoteFileManager manager;

    //@Test
    // Ran only once for ease of feature checking
    public void testFileDownload() {
        Optional<byte[]> dataOpt =
                manager.getFileData("/Alex-D-TC---4290698962142233649.pdf");

        Assert.assertTrue(dataOpt.isPresent());
    }
}
