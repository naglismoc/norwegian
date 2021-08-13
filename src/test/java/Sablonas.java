import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Sablonas {
    public WebDriver driver;


    @Test
    public void pirmasTestas() throws InterruptedException {
        String year = "2021";
        String month = "11";
        String from = "OSL";
        String to = "FCO";
        driver.get("https://www.norwegian.com/api/booking/dateAvailabilitySearch?channelId=IP&culture=en&marketCode=us&adultCount=1&origin="+from+"&destination="+to+"&outboundDate="+year+"-"+month+"-01");
        String[] days = days(driver);
        driver.quit();

        ArrayList<Flight> flights = new ArrayList<>();
        for (int i = 1; i < days.length ;i++) {
            driver = new ChromeDriver();
            String day = (days[i].length()==2)?days[i]: "0"+days[i];
            driver.get("https://www.norwegian.com/uk/ipc/availability/avaday?AdultCount=1&A_City="+to+"&D_City="+from+"&D_Month="+year+month+"&D_Day="+day+"&IncludeTransit=true&TripType=1");


            flights.add(getData(driver));
            driver.quit();
        }

    }

    public static Flight getData(WebDriver driver){
         Flight f = new Flight();
        f.depart = driver.findElement(By.xpath("//*[@id=\"avaday-outbound-result\"]/div/div/div[2]/div/table/tbody/tr[2]/td[1]/div")).getText();
        f.arrival = driver.findElement(By.xpath("//*[@id=\"avaday-outbound-result\"]/div/div/div[2]/div/table/tbody/tr[2]/td[2]/div")).getText();
        f.dTime = driver.findElement(By.xpath("//*[@id=\"avaday-outbound-result\"]/div/div/div[2]/div/table/tbody/tr[1]/td[1]/div")).getText();
        f.aTime = driver.findElement(By.xpath("//*[@id=\"avaday-outbound-result\"]/div/div/div[2]/div/table/tbody/tr[1]/td[2]/div")).getText();
        f.price = driver.findElement(By.xpath("//*[@id=\"avaday-outbound-result\"]/div/div/div[2]/div/table/tbody/tr[1]/td[5]/div/label")).getText();
        return  f;
    }

    public static String[] days (WebDriver driver){
        String[] dataB = driver.findElements(By.tagName("html")).get(0).getText().split("<Day>");
        String[] days = new String[dataB.length-1];
        for (int i = 1; i<dataB.length;i++) {
            days[i-1] = dataB[i].split("<")[0];
            System.out.println(days[i-1]);
        }
        Arrays.sort(days);
        return days;
    }
    @BeforeMethod
    public void every() {
        System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver92.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

    }

}
