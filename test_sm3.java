import org.bouncycastle.crypto.digests.SM3Digest;
import java.nio.charset.StandardCharsets;

public class TestSM3 {
    public static void main(String[] args) {
        String password = "123456";
        byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
        
        SM3Digest digest = new SM3Digest();
        digest.update(bytes, 0,  bytes.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        
        String hex = bytesToHex(hash);
        System.out.println("Password: " + password);
        System.out.println("SM3 Hash: " + hex);
    }
    
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xff);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}