package com.example.galerie_artisanale.security;

import com.example.galerie_artisanale.entity.Ordered;
import com.example.galerie_artisanale.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Component
public class MailConstructor {
    @Autowired
    private Environment env;

    @Autowired
    private TemplateEngine templateEngine;

    public  SimpleMailMessage constructResetTokenEmail(
            String contextPath, Locale locale, String token, User user, String password
            ){

        String url = contextPath + "/newUser?token="+token;
        String message = "\n Valider votre inscription avec ce lien :\n"+password;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Galerie Artisanale- Nouvelle inscription ");
        email.setText(url+message);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    /*public MailConstructor(Environment env) {
        this.env = env;
    }*/

    public MimeMessagePreparator constructOrderConfirmationEmail (User user, Ordered ordered, Locale locale) {
        Context context = new Context();
        context.setVariable("ordered", ordered);
        context.setVariable("user", user);
        context.setVariable("cartItemList", ordered.getCartItemList());
        String text = templateEngine.process("orderConfirmationEmailTemplate", context);

        MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
                email.setTo(user.getEmail());
                email.setSubject("Order Confirmation - "+ordered.getId());
                email.setText(text, true);
                email.setFrom(new InternetAddress("ray.deng83@gmail.com"));
            }
        };

        return messagePreparator;
    }
}
