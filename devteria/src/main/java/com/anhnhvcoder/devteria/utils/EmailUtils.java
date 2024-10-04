package com.anhnhvcoder.devteria.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EmailUtils {

    public String subjectRegister(){
        return "Welcome to Devteria Learning Project";
    }

    public String bodyRegister(String firstName, String lastName){
        return "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "<head>\n"
                + "  <meta charset=\"utf-8\">\n"
                + "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n"
                + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "  <style>\n"
                + "    body {\n"
                + "      font-family: Arial, sans-serif;\n"
                + "      background-color: #f4f4f4;\n"
                + "      margin: 0;\n"
                + "      padding: 0;\n"
                + "    }\n"
                + "\n"
                + "    .email-container {\n"
                + "      max-width: 600px;\n"
                + "      margin: 20px auto;\n"
                + "      background-color: #fff;\n"
                + "      border-radius: 8px;\n"
                + "      overflow: hidden;\n"
                + "      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n"
                + "    }\n"
                + "\n"
                + "    .header {\n"
                + "      background-color: #34db74;\n"
                + "      color: #fff;\n"
                + "      padding: 20px;\n"
                + "      text-align: center;\n"
                + "    }\n"
                + "\n"
                + "    .content {\n"
                + "      padding: 20px;\n"
                + "    }\n"
                + "\n"
                + "    .discount-code {\n"
                + "      background-color: #e74c3c;\n"
                + "      color: #fff;\n"
                + "      padding: 10px;\n"
                + "      text-align: center;\n"
                + "      margin-bottom: 20px;\n"
                + "      font-size: 20px;\n"
                + "      border-radius: 4px;\n"
                + "    }\n"
                + "\n"
                + "    .footer {\n"
                + "      background-color: #34db74;\n"
                + "      color: #fff;\n"
                + "      padding: 10px;\n"
                + "      text-align: center;\n"
                + "    }\n"
                + "  </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <div class=\"email-container\">\n"
                + "    <div class=\"header\">\n"
                + "     <h1>Devteria Learning Project</h1>\n "
                + "      <h2>Xin chào bạn " + firstName + " "  + lastName + "</h2>\n"
                + "    </div>\n"
                + "    <div class=\"content\">\n"
                + "      <h3>Thông tin:</h3>\n"
                + "      <p>Ngày : " + LocalDate.now() + "</p>\n"
                + "      <p>Contact 0976652503.</p>\n"
                + "      <p>We are ready support</p>\n"
                + "    </div>\n"
                + "    <div class=\"footer\">\n"
                + "      <p>Thanks for Subcribe</p>\n"
                + "    </div>\n"
                + "  </div>\n"
                + "</body>\n"
                + "</html>";
    }

    public String subjectVerifyEmail(){
        return "Verify your email";
    }

    public String bodyVerifyEmail(){
        return null;
    }
}
