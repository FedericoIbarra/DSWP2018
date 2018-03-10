package mx.iteso.sportsquare;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class HashTest {
    @Test
    public void hashing() {
        assertEquals("268a4e78f70a4c03640bb9cc916764bbba88525b", Hash.sha1("El Fede"));
        assertEquals("a276565430072e1976a7f5f296efcd9da6f649b7", Hash.sha1("El fede"));
    }
}