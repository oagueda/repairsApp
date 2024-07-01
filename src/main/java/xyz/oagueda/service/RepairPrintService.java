package xyz.oagueda.service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import xyz.oagueda.domain.Repair;

/**
 * Service Implementation for printing {@link xyz.oagueda.domain.Repair}.
 */
@Service
@Transactional
public class RepairPrintService {

    private static final String REPAIR = "repair";

    private static final String CODE = "code";

    private static final String DATE = "date";

    private static final String REPAIR_PATH = "repair/printRepair";

    private static final int MAX_LENGTH = 400;

    private static final int MAX_LENGTH_NOTES = 500;

    private static final int MAX_LENGTH_DESC = 350;

    private static final int SMALL_MAX_LENGTH = 170;

    private final SpringTemplateEngine templateEngine;

    private final UserService userService;

    public RepairPrintService(SpringTemplateEngine templateEngine, UserService userService) {
        this.templateEngine = templateEngine;
        this.userService = userService;
    }

    public ByteArrayOutputStream printRepair(Repair repair) {
        Locale locale = Locale.forLanguageTag(userService.getUserWithAuthorities().orElseThrow().getLangKey());
        Context context = new Context(locale);
        if (repair.getDescription() != null && repair.getDescription().length() > MAX_LENGTH_DESC) repair.setDescription(
            repair.getDescription().substring(0, MAX_LENGTH_DESC) + "..."
        );
        if (repair.getObservations() != null && repair.getObservations().length() > MAX_LENGTH) repair.setObservations(
            repair.getObservations().substring(0, SMALL_MAX_LENGTH) + "..."
        );
        if (repair.getCustomerMaterial() != null && repair.getCustomerMaterial().length() > MAX_LENGTH) repair.setCustomerMaterial(
            repair.getCustomerMaterial().substring(0, SMALL_MAX_LENGTH) + "..."
        );
        if (repair.getWorkDone() != null && repair.getWorkDone().length() > MAX_LENGTH) repair.setWorkDone(
            repair.getWorkDone().substring(0, MAX_LENGTH) + "..."
        );
        if (repair.getUsedMaterial() != null && repair.getUsedMaterial().length() > MAX_LENGTH) repair.setUsedMaterial(
            repair.getUsedMaterial().substring(0, MAX_LENGTH) + "..."
        );
        if (
            repair.getDevice() != null && repair.getDevice().getNotes() != null && repair.getDevice().getNotes().length() > MAX_LENGTH_NOTES
        ) repair.getDevice().setNotes(repair.getDevice().getNotes().substring(0, MAX_LENGTH_NOTES) + "...");
        context.setVariable(REPAIR, repair);
        if (
            repair.getDevice() != null && repair.getDevice().getPattern() != null && repair.getDevice().getPattern().getCode() != null
        ) context.setVariable(CODE, repair.getDevice().getPattern().getCode().split(""));
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String today = myDateObj.format(myFormatObj);
        context.setVariable(DATE, today);
        String content = templateEngine.process(REPAIR_PATH, context);
        ConverterProperties converterProperties = new ConverterProperties();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(content, baos, converterProperties);
        return baos;
    }
}
