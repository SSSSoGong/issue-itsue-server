package com.ssssogong.issuemanager.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class JwtUtilTest {

    private static final String SECRET_KEY_TEXT = "hello-my-name-is-secret-key!!!nice-to-meet-U";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_TEXT.getBytes(StandardCharsets.UTF_8));
    private static final int EXPIRATION_MINUTES = 30;
    private static final String ACCOUNT_ID = "accountId";
    private static final String IS_ADMIN = "isAdmin";
    private static final JwtUtil JWT_UTIL = new JwtUtil(SECRET_KEY_TEXT, EXPIRATION_MINUTES);

    @Test
    void JWT_토큰_생성() {
        final String accountId = "happy";
        final String token = JWT_UTIL.createToken(accountId, false);

        assertThat(token).isNotBlank();
    }

    @Test
    void 시크릿_키로_JWT_토큰_검증() {
        final String accountId = "happy";
        final String token = JWT_UTIL.createToken(accountId, false);

        assertDoesNotThrow(
                () -> Jwts.parser()
                        .verifyWith(SECRET_KEY)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
        );
    }

    @Test
    void 시크릿_키로_JWT_토큰에서_계정_아이디_추출() {
        final String accountId = "happy";
        final String token = JWT_UTIL.createToken(accountId, false);

        final Claims payload = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        final String extractedAccountId = payload.get(ACCOUNT_ID, String.class);
        final Boolean extractedIsAdmin = payload.get(IS_ADMIN, Boolean.class);

        assertThat(extractedAccountId).isEqualTo(accountId);
        assertThat(extractedIsAdmin).isFalse();
    }

    @Test
    void JWT_토큰에서_유저_정보_추출() {
        final String accountId = "happy";
        final String token = JWT_UTIL.createToken(accountId, false);

        final AccessAccount accessAccount = JWT_UTIL.extractAccountData(token);
        assertThat(accessAccount.getAccountId()).isEqualTo(accountId);
        assertThat(accessAccount.isAdmin()).isFalse();
    }
}
