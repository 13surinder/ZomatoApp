package com.upgrad.FoodOrderingApp.service.businness;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.upgrad.FoodOrderingApp.service.common.GenericErrorCode;
import com.upgrad.FoodOrderingApp.service.common.UnexpectedException;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.UUID;


public class JwtTokenProvider {
    private static final String TOKEN_ISSUER = "https://FoodOrderingApp.io";

    private final Algorithm algo;

    public JwtTokenProvider(final String secrets) {
        try {
            algo = Algorithm.HMAC512(secrets);
        } catch (IllegalArgumentException e) {
            throw new UnexpectedException(GenericErrorCode.GEN_001);
        }
    }

    public String generateToken(final String custUuid, final ZonedDateTime issuedDateAndTime, final ZonedDateTime expiresDateAndTime) {

        final Date issuedAt = new Date(issuedDateAndTime.getLong(ChronoField.INSTANT_SECONDS));
        final Date expiresAt = new Date(expiresDateAndTime.getLong(ChronoField.INSTANT_SECONDS));

        return JWT.create().withIssuer(TOKEN_ISSUER) //
                .withKeyId(UUID.randomUUID().toString())
                .withAudience(custUuid) //
                .withIssuedAt(issuedAt).withExpiresAt(expiresAt).sign(algo);
    }

}
