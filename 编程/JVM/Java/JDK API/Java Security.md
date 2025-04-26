# Curve25519/Curve448
#java11 

  ```java
  public void curve25519() throws Exception {  
      KeyPairGenerator kpg = KeyPairGenerator.getInstance("XDH");
      // Curve448: X448
      kpg.initialize(paramSpec);
      KeyPair kp = kpg.generateKeyPair();
      // 公钥  
      KeyFactory kf = KeyFactory.getInstance("XDH");
      BigInteger u = ...; // u
      XECPublicKeySpec pubSpec = new XECPublicKeySpec(paramSpec, u);
      PublicKey pubKey = kf.generatePublic(pubSpec);
    
      // 私钥  
      KeyAgreement ka = KeyAgreement.getInstance("XDH");
      ka.init(kp.getPrivate());
      ka.doPhase(pubKey, true);
      byte[] secret = ka.generateSecret();
  }
  ```
# Ed25519
#java15

  ```java
  public void EdDSA(String msg) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {  
      KeyPairGenerator kpg = KeyPairGenerator.getInstance("Ed25519");  
      KeyPair kp = kpg.generateKeyPair();  
      Signature sig = Signature.getInstance("Ed25519");  
      sig.initSign(kp.getPrivate());  
      sig.update(msg.getBytes(StandardCharsets.UTF_8));  
      byte[] s = sig.sign();  
  }
  ```
