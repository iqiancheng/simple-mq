package me.qiancheng.simple.mq;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

import me.qiancheng.simple.mq.utils.Utils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


public class TestUtils {


    @Test(expected = IllegalArgumentException.class)
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
