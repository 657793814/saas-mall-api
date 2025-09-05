package com.liuzd.soft.service.impl;

import com.liuzd.soft.vo.CaptchaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

@Slf4j
@Service
public class CaptchaService {

    private static final String CHAR_SET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int CODE_LENGTH = 4;
    private static final int WIDTH = 120;
    private static final int HEIGHT = 40;
    private static final int FONT_SIZE = 20;

    /**
     * 生成验证码图片
     *
     * @return 验证码图片的Base64编码和验证码文本
     */
    public CaptchaResult generateCaptcha() {
        // 生成随机验证码
        String captchaText = generateRandomText();

        // 创建图片
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // 设置背景色
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // 设置字体
        g2d.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));

        // 绘制验证码文本
        drawCaptchaText(g2d, captchaText);

        // 添加干扰线
        drawInterferenceLines(g2d);

        // 添加噪点
        drawNoise(g2d);

        g2d.dispose();

        // 转换为Base64
        String base64Image = imageToBase64(image);

        return new CaptchaResult(base64Image, captchaText);
    }

    /**
     * 生成随机验证码文本
     */
    private String generateRandomText() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHAR_SET.length());
            sb.append(CHAR_SET.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 绘制验证码文本
     */
    private void drawCaptchaText(Graphics2D g2d, String captchaText) {
        Random random = new Random();
        int x = 10;
        for (int i = 0; i < captchaText.length(); i++) {
            // 设置随机颜色
            g2d.setColor(new Color(random.nextInt(150), random.nextInt(150), random.nextInt(150)));

            // 设置随机旋转角度
            double theta = random.nextInt(40) - 20; // -20到20度
            g2d.rotate(Math.toRadians(theta), x, HEIGHT / 2);

            // 绘制字符
            g2d.drawString(String.valueOf(captchaText.charAt(i)), x, HEIGHT / 2 + FONT_SIZE / 2 - 5);

            // 恢复旋转角度
            g2d.rotate(-Math.toRadians(theta), x, HEIGHT / 2);

            x += (WIDTH - 20) / CODE_LENGTH;
        }
    }

    /**
     * 添加干扰线
     */
    private void drawInterferenceLines(Graphics2D g2d) {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * 添加噪点
     */
    private void drawNoise(Graphics2D g2d) {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g2d.drawOval(x, y, 1, 1);
        }
    }

    /**
     * 将BufferedImage转换为Base64编码
     */
    private String imageToBase64(BufferedImage image) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            log.error("Failed to convert image to base64", e);
            return null;
        }
    }

    /**
     * 验证验证码
     *
     * @param userInput   用户输入的验证码
     * @param captchaCode 实际的验证码
     * @return 是否匹配
     */
    public boolean validateCaptcha(String userInput, String captchaCode) {
        if (userInput == null || captchaCode == null) {
            return false;
        }
        return userInput.equalsIgnoreCase(captchaCode);
    }
}
