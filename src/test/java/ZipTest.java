import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.codeborne.pdftest.assertj.Assertions.assertThat;

public class ZipTest {

    @Test
    void testZipArchive() throws Exception {
        ZipFile zipFile = new ZipFile("src/test/resources/resources.zip");
        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();

            String fileFormat = entry.getName().substring(entry.getName().indexOf(".") + 1);
            switch (fileFormat) {
                case ("pdf"):
                    testPdf(zipFile.getInputStream(entry));
                    break;
                case ("csv"):
                    testCsv(zipFile.getInputStream(entry));
                    break;
                case ("xlsx"):
                    testXsls(zipFile.getInputStream(entry));
                    break;
            }
        }
    }

    @Step("Проверка PDF файла")
    void testPdf(InputStream is) throws Exception {
        PDF pdf = new PDF(is);
        assertThat(pdf.author).isEqualTo("Yukon Department of Education");
        assertThat(pdf).containsExactText("PDF Test File");
    }

    @Step("Проверка Csv файла")
    void testCsv(InputStream is) throws Exception {
        try (CSVReader reader = new CSVReader(new InputStreamReader(is))) {
            List<String[]> lines = reader.readAll();
            assertThat(lines.get(0)).contains("John", "Doe", "120 jefferson st.", "Riverside", " NJ", " 08075");
        }
    }

    @Step("Проверка Xsls файла")
    void testXsls(InputStream is) throws Exception {
        XLS xls = new XLS(is);
        assertThat(xls.excel
                .getSheetAt(0)
                .getRow(0)
                .getCell(0)
                .getStringCellValue())
                .contains("ID");
    }
}
