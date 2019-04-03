package com.beautifulsoup.driving.utils;

import com.beautifulsoup.driving.dto.UserTokenDto;
import com.beautifulsoup.driving.pojo.Agent;
import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;


import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtil {

    private static final String rawKey="8c07b32e-f968-43a7-8160-5279d0ebc328";

    public static String conferToken(UserTokenDto agentToken, Long expire){
        SignatureAlgorithm signatureAlgorithm=SignatureAlgorithm.HS512;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Map<String,Object> claims = new HashMap<>();
        claims.put("name", "BeautifulSoup");
        claims.put("admin", true);


        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setIssuer("BeautifulSoup")
                .setId("jwt")
                .setIssuedAt(now)
                .setSubject(JsonSerializerUtil.obj2String(agentToken))
                .signWith(signatureAlgorithm,generateSecretKey());
        if (expire >= 0) {
            long expMillis = nowMillis + expire;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    //解析JWT
    public static Claims parseJWT(String jwt) throws ExpiredJwtException, MalformedJwtException,SignatureException{
        Claims claims = Jwts.parser()
                .setSigningKey(generateSecretKey())
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

    //生成秘钥
    private static SecretKey generateSecretKey(){
        byte[] encodedKey = Base64.decodeBase64(rawKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }
}
