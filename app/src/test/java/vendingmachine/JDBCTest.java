package vendingmachine;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JDBCTest {

    @Test
    public void test1() {
        JDBC jdbc = new JDBC();
        jdbc.initDB();
        assertEquals(jdbc.getCard("abc").getNumber(), null);
        assertEquals(jdbc.getCash("$100").getValue(), 100.0);
        assertEquals(jdbc.getCash("$100").getAmount(), 0);
    }


    @Test
    public void test2() {
        JDBC jdbc = new JDBC();
        jdbc.initDB();
        assertEquals(jdbc.getCard("abc").getNumber(), null);
    }


    @Test
    public void test3() {
        JDBC jdbc = new JDBC();
        jdbc.initDB();
        assertEquals(jdbc.getCashAll().size(), 13);
        assertEquals(jdbc.getProduct("abc").getName(), null);
    }


    @Test
    public void test4() {
        JDBC jdbc = new JDBC();
        jdbc.initDB();
        assertEquals(jdbc.getProduct("Mars").getName(), "Mars");
        assertEquals(jdbc.getProduct("Mars").getId(), 201);
        assertEquals(jdbc.getProduct("Mars").getPrice(), 1.0);

    }
}
