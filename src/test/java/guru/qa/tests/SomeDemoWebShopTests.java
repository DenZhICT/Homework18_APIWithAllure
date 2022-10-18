package guru.qa.tests;


import guru.qa.pages.DemoWebShopPage;
import org.junit.jupiter.api.Test;

public class SomeDemoWebShopTests extends TestBase {

    DemoWebShopPage page = new DemoWebShopPage();

    @Test
    void registrationTest() {
        page.registrationApi();
        page.loginApi();
        page.checkLogin();
    }

    @Test
    void accountChangeTest() {
        page.registrationApi();
        page.loginApi();
        page.editAndCheckAccountInfo();
    }
}
