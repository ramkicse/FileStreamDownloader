package com.ramki.servlet;

import com.ramki.rest.Data;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ramki
 */
@WebServlet(urlPatterns = {"/makecloud"})
public class MakeCloud extends HttpServlet {

    @Inject
    Data data;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final String ALGO = "AES";
    private static final byte[] keyValue = "ramkrishnan18123".getBytes();

//    public static String encrypt(String Data) throws Exception {
//        Key key = new SecretKeySpec(keyValue, ALGO);
//        Cipher c = Cipher.getInstance(ALGO);
//        c.init(Cipher.ENCRYPT_MODE, key);
//        byte[] encVal = c.doFinal(Data.getBytes());
//        String encryptedValue = new BASE64Encoder().encode(encVal);
//        return encryptedValue;
//    }
//    public static String decrypt(String encryptedData) throws Exception {
//        Key key = generateKey();
//        Cipher c = Cipher.getInstance(ALGO);
//        c.init(Cipher.DECRYPT_MODE, key);
//        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
//        byte[] decValue = c.doFinal(decordedValue);
//        String decryptedValue = new String(decValue);
//        return decryptedValue;
//    }
    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.addHeader("Content-disposition", "attachment; filename=" + data.getName());
        //response.setContentType("text/html;charset=UTF-8");
        ServletOutputStream sos = response.getOutputStream();

        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);

        String path = System.getenv("OPENSHIFT_DATA_DIR");

        FileInputStream fileInputStream = new FileInputStream(path + "test.zip");
        byte[] buffer = new byte[4000];
        int bytedRead;
        long count = 0;
        long dataCompleted = data.getTotalCompleted();
        long size = data.getSize();
        if (dataCompleted > 0) {
            fileInputStream.skip(dataCompleted);
            count=dataCompleted;
        }
        System.out.println(fileInputStream.available());
        while (fileInputStream.available() > 0) {
            bytedRead = fileInputStream.read(buffer);

            if (dataCompleted + size > count) {
                byte[] encVal = c.update(buffer, 0, bytedRead);
                sos.write(encVal);
            } else {
                break;
            }

            count += bytedRead;
            data.setTotalCompleted(count);
        }

        sos.write(c.doFinal());
        fileInputStream.close();
        sos.close();

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(MakeCloud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(MakeCloud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
