package net.snemeis.quteproduction.controller;

import com.thedeanda.lorem.LoremIpsum;
import io.quarkus.qute.Qute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/engine")
    String engine(Model model) {
        // manual dev mode
        // ============================
//        Qute.engine().clearTemplates();
        // ----------------------------

        // dummy data
        List<String> filters = List.of("filter.availability", "filter.product_type", "filter.approbation");
        List<Product> products = IntStream.range(0, 10).mapToObj((num) -> generateProduct()).toList();

        model.addAttribute("filters", filters);
        model.addAttribute("hits", 999);
        model.addAttribute("products", products);

        // set model data and render template
        return "engin";
    }

    @GetMapping("/")
    @ResponseBody
    String index(Model model) {
        // manual dev mode
        // ============================
//        Qute.engine().clearTemplates();
        // ----------------------------

        // dummy data
        List<String> filters = List.of("filter.availability", "filter.product_type", "filter.approbation");
        List<Product> products = IntStream.range(0, 10).mapToObj((num) -> generateProduct()).toList();

        // set model data and render template
        return Qute.engine()
                .getTemplate("vindex")
                .data("filters", filters)
                .data("hits", 10009)
                .data("products", products)
                .render();
    }

    private Product generateProduct() {
        var lorem = LoremIpsum.getInstance();
        return new Product(lorem.getWords(2, 5), lorem.getZipCode(), lorem.getWords(15, 25));
    }

    record Product(String name, String productId, String description) {}
}
