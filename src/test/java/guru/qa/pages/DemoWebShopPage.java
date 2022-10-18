package guru.qa.pages;

import com.codeborne.selenide.WebDriverRunner;
import guru.qa.data.DemoWebShopData;
import guru.qa.helpers.CustomApiListener;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;


public class DemoWebShopPage {

    private final String logAUTH = "NOPCOMMERCE.AUTH";
    private final String paramAndCookieName = "__RequestVerificationToken";
    private final String paramValue = "Bk_VNMOru0fYyZqQGixkv7hEuDYD69eo6yY9rbBc3xhDeNEl0EXEMCG1h_R-1EprBDAUH1tYENLrGTQ214WSZy62iOEEJ69ajUfEqygF9sQ1";
    private final String cookieValue = "B9YnVwzj3i8mZEckCofXRALssxrTmds_cXXQILeixVoug6GCDbvCtYw-xlqUfZ_q1UtBhokXSOJBY10q3bdlYrS_ueR1y1UEFYQ_-hAjHfI1";
    DemoWebShopData data;
    String getCookie;

    public DemoWebShopPage() {
        data = new DemoWebShopData();
    }

    public void registrationApi() {
        step("Регистрация нового пользователя", () -> {
            return given()
                    .filter(CustomApiListener.withCustomTemplates())
                    .formParam("Gender", data.gender.charAt(0))
                    .formParam("FirstName", data.firstName)
                    .formParam("LastName", data.lastName)
                    .formParam("Email", data.email)
                    .formParam("Password", data.password)
                    .formParam("ConfirmPassword", data.password)
                    .formParam(paramAndCookieName, paramValue)
                    .cookie(paramAndCookieName, cookieValue)
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
            $("#Email").shouldHave(value(data.email));
        });
    }
}
