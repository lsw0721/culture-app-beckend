package cultureinfo.culture_app.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberService memberService;

    @Value("${spring.mail.username}")
    private String senderEmail;

    private static final long CODE_EXPIRE_MINUTES = 5; // 인증 코드 유효 시간 (5분)

    // 인증 번호 생성
    public static int createNumber() {
        return (int)(Math.random() * 900000) + 100000; // 6자리 랜덤 숫자
    }

    //랜덤 비밀번호 생성
    public static String createPassword() {
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String allChars = lower + upper + digits;

        Random rand = new Random();
        List<Character> passwordChars = new ArrayList<>();

        // 반드시 하나씩 포함
        passwordChars.add(lower.charAt(rand.nextInt(lower.length())));
        passwordChars.add(upper.charAt(rand.nextInt(upper.length())));
        passwordChars.add(digits.charAt(rand.nextInt(digits.length())));

        // 나머지 4자리를 랜덤하게 채움
        for (int i = 0; i < 4; i++) {
            passwordChars.add(allChars.charAt(rand.nextInt(allChars.length())));
        }

        // 셔플해서 랜덤한 위치로 섞음
        Collections.shuffle(passwordChars);

        // 리스트를 문자열로 변환
        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }

        return password.toString();
    }

    // 회원가입용 이메일 작성
    public MimeMessage createJoinMail(String recipientEmail, int authCode) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, recipientEmail);
            message.setSubject("회원가입을 위한 이메일 인증");

            // 이메일 본문
            String body = "<h1 style='color:#2c2f33;'>안녕하세요.</h1>"
                    + "<h3 style='color:#99aab5;'>회원가입을 위해 요청하신 인증 번호입니다.</h3>"
                    + "<div align='center' style='border:1px solid #2c2f33; font-family:verdana;'>"
                    + "<h2 style='color:#2c2f33;'>회원가입 인증 코드입니다.</h2>"
                    + "<h1 style='color:#7289da'>" + authCode + "</h1>"
                    + "</div>";

            message.setText(body, "UTF-8", "html");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    // 비밀번호 찾기용 이메일 작성
    public MimeMessage createPasswordMail(String recipientEmail, String password) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, recipientEmail);
            message.setSubject("비밀번호 제공을 위한 이메일 발송");

            // 이메일 본문
            String body = "<h1 style='color:#2c2f33;'>안녕하세요.</h1>"
                    + "<h3 style='color:#99aab5;'>비밀번호 찾기를 위한 임시 비밀번호입니다.</h3>"
                    + "<div align='center' style='border:1px solid #2c2f33; font-family:verdana;'>"
                    + "<h2 style='color:#2c2f33;'>비밀번호입니다.</h2>"
                    + "<h1 style='color:#7289da'>" + password + "</h1>"
                    + "</div>";

            message.setText(body, "UTF-8", "html");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    // 회원가입 이메일 전송 및 Redis 저장
    public void sendJoinEmail(String recipientEmail) {
        int authCode = createNumber();
        MimeMessage message = createJoinMail(recipientEmail, authCode);

        // Redis에 인증 코드 저장, 5분 후 만료
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(recipientEmail, String.valueOf(authCode), CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);

        // 이메일 전송
        javaMailSender.send(message);
    }

    // 비밀번호 찾기용 이메일 전송
    //아이디 이메일 검증 -> 임시 비밀번호 생성/저장 -> 메일 발송
    public void sendTemporaryPassword(String username, String recipientEmail) {
        //이메일 존재 검증
        memberService.validateUsernameAndEmail(username, recipientEmail);
        //임시 비밀번호
        String tempPassword = createPassword();
        //db에 임시 비밀번호 저장
        memberService.saveTemporaryPassword(username, recipientEmail, tempPassword);
        // 이메일 메시지 작성 & 전송
        MimeMessage message = createPasswordMail(recipientEmail, tempPassword);
        // 이메일 전송
        javaMailSender.send(message);
    }

    // 인증 코드 확인
    public boolean verifyAuthCode(String email, String inputCode) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String storedCode = valueOperations.get(email);

        return storedCode != null && storedCode.equals(inputCode);
    }
}
