package guru.qa.config;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;

public class WebDriverProvider{

    public void setWebDriverConfiguration() {
        WebDriverConfig config = ConfigFactory.create(WebDriverConfig.class, System.getProperties());
        Configuration.browser = config.getBrowserName();
        Configuration.browserVersion = config.getBrowserVersion();
        Configuration.browserPosition = config.getBrowserPosition();
        Configuration.browserSize = config.getBrowserSize();
        Configuration.baseUrl = config.getBaseUrl();
        RestAssured.baseURI = config.getBaseUri();
        if (config.getRemoteUrl() != null) {
            Configuration.remote = config.getRemoteUrl();
        }
    }
}
