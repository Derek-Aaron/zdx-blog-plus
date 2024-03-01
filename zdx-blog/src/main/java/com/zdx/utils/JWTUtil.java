package com.zdx.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.IdUtil;
import com.zdx.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
public class JWTUtil {

    public static String parseToken(String token) {

        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(getPublicKey())
                .build().parseSignedClaims(token);
        Claims claims = claimsJws.getPayload();
        if (claims.getExpiration().before(Calendar.getInstance().getTime())) {
            return null;
        }
        return claims.get(Constants.JWT_USER_INFO_KEY, String.class);
    }



    private static PublicKey getPublicKey(){
        try {
            InputStream is = ResourceUtil.getStream("jwtKey/public.key");
            List<String> strs = new ArrayList<>();
            IoUtil.readLines(is, StandardCharsets.UTF_8, strs);
            strs.remove(0);
            strs.remove(strs.size() -1);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(String.join("", strs)));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("生成密匙异常：{}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    public static String generateToken(String token, int expire){
        if (expire <= 0) {
            expire = Constants.DEFAULT_EXPIRE_DAY;
        }

        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.DAY_OF_MONTH, expire);
        return Jwts.builder()
                .claim(Constants.JWT_USER_INFO_KEY, token)
                .issuedAt(new Date())
                .id(IdUtil.fastUUID())
                .expiration(nowTime.getTime())
                .signWith(getPrivateKey(), Jwts.SIG.RS256)
                .compact();
    }

    public static String generateToken(String token) {
        return generateToken(token, 0);
    }

    private static PrivateKey getPrivateKey(){
        try {
            InputStream is = ResourceUtil.getStream("jwtKey/private.key");
            List<String> strs = new ArrayList<>();
            IoUtil.readLines(is, StandardCharsets.UTF_8, strs);
            strs.remove(0);
            strs.remove(strs.size() -1);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(String.join("", strs)));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("生成密匙错误：{}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
