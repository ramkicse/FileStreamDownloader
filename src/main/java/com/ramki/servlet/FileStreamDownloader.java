package com.ramki.servlet;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ramki
 */
@WebServlet(urlPatterns = {"/makedata"})
public class FileStreamDownloader extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        int no = 0;
        try {
            no = Integer.parseInt(request.getParameter("no"));

        } catch (NumberFormatException exception) {
            no = 0;
        }
       
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet NewServlet</title>");
            out.println("</head>");
            out.println("<body>");

            final WebClient webClient = new WebClient();
            webClient.getCookieManager().clearCookies();
            webClient.getOptions().setUseInsecureSSL(true);
            //webClient.setJavaScriptEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            final HtmlPage page = webClient.getPage("https://filestream.me/");
            //out.println(page.getTitleText());
            System.out.println("no of forms : " + page.getForms().size());
            HtmlForm form = page.getForms().get(1);
            final HtmlTextInput textField = form.getInputByName("login");
            textField.setAttribute("value", "ramkicse@gmail.com");

            final HtmlPasswordInput passField = form.getInputByName("password");
            passField.setAttribute("value", "ramkrishnan18");
            HtmlSubmitInput htmlSubmitInput = form.getInputByValue("login");
            HtmlPage page1 = htmlSubmitInput.click();

            HtmlTable htmlTable = page1.getHtmlElementById("fileCatTable");
            List<HtmlTableRow> listOfHtmlTableRow = htmlTable.getBodies().get(0).getRows();
            List<String> allLinks = new ArrayList<String>();
            for (HtmlTableRow htmlTableRow : listOfHtmlTableRow) {
                HtmlTableCell cell = htmlTableRow.getCells().get(htmlTableRow.getCells().size() - 1);
                for (DomElement domElement : cell.getChildElements()) {
                    //out.println(domElement.getTagName());
                    if (domElement.getTagName().equals("div")) {
                        boolean flag = true;

                        for (DomElement celldomElement : domElement.getChildElements()) {
                            if (flag) {
                               // out.println(celldomElement.getTagName());

                                    String title=celldomElement.getAttribute("title");
                                if("Download".equals(title)|| "Downloads".equals(title)){




                                String link = celldomElement.getAttribute("onclick");
                                String http = link.substring(link.indexOf('\'') + 1, link.indexOf('\'', link.indexOf('\'') + 1));

                                out.println(celldomElement.getAttribute("onclick") + "<br/>");
                                out.println("URL : "+http + "<br/><br/>");
                                allLinks.add(http);
                                flag = false;

                                }

                            }


                        }

                    }
                }
            }


            //  InputStream is =anchorAttachment.click().getWebResponse().getContentAsStream();

            out.println("************ Start *************");

            //  HtmlAnchor anchorElement=HTMLAnchorElement.;
            //  anchorElement.set
            //anchorElement.setHref(allLinks.get(0));

            //  anchorElement.cl
            final String u = allLinks.get(no);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(u);
                        System.out.println("Link : " + u);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();

                        // System.out.println("Response Code : " + con.getInputStream().available());
                        con.setRequestProperty("Accept-Encoding", "gzip,deflate");
                        con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.71 Safari/537.36");

                        Map<String, List<String>> map = con.getHeaderFields();

                        for (String object : map.keySet()) {
                            System.out.println(object + " --> " + map.get(object));

                        }


                        System.out.println("Response Code : " + con.getResponseCode());
                        System.out.println("length :" + con.getContentLength());
                        System.out.println("Message " + con.getResponseMessage());

                        InputStream inputStream = con.getInputStream();
                        String path=System.getenv("OPENSHIFT_DATA_DIR");
                        FileOutputStream fileOutputStream = new FileOutputStream( path + "test.zip");
                        byte[] buffer = new byte[1024];
                        System.out.println(inputStream.available());
                        int bytesRead = 10;
                        while (true) {
                            //   System.out.print("--");
                            bytesRead = inputStream.read(buffer);
                            //   System.out.println(bytesRead);
                            if (bytesRead == -1) {
                                break;
                            }
                            fileOutputStream.write(buffer, 0, bytesRead);
                            // System.out.print(">");
                        }
                        fileOutputStream.close();

                        System.out.println("File Completed");
                    } catch (IOException ex) {
                        Logger.getLogger(FileStreamDownloader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            t.start();

//            System.out.println(allLinks.get(0));
//            System.out.println(webClient.getCookieManager().getCookies(new URL(allLinks.get(0))).size());
//            // System.out.println(httpPage.asText());
//             HtmlPage httpPage = webClient.getPage(allLinks.get(0));
//             
//           // final String pageAsXml = page1.asXml();
//            // InputStream inputStream=httpPage.getWebResponse().getContentAsStream();
//           //  inputStream.available();
//              System.out.println("Code : "+httpPage.getWebResponse().getStatusMessage());
//               System.out.println("Code : "+httpPage.getWebResponse().getContentType());
//                System.out.println("netCode : "+httpPage.getWebResponse().getContentCharset());
//            
//out.println("<br/><br/>Code : "+httpPage.getWebResponse().getStatusCode());
//out.println("<br/><br/>Code : "+httpPage.getWebResponse().getContentType());
//out.println("<br/><br/>Code : "+httpPage.getWebResponse().getContentCharset());

            //final String pageAsText = page1.asText();

            //out.println(pageAsXml);
           // webClient.closeAllWindows();
            webClient.close();
            out.println("<h1>Servlet NewServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }catch(Exception e){ 
            out.println(e.getMessage());
        }
        finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
