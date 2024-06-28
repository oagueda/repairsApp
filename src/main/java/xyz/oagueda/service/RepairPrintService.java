package xyz.oagueda.service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import java.io.ByteArrayOutputStream;
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

    private static final String REPAIR_PATH = "repair/printRepair";

    private static final int MAX_LENGTH = 500;

    private static final int SMALL_LENGTH = 200;

    private final SpringTemplateEngine templateEngine;

    private final UserService userService;

    public RepairPrintService(SpringTemplateEngine templateEngine, UserService userService) {
        this.templateEngine = templateEngine;
        this.userService = userService;
    }

    public ByteArrayOutputStream printRepair(Repair repair) {
        Locale locale = Locale.forLanguageTag(userService.getUserWithAuthorities().orElseThrow().getLangKey());
        Context context = new Context(locale);
        if (repair.getDescription() != null) repair.setDescription(repair.getDescription().substring(0, MAX_LENGTH));
        if (repair.getObservations() != null) repair.setObservations(repair.getObservations().substring(0, SMALL_LENGTH));
        if (repair.getCustomerMaterial() != null) repair.setCustomerMaterial(repair.getCustomerMaterial().substring(0, SMALL_LENGTH));
        if (repair.getWorkDone() != null) repair.setWorkDone(repair.getWorkDone().substring(0, MAX_LENGTH));
        if (repair.getUsedMaterial() != null) repair.setUsedMaterial(repair.getUsedMaterial().substring(0, MAX_LENGTH));
        if (repair.getDevice() != null && repair.getDevice().getNotes() != null) repair
            .getDevice()
            .setNotes(repair.getDevice().getNotes().substring(0, MAX_LENGTH));
        context.setVariable(REPAIR, repair);
        String content = templateEngine.process(REPAIR_PATH, context);
        ConverterProperties converterProperties = new ConverterProperties();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(content, baos, converterProperties);
        return baos;
    }
}
