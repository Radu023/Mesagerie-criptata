package com.example.mesagerie_criptata;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Helper //in clasa de fata definim 2 metode, una pentru criptare si una pentru decriptare
{
    private static final String SECRET_KEY = "random_key"; //parola din care vom deriva cheia
    private static final String SALT = "ssshhhhhhhhhhh!!!!"; //adaugam un salt pentru a oferi securitate
    private static ProgressDialog progressDialog;

    //criptare

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt(String strToEncrypt) {
        try {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //vector de initializare
            IvParameterSpec ivspec = new IvParameterSpec(iv); //protejam vectorul definit anterior printr-o variabila IvParameterSpec pentru a nu fi alterat ulterior

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256"); //Password-Based Key Derivation Function // merge doar pentru criptarea simetrica
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 1000, 256); //Password based encryption dupa parola de mai sus
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec); // uinitializierea criptarii
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8))); //returnam textul criptat
        } catch (Exception e) {
            System.out.println("Eroare in timpul criptarii: " + e.toString());
        }
        return null;
    }

    //decriptare

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt(String strToDecrypt) {
        try {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //vector initializare
            IvParameterSpec ivspec = new IvParameterSpec(iv); //atribuirea valorii vectorului de initatilzare intr-o variabila pentru algoritmul de criptare
            // definirea variabilelor pentru criptare
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256"); //Password-Based Key Derivation Function
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 1000, 256); // 1000 de iteratii, conform standardului recomandat de  RSA Laboratories
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Eroare in timpul decriptarii: " + e.toString());
        }
        return null;
    }








    public static void showLoader(Context con, String msg)
    {
        progressDialog = new ProgressDialog(con);
        progressDialog.setMessage(msg);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    public static void stopLoader()
    {
        if (progressDialog != null)
        {

            progressDialog.cancel();
            progressDialog = null;

        }
    }



}

