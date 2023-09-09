package com.convrse.utils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.convrse.token.BearerToken;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class JwtVerifier {


    private String keyStorePath = "keystore.jks";

    private String keyStorePassword = "password";

    private String keyAlias = "jwtsigning";

    private String privateKeyPassphrase = "password";

    private KeyStore keyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(keyStorePath);
            keyStore.load(resourceAsStream, keyStorePassword.toCharArray());
            return keyStore;
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {

            System.out.println("Unable to load keystore: {}" +  keyStorePath + e);
        }

        throw new IllegalArgumentException("Unable to load keystore");
    }


    private RSAPrivateKey jwtSigningKey(KeyStore keyStore) {
        try {
            Key key = keyStore.getKey(keyAlias, privateKeyPassphrase.toCharArray());
            if (key instanceof RSAPrivateKey) {
                return (RSAPrivateKey) key;
            }
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            System.out.println("Unable to load private key from keystore: {}" + keyStorePath + e);
        }

        throw new IllegalArgumentException("Unable to load private key");
    }

    private RSAPublicKey jwtValidationKey(KeyStore keyStore) {
        try {
            Certificate certificate = keyStore.getCertificate(keyAlias);
            PublicKey publicKey = certificate.getPublicKey();

            if (publicKey instanceof RSAPublicKey) {
                return (RSAPublicKey) publicKey;
            }
        } catch (KeyStoreException e) {
            System.out.println("Unable to load private key from keystore: {}" + keyStorePath + e);
        }

        throw new IllegalArgumentException("Unable to load RSA public key");
    }

    public DecodedJWT verifyJwtToken(String jwtToken, String issuer) throws  JWTVerificationException{
        Algorithm algorithm = Algorithm.RSA256(this.jwtValidationKey(keyStore()), this.jwtSigningKey(keyStore()));
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
            return verifier.verify(jwtToken);

    }

    public DecodedJWT verifyJwtToken(String jwtToken) throws JWTVerificationException {
        return verifyJwtToken(jwtToken, "http://auth-server:9002");
    }

    public DecodedJWT verifyBearerToken(BearerToken token) throws  JWTVerificationException {
        return verifyJwtToken(token.getValue());
    }

    public DecodedJWT verifyBearerToken(BearerToken token, String issuer) throws  JWTVerificationException {
        return verifyJwtToken(token.getValue(), issuer);
    }
}