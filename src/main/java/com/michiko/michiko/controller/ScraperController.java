package com.michiko.michiko.controller;

import com.michiko.michiko.dto.DownloadManga;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.awt.image.ByteLookupTable;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.regex.*;

@Controller
public class ScraperController {

    @GetMapping("/statusCheck")
    public ResponseEntity statusCheck(){

        // TODO: make this work with the driver in your project (linux...)
//        System.setProperty("webdriver.chrome.driver", Objects.requireNonNull(this.getClass().getClassLoader().getResource("chromedriver.exe")).getPath());
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\The one and only\\Desktop\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();

        driver.get("https://kissmanga.org");
        if(driver.getTitle().equals("KissManga - Read manga online in high quality")){

        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not reach kissmanga.org");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }


    @PostMapping("/downloadTitle")
    public ResponseEntity downloadManga(@RequestBody DownloadManga downloadManga){
        //        TODO: make this work with the driver in your project (linux...)
        //        System.setProperty("webdriver.chrome.driver", Objects.requireNonNull(this.getClass().getClassLoader().getResource("chromedriver.exe")).getPath());
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\The one and only\\Desktop\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        driver.get("https://kissmanga.org");

        String title = "";
        // check if kissmanga is up
        if(driver.getTitle().equals("KissManga - Read manga online in high quality")){

            WebElement search = driver.findElement(By.id("keyword"));

            search.sendKeys(downloadManga.getTitle());
            WebElement searchButton = driver.findElement(By.id("imgSearch"));
            searchButton.click();

            // in search results page find matching link
            List<WebElement> links = driver.findElements(By.className("item_movies_link"));

            Optional<WebElement> optionalMatchingLinks = links.stream().filter(x ->
                    x.getText().equals(downloadManga.getTitle())
            ).collect(Collectors.toList()).stream().findFirst();

            if(optionalMatchingLinks.isPresent()){
                WebElement targetLink =  optionalMatchingLinks.get();
                targetLink.click();
                WebElement titleOnPage = driver.findElement(By.className("bigChar"));

                // click on first chapter and download all images chapter by chapter until complete
                Optional<WebElement> optionalWebElement = driver.findElements(By.className("item_static")).stream().findFirst();
                if(optionalMatchingLinks.isPresent()){
                    WebElement webElement = optionalWebElement.get();
                   String mangaStatus =  webElement.getText();
                   if(!mangaStatus.contains("Completed")){
                       // TODO: add to ongoing table
                       System.out.print("");
                   }

                   //HERE
                    //TODO: WIP
//                    String selector = "[title=" + "Read " + downloadManga.getTitle() + " online ]";
//                    int chapterCount = webElement.findElements(By.cssSelector(selector)).size();
//                    WebElement firstChapter = webElement.findElement(By.xpath("/html/body/div[2]/div[5]/div[2]/div[3]/div[2]/div[2]/div[2]/div["+ (chapterCount + 1) + "]/div[1]/h3/a"));

                    // start at the very first link in the list
                    // click on it
                    // download all the images
                    // go to next page
                    // how to know your on the last image or last chapter?
                }

                title = titleOnPage.getText(); // test line uneeded
            }else{
//           throw an error and log it
            }
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not reach kissmanga.org");
        }

        // enter title in search
        // click search
        // find and click link that matches title
        // add to the ongoing table if the status is ongoing
        // downlaod all images (try catch)
        return ResponseEntity.ok("Success grabbed page: " + title);

    }

}
