package web.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@Configuration
@EnableWebMvc //аннотация для веб приложении
@ComponentScan("web") //spring регистрирует классы с аннотациями в пределах пакета web
public class WebConfig implements WebMvcConfigurer { //класс для настройки spring

    private final ApplicationContext applicationContext;

    public WebConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Bean
    public SpringResourceTemplateResolver templateResolver() { //класс для нахождения HTML-шаблонов приложения
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/pages/");
        templateResolver.setSuffix(".html"); //какое расширение искать
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() { //обработка шаблона html после SpringResourceTemplateResolver
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true); //включает компилятор Spring Expression Language.
        return templateEngine;
    }


    @Override //из класса WebMvcConfigurer
    public void configureViewResolvers(ViewResolverRegistry registry) { //метод как Spring должен находить представление (view), которое вернул контроллер.
        ThymeleafViewResolver resolver = new ThymeleafViewResolver(); //объект, через который можно зарегистрировать настройки поиска представлений(view)
        resolver.setTemplateEngine(templateEngine());//ViewResolver должен использовать TemplateEngine
        registry.viewResolver(resolver);
    }
}