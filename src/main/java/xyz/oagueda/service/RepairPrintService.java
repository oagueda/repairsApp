package xyz.oagueda.service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import java.io.ByteArrayOutputStream;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import xyz.oagueda.domain.Repair;
import xyz.oagueda.service.dto.RepairDTO;

/**
 * Service Implementation for printing {@link xyz.oagueda.domain.Repair}.
 */
@Service
@Transactional
public class RepairPrintService {

    private static final String REPAIR = "repair";

    private static final String REPAIR_PATH = "repair/printRepair";

    private final Logger log = LoggerFactory.getLogger(RepairPrintService.class);

    private final SpringTemplateEngine templateEngine;

    private final UserService userService;

    public RepairPrintService(SpringTemplateEngine templateEngine, UserService userService) {
        this.templateEngine = templateEngine;
        this.userService = userService;
    }

    public ByteArrayOutputStream printRepair(Repair repair) {
        log.debug("Printing repair {}", repair);
        Locale locale = Locale.forLanguageTag(userService.getUserWithAuthorities().orElseThrow().getLangKey());
        Context context = new Context(locale);
        context.setVariable(REPAIR, repair);
        String content = templateEngine.process(REPAIR_PATH, context);
        ConverterProperties converterProperties = new ConverterProperties();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(content, baos, converterProperties);
        return baos;
    }
}
