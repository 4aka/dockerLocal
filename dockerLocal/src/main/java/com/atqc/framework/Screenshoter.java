package com.atqc.framework;

import io.qameta.allure.Allure;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.NoAlertPresentException;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.atqc.framework.Driver.getDriver;

@Log4j2
public class Screenshoter {

    public static synchronized void takeScreenshot(String name) {
        String path = "./target/screenshots/" + name + ".png";
        try {
            Screenshot scr = shoot();
            File screenshot = writeToFS(path, scr);
            attachToAllure(name, screenshot);
        } catch (Exception e) {
            try{
                log.error(e.getMessage(), e);
                getDriver().switchTo().alert().accept();
            }
            catch(NoAlertPresentException noAlert) {
                log.error("Exception while taking screenshot " + e.getMessage() + ".");
            }
        }
    }

    public static void takeScreenshot() {
        takeScreenshot(Long.toString(System.currentTimeMillis()));
    }

    private static File writeToFS(String path, Screenshot scr) throws IOException {
        File outputFile = new File(Thread.currentThread().getId() + ".png");
        ImageIO.write(scr.getImage(), "PNG", outputFile);
        File screenshot = new File(path);
        FileUtils.copyFile(outputFile, screenshot);
        outputFile.deleteOnExit();
        return screenshot;
    }

    private static Screenshot shoot() {
        Screenshot scr;
        scr = new AShot().shootingStrategy(ShootingStrategies.simple()).takeScreenshot(getDriver());
        log.info("Screenshot was taken");
        return scr;
    }

    private static void attachToAllure(String name, File file) {
        try (InputStream is = Files.newInputStream(Paths.get(file.getAbsolutePath()))) {
            Allure.addAttachment(name, is);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}