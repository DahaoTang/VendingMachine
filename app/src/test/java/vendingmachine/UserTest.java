package vendingmachine;


import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {


    @Test
    public void test1() {
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product(1,ProductType.CANDY,"Candy",20.0,30,20));
        User user = new User("tom","123456", products, UserType.CASHIER, "ABC");
        assertEquals(user.getName(), "tom");
        assertEquals(user.getPassword(), "123456");
        assertEquals(user.getTypeString(), "CASHIER");
        assertEquals(user.getCardName(), "ABC");
        assertEquals(user.getRecentProducts(), products);
    }


    @Test
    public void test2() {
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product(1,ProductType.CANDY,"Candy",20.0,30,20));
        User user = new User("tom","123456", products, UserType.CASHIER, "ABC");
        assertEquals(user.getRecentProducts(), products);
        user.setType(UserType.NORMAL);
        assertEquals(user.getTypeString(), "NORMAL");
        user.setType(UserType.OWNER);
        assertEquals(user.getTypeString(), "OWNER");
        user.setType(UserType.SELLER);
        assertEquals(user.getTypeString(), "SELLER");
    }

    @Test
    public void test3() {
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product(1,ProductType.CANDY,"Candy",20.0,30,20));
        products.add(new Product(2,ProductType.CHOCOLATE,"CHOCOLATE",20.0,30,25));
        products.add(new Product(3,ProductType.CHIP,"CHIP",22.0,30,30));
        products.add(new Product(4,ProductType.DRINK,"DRINK",23.0,30,40));
        products.add(new Product(5,ProductType.DRINK,"tt",12.0,30,40));

        User user = new User("tom","123456", products, UserType.CASHIER, "ABC");
        String expected = "Name: tom\n" +
                "Password: 123456\nRencet Products: Candy, CHOCOLATE, CHIP, DRINK, tt, Type: CASHIER, CardName: ABC";
        assertEquals(user.toString(), expected);
    }

}
