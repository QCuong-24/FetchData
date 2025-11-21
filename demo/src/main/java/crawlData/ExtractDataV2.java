package crawlData;

import com.google.gson.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class ExtractDataV2 {
    static String filePath = "src/main/java/crawlData/Patient.json";
    static List<String> idList = List.of("ctl04_txtCodeID",
            "ctl04_txtCustomerName",
            "ctl04_txtIDcard",
            "ctl04_txtDateOfBirth",
            "ctl04_txtBirth",
            "ctl04_txtAge",
            "ctl04_optNam",
            "ctl04_optNu",
            "ctl04_txtInsureID",
            "ctl04_txtFamilyID",
            "ctl04_txtMobile",
            "ctl04_txtTel",
            "ctl04_txtEmail",
            "ctl04_drpJobE",
            "ctl04_txtAddress",
            "ctl04_drpProvince",
            "ctl04_drpDistrict",
            "ctl04_drpStreet",
            "ctl04_drpNation",
            "ctl04_drpClinicE",
            "ctl04_drpEmpE",
            "ctl04_drpSourceE",
            "ctl04_txtParentID",
            "ctl04_chkBleed",
            "ctl04_chkReact",
            "ctl04_chkAllergic",
            "ctl04_chkCoaHA",
            "ctl04_chkCardiovascular",
            "ctl04_chkDiabetes",
            "ctl04_chkGastric",
            "ctl04_chkLung",
            "ctl04_chkCatching",
            "ctl04_txtPathology",
            "ctl04_txtNotes"
    );
    static Map<String, String> map = Map.ofEntries(
            Map.entry("ctl04_txtCodeID", "Mã"),
            Map.entry("ctl04_txtCustomerName", "Tên khách hàng"),
            Map.entry("ctl04_txtIDcard", "CCCD"),
            Map.entry("ctl04_txtDateOfBirth", "Ngày sinh"),
            Map.entry("ctl04_txtBirth", "Năm sinh"),
            Map.entry("ctl04_txtAge", "Tuổi"),
            Map.entry("ctl04_optNam", "Nam"),
            Map.entry("ctl04_optNu", "Nữ"),
            Map.entry("ctl04_txtInsureID", "Thẻ BHYT"),
            Map.entry("ctl04_txtFamilyID", "Mã gia đình"),
            Map.entry("ctl04_txtMobile", "Di động"),
            Map.entry("ctl04_txtTel", "Cố định"),
            Map.entry("ctl04_txtEmail", "Email"),
            Map.entry("ctl04_drpJobE", "Nghề nghiệp"),
            Map.entry("ctl04_txtAddress", "Địa chỉ"),
            Map.entry("ctl04_drpProvince", "Tỉnh/TP"),
            Map.entry("ctl04_drpDistrict", "Quận/Huyện"),
            Map.entry("ctl04_drpStreet", "Phường/Xã"),
            Map.entry("ctl04_drpNation", "Quốc tịch"),
            Map.entry("ctl04_drpClinicE", "Phòng khám"),
            Map.entry("ctl04_drpEmpE", "Phụ trách"),
            Map.entry("ctl04_drpSourceE", "Nguồn"),
            Map.entry("ctl04_txtParentID", "Mã giới thiệu"),
            Map.entry("ctl04_chkBleed", "Chảy máu lâu"),
            Map.entry("ctl04_chkReact", "Phản ứng thuốc"),
            Map.entry("ctl04_chkAllergic", "Dị ứng, thấp khớp"),
            Map.entry("ctl04_chkCaoHA", "Cao HA"),
            Map.entry("ctl04_chkCardiovascular", "Tim mạch"),
            Map.entry("ctl04_chkDiabetes", "Tiểu đường"),
            Map.entry("ctl04_chkGastric", "Dạ dày, tiêu hóa"),
            Map.entry("ctl04_chkLung", "Bệnh phổi"),
            Map.entry("ctl04_chkCatching", "Bệnh truyền nhiễm"),
            Map.entry("ctl04_txtPathology", "Bệnh khác"),
            Map.entry("ctl04_txtNotes", "Ghi chú")
    );

    static void addJSon(Document doc) {
        JsonObject json = new JsonObject();

        for (String id : idList) {
            Element element = doc.getElementById(id);
            if (element == null) continue;
            String tagName = element.tagName();
            String type = element.attr("type");

            if (!tagName.equals("input") || type.equals("text")) {
                String value = (element != null) ? element.attr("value") : null; // keep null if not present
                json.addProperty(map.get(id), value);
            } else {
                if (element.hasAttr("checked")) json.addProperty(map.get(id), "Có");
                else json.addProperty(map.get(id), "Không");
            }
        }

        addFile(json);
        //String jsonString = json.toString();
        //System.out.println(jsonString);
    }

    static void addFile(JsonObject json) {
        JsonArray jsonArray;

        try {
            // Read existing JSON file
            FileReader reader = new FileReader(filePath);
            jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            reader.close();
        } catch (Exception e) {
            jsonArray = new JsonArray();
        }

        jsonArray.add(json);

        writeJson(jsonArray);
    }

    static void writeJson(JsonArray jsonArray) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(jsonArray, writer);
            //System.out.println("New object added successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        writeJson(new JsonArray());
        //Initialize the ChromeDriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        WebDriver driver = new ChromeDriver(options);
        //WebDriver driver = new ChromeDriver();

        long startTime = System.currentTimeMillis();  // Get current time in milliseconds
        long cnt = 0;
        try {
            driver.get("https://hanoismile.bambu.vn");

            List<String> lines = Files.readAllLines(Paths.get("C:\\Users\\windows\\MySpace\\AWorkingLearningPlace\\Workspace MavenJava\\Account.txt"));

            driver.findElement(new By.ById("txtuName")).sendKeys(lines.get(0));
            driver.findElement(new By.ById("txtPass")).sendKeys(lines.get(1));

            driver.findElement(By.cssSelector("input[type='submit']")).click();

            // Select all patient
            driver.get("https://hanoismile.bambu.vn/default.aspx?kh=6");
            driver.findElement(new By.ById("ctl04_txtSearch")).sendKeys("%");
            driver.findElement(new By.ById("ctl04_cmdSearch")).click();

            // Print the title of the page
            System.out.println("Page title is: " + driver.getTitle());

            Document doc = Jsoup.parse(driver.getPageSource());

            /*
            Elements names = doc.select("a.b12 b");
            for (Element name: names) {
                System.out.println("Tên: " + name.text());
            }*/

            Elements links = doc.select("table#ctl04_grd_data a.b12");
            for (Element link: links) {
                System.out.println("Links " + link.attr("href"));

                driver.get("https://hanoismile.bambu.vn"+link.attr("href"));
                Document docu = Jsoup.parse(driver.getPageSource());

                cnt++;
                addJSon(docu);

                /*Elements nonEmptyInputs = docu.select("#ctl04_pnlEdit input[value]:not([value=''])");
                for (Element input : nonEmptyInputs) {
                    System.out.println("Non-empty input: " + input.attr("name") + " -> " + input.attr("value"));
                }
                String id = docu.getElementById("ctl04_txtCodeID").attr("value");
                System.out.println(id);*/
                //break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            driver.quit();

            long endTime = System.currentTimeMillis();  // Get current time after task
            long duration = endTime - startTime;  // Elapsed time in milliseconds

            System.out.println("Execution time: " + duration + " milliseconds");
            System.out.println("Number of collected patients: " + cnt);
            //fetch 11/21/25 ~ 6561 patients in 6469764 milliseconds
        }
    }
}
