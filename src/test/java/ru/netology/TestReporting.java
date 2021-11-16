package ru.netology;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.DataGenerator.Registration.generateDate;

public class TestReporting {
    private RegistrationCardInfo regCardInfo;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setAll() {
        regCardInfo = DataGenerator.Registration.generateCard("ru");
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessDeliveryCard() {
        $("[placeholder='Город']").setValue(regCardInfo.getCityName());
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(generateDate(3));
        $("[type=text][name=name]").setValue(regCardInfo.getName());
        $("[type=tel][name=phone]").setValue(regCardInfo.getPhone().toString());
        $("[data-test-id=agreement]").click();
        $$("[class=button__text]").find(exactText("Запланировать")).click();
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(generateDate(5));
        $$("[class=button__text]").find(exactText("Запланировать")).click();
        $$("[class=button__text]").find(exactText("Перепланировать")).click();
        $(withText("Встреча успешно запланирована на")).find(String.valueOf(exactText("Встреча успешно запланирована на " + generateDate(5))));
    }

    @Test
    void shouldNotAdministrativeCenter() {
        $("[placeholder='Город']").setValue("Кинель-Черкассы");
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(generateDate(3));
        $("[type=text][name=name]").setValue(regCardInfo.getName());
        $("[type=tel][name=phone]").setValue(regCardInfo.getPhone().toString());
        $("[data-test-id=agreement]").click();
        $("[class=button__text]").click();
        $(withText("Запланировать")).shouldBe(visible, Duration.ofSeconds(5));
        $(withText("Доставка в выбранный город недоступна")).shouldBe(visible);
    }

    @Test
    void shouldEmptyFieldCity() {
        $("[placeholder='Город']").setValue(" ");
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(generateDate(3));
        $("[type=text][name=name]").setValue(regCardInfo.getName());
        $("[type=tel][name=phone]").setValue(regCardInfo.getPhone().toString());
        $("[data-test-id=agreement]").click();
        $("[class=button__text]").click();
        $(withText("Запланировать")).shouldBe(visible, Duration.ofSeconds(5));
        $(withText("Поле обязательно для заполнения")).shouldBe(visible);
    }

    @Test
    void shouldInvalidDate() {
        $("[placeholder='Город']").setValue(regCardInfo.getCityName());
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(generateDate(1));
        $("[type=text][name=name]").setValue(regCardInfo.getName());
        $("[type=tel][name=phone]").setValue(regCardInfo.getPhone().toString());
        $("[data-test-id=agreement]").click();
        $("[class=button__text]").click();
        $(withText("Запланировать")).shouldBe(visible, Duration.ofSeconds(5));
        $(withText("Заказ на выбранную дату невозможен")).shouldBe(visible);
    }

    @Test
    void shouldEmptyFieldDate() {
        $("[placeholder='Город']").setValue(regCardInfo.getCityName());
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(" ");
        $("[type=text][name=name]").setValue(regCardInfo.getName());
        $("[type=tel][name=phone]").setValue(regCardInfo.getPhone().toString());
        $("[data-test-id=agreement]").click();
        $("[class=button__text]").click();
        $(withText("Запланировать")).shouldBe(visible, Duration.ofSeconds(5));
        $(withText("Неверно введена дата")).shouldBe(visible);
    }

    @Test
    void shouldEmptyFieldName() {
        $("[placeholder='Город']").setValue(regCardInfo.getCityName());
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(generateDate(3));
        $("[type=text][name=name]").setValue(" ");
        $("[type=tel][name=phone]").setValue(regCardInfo.getPhone().toString());
        $("[data-test-id=agreement]").click();
        $("[class=button__text]").click();
        $(withText("Запланировать")).shouldBe(visible, Duration.ofSeconds(5));
        $(withText("Поле обязательно для заполнения")).shouldBe(visible);
    }

    @Test
    void shouldEmptyFieldPhone() {
        $("[placeholder='Город']").setValue(regCardInfo.getCityName());
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(generateDate(3));
        $("[type=text][name=name]").setValue(regCardInfo.getName());
        $("[type=tel][name=phone]").setValue("");
        $("[data-test-id=agreement]").click();
        $("[class=button__text]").click();
        $(withText("Запланировать")).shouldBe(visible, Duration.ofSeconds(5));
        $(withText("Поле обязательно для заполнения")).shouldBe(visible);
    }

    @Test
    void shouldWithoutInstallCheckbox() {
        $("[placeholder='Город']").setValue(regCardInfo.getCityName());
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(generateDate(3));
        $("[type=text][name=name]").setValue(regCardInfo.getName());
        $("[type=tel][name=phone]").setValue(regCardInfo.getPhone().toString());
        $("[class=button__text]").click();
        $(withText("Запланировать")).shouldBe(visible, Duration.ofSeconds(5));
        $("[data-test-id=agreement].input_invalid .checkbox__text").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }
}
