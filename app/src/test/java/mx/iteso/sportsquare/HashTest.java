package mx.iteso.sportsquare;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class HashTest {

    @Test
    public void sha1() {
        assertEquals("268a4e78f70a4c03640bb9cc916764bbba88525b", Hash.sha1("El Fede"));
        assertEquals("a276565430072e1976a7f5f296efcd9da6f649b7", Hash.sha1("El fede"));
    }

    /*
        @Test
        public void md5() {
            assertEquals("b8e5a4cbccf866100a3318a66bef5d2d", Hash.md5("El Fede"));
            assertEquals("9b3407d26d4d564d1c1f671a5c40b20f", Hash.md5("El fede"));
        }
    */
    @Test
    public void failure() {
        assertNull(Hash.getHash("x","asd"));
    }

}