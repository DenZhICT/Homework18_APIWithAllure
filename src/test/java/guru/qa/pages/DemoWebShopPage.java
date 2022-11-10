package guru.qa.pages;

import com.codeborne.selenide.WebDriverRunner;
import guru.qa.data.DemoWebShopData;
import guru.qa.helpers.CustomApiListener;
import io.restassured.response.Response;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;


public class DemoWebShopPage {

    private final String logAUTH = "NOPCOMMERCE.AUTH";
    private final String tokenName = "__RequestVerificationToken";
    DemoWebShopData data;
    String getCookie;

    public DemoWebShopPage() {
        data = new DemoWebShopData();
    }

    private Response getRegister() {
        return given()
                .filter(CustomApiListener.withCustomTemplates())
                .when()
                .get("/register")
                .then()
                .extract()
                .response();
    }

    public void registrationApi() {
        Response register = getRegister();
        String token = register.htmlPath().getString("**.find{it.@name == '__RequestVerificationToken'}.@value");
        String cookie = register.cookie(tokenName);

        step("Регистрация нового пользователя", () -> {
            given()
                    .filter(CustomApiListener.withCustomTemplates())
                    .formParam("Gender", data.gender.charAt(0))
                    .formParam("FirstName", data.firstName)
                    .formParam("LastName", data.lastName)
                    .formParam("Email", data.email)
                    .formParam("Password", data.password)
                    .formParam("ConfirmPassword", data.password)
                    .formParam(tokenName, token)
                    .cookie(tokenName, cookie)
                    .when()
                    .post("/register")
                    .then()
                    .statusCode(302);
        });
    }

    public void loginApi() {
        step("Получение Cookie этого пользователя", () -> {
            getCookie =
                    given()
                            .filter(CustomApiListener.withCustomTemplates())
                            .formParam("Email", data.email)
                            .formParam("Password", data.password)
                            .when()
                            .post("/login")
                            .then()
                            .statusCode(302)
                            .extract()
                            .cookie(logAUTH);
        });
    }

    private void dropCookie() {
        step("Подкладка Cookie", () -> {
            open("/Themes/DefaultClean/Content/images/logo.png");
            Cookie authCookie = new Cookie(logAUTH, getCookie);
            WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
        });
    }

    public void checkLogin() {
        dropCookie();
        step("Проверка создание пользователя", () -> {
            open("");
            $(".account").shouldHave(text(data.email));
        });
    }

    public void editAndCheckAccountInfo() {
        dropCookie();
        data = new DemoWebShopData();
        step("Простановка новых данных", () -> {
            open("");
            $(".account").click();
            $(".fieldset").find(byText(data.gender)).click();
            $("#FirstName").setValue(data.firstName);
            $("#LastName").setValue(data.lastName);
            $("#Email").setValue(data.email);
            $(".save-customer-info-button").click();
        });
        refresh();
        step("Проверка изменения данных", () -> {
            $("#FirstName").shouldHave(value(data.firstName));
            $("#LastName").shouldHave(value(data.lastName));
            $(".account").shouldHave(text(data.email));
        });
    }
}
