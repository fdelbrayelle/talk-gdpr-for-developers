package io.github.fdelbrayelle.gdpr.user.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class User {

  @JsonIgnore
  UUID id;

  String lastname;
  String firstname;
  String phoneNumber;
  Address address;
  Sex sex;

  public String getLastname() {
    return pseudonymize(lastname);
  }

  String pseudonymize(final String stringToPseudonymize) {
    try {
      byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
      var ivspec = new IvParameterSpec(iv);

      var factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      var spec = new PBEKeySpec("my_super_secret_key".toCharArray(), "my_super_salt".getBytes(), 65536, 256);
      var tmp = factory.generateSecret(spec);
      var secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

      // AES-256 Encryption
      var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
      return Base64.getEncoder().encodeToString(cipher.doFinal(stringToPseudonymize.getBytes(StandardCharsets.UTF_8)));
    } catch (final Exception e) {
      log.error("Error while encrypting: {0}", e);
    }
    return null;
  }
}
