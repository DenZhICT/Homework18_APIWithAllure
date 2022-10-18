package guru.qa.data;

import com.github.javafaker.Faker;

public class DemoWebShopData {
    public String firstName, lastName, email, gender, password;

    public DemoWebShopData() {
        this.firstName = randomFirstName();
        this.lastName = randomLastName();
        this.email = randomEmail();
        this.gender = randomGender();
        this.password = randomPassword();
    }

    private String randomGender() {
        String[] list = {"Male", "Female"};
        int randomIndex = new Faker().random().nextInt(list.length);
        return list[randomIndex];
    }

    private String randomEmail() {
        return new Faker().internet().emailAddress();
    }

    private String randomLastName() {
        return new Faker().name().lastName();
    }

    private String randomFirstName() {
        return new Faker().name().firstName();
    }

    private String randomPassword() {
        return new Faker().internet().password();
    }

}
