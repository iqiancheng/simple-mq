package me.qiancheng.simple.mq;

import me.qiancheng.simple.mq.utils.Utils;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotSame;


public class TestSuits {


    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSerialize() {
        ArrayList<Bar> list = new ArrayList<Bar>();
        list.add(new Bar());
        Utils.serialize(list);
    }


    @Test
    public void testCopy() {
        Foo f = new Foo();
        Foo copy = (Foo) Utils.copy(f);
        assertNotSame(f, copy);
    }


    @Test
    public void testDeletedirectory() throws IOException {

        String tmp = System.getProperty("java.io.tmpdir");

        File dir = new File(tmp, "testdir");
        dir.mkdir();

        File file = new File(dir, "filename");
        file.createNewFile();

        Utils.deleteDirectory(dir);
        assertFalse(dir.exists());
    }
}

class Foo implements Serializable {

}

class Bar {

}
