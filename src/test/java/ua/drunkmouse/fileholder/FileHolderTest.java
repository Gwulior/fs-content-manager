package ua.drunkmouse.fileholder;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Dreval Viacheslav on 05.03.2017.
 */
public class FileHolderTest {

    private static final String pathPrefix = "D:\\1app_test";
    private static final int maxFilesInFolder = 25;

    private FileHolder fileHolder;

    @Before
    public void prepareHolder() {
        fileHolder = new FileHolder(pathPrefix, maxFilesInFolder);
    }


    @Test
    public void Test() throws IOException {

        byte[] arr = new byte[]{1, 2, 3};
        fileHolder.saveFile(arr);
    }

    @Test
    public void testLoad() throws Exception {
//        byte[] bytes = fileHolder.loadFile("");

    }
}
