package ladyjek.twiscode.com.ladyjek.Parse;

import android.app.Application;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.parse.PushService;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.TextFields;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Gotham.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        ParseManager.registerParse(this);
        ParseManager.getDeviceToken(this);
        NewEncrypt("k!asu123");

    }


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public PublicKey getPublicKey(){
        PublicKey publicKey = null;
        RSAPublicKey rsaKey = null;
        try {
            String filename = Environment.getExternalStorageDirectory() + "/" + "public_key.der";
            File file = new File(filename);
            FileInputStream fileInputStream = new FileInputStream(file);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            byte[] keyBytes = new byte[(int)file.length()];
            dataInputStream.readFully(keyBytes);
            dataInputStream.close();

            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

            Log.d("MyApplication", "filename" + filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return publicKey;
    }

    public String encrypt(String text){
        String result = "";

        try {
            byte[] cipherText=null;
            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.PUBLIC_KEY,(RSAPublicKey)getPublicKey());
            byte[] textB = cipher.doFinal(text.getBytes("UTF-8"));
            cipherText = Base64.encode(textB, Base64.CRLF);
            result = new String(cipherText);
            Log.d("MyApplication", "cipherText : " + result);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String NewEncrypt(String text){
        String result = "";
        try {
            /* Development */
            //BigInteger modulus = new BigInteger("D6C00AC47FB8E87F5995B27107BF4C54E1873D0A520E632DA17EC3D1BF8F45F0B1C4B0C5A114299DAE655A301630294D58C8FE8B299EE6A45AE1966D2289E9BD", 16);
            /* Production */
            BigInteger modulus = new BigInteger("00F0A500AC000F8E76BEB6ACBDA4EEE0333BA2D25FF11DC7E48268A50AB83D318E701478CD3735B21ABC51B96EBC90806843BAFC1DA608F9E8DA142EF966904D09C759CB90E9556346E573418EF595979B279235C4C9B43C0A044911379DBD7301B7C736E8EFB13BC308F833F91E09EA55B96EB2035C46329E2CC0CAB0F6A7B8CF",16);

            BigInteger exp = new BigInteger("65537");

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exp);
            RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] temp = cipher.doFinal(text.getBytes());

            result = Base64.encodeToString(temp, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            Log.e("EncryptUtils", e.getMessage());
        } catch (InvalidKeySpecException e) {
            Log.e("EncryptUtils", e.getMessage());
        } catch (NoSuchPaddingException e) {
            Log.e("EncryptUtils", e.getMessage());
        } catch (InvalidKeyException e) {
            Log.e("EncryptUtils", e.getMessage());
        } catch (BadPaddingException e) {
            Log.e("EncryptUtils", e.getMessage());
        } catch (IllegalBlockSizeException e) {
            Log.e("EncryptUtils", e.getMessage());
        }

        return result;
    }




}
