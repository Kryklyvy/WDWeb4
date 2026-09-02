package com.example.demo;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final Resend resend;
    private final String resendFrom;

    public UserController(
            @Value("${resend.api.key}") String apiKey,
            @Value("${resend.from}") String resendFrom) {

        this.resend = new Resend(apiKey);
        this.resendFrom = resendFrom;
    }

    // ==============================
    // DISPLAY INDEX PAGE
    // ==============================

    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("userData", new UserData());

        return "index";
    }

    // ==============================
    // WEDDING RSVP
    // ==============================

    @PostMapping("/submit-form")
    public String submitForm(UserData userData) {

        System.out.println("================================");
        System.out.println("       NEW WEDDING RSVP");
        System.out.println("================================");

        System.out.println("Attending: "
                + (userData.isAttending() ? "YES" : "NO"));

        System.out.println("Attendees:");

        if (userData.getAttendees() != null) {

            for (String attendee : userData.getAttendees()) {

                System.out.println("- " + attendee);
            }
        }

        // Create RSVP email content

        StringBuilder email = new StringBuilder();

        email.append("NEW WEDDING RSVP\n\n");

        email.append("Attending: ");

        email.append(
                userData.isAttending()
                        ? "YES"
                        : "NO"
        );

        email.append("\n\n");

        email.append("Attendees:\n");

        if (userData.getAttendees() != null) {

            for (int i = 0;
                 i < userData.getAttendees().size();
                 i++) {

                email.append(i + 1)
                        .append(". ")
                        .append(userData.getAttendees().get(i))
                        .append("\n");
            }
        }

        // Send RSVP email using Resend

        try {

            CreateEmailOptions request =
                    CreateEmailOptions.builder()
                            .from(resendFrom)
                            .to("xk.rosal@gmail.com")
                            .subject("New Wedding RSVP")
                            .text(email.toString())
                            .build();

            resend.emails().send(request);

            System.out.println("RSVP email sent successfully!");

        } catch (Exception e) {

            System.out.println("Failed to send RSVP email.");
            e.printStackTrace();
        }

        return "redirect:/?rsvpSent=true";
    }

    // ==============================
    // LOVE MESSAGE
    // ==============================

    @PostMapping("/send-love-message")
    public String sendLoveMessage(UserData userData) {

        System.out.println("================================");
        System.out.println("       NEW LOVE MESSAGE");
        System.out.println("================================");

        System.out.println("From: "
                + userData.getMessageName());

        System.out.println("Message: "
                + userData.getLoveMessage());

        // Create love message email

        String emailText =
                "NEW LOVE MESSAGE\n\n" +

                "From: "
                + userData.getMessageName()
                + "\n\n" +

                "Message:\n"
                + userData.getLoveMessage();

        // Send email using Resend

        try {

            CreateEmailOptions request =
                    CreateEmailOptions.builder()
                            .from(resendFrom)
                            .to("xk.rosal@gmail.com")
                            .subject("New Wedding Love Message")
                            .text(emailText)
                            .build();

            resend.emails().send(request);

            System.out.println("Love message email sent successfully!");

        } catch (Exception e) {

            System.out.println("Failed to send love message email.");
            e.printStackTrace();
        }

        return "redirect:/?messageSent=true";
    }
}
```
