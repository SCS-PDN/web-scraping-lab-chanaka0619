import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ScrapeServlet")
public class ScrapeServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer visitCount = (Integer) session.getAttribute("visitCount");
        if (visitCount == null) visitCount = 0;
        session.setAttribute("visitCount", visitCount + 1);

        
        String url = request.getParameter("url");
        String[] options = request.getParameterValues("options");

        List<ScrapedData> scrapedDataList = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url).get();

            if (options != null) {
                for (String option : options) {
                    switch (option) {
                        case "title":
                            scrapedDataList.add(new ScrapedData("Title", doc.title()));
                            break;
                        case "links":
                            Elements links = doc.select("a[href]");
                            for (Element link : links) {
                                scrapedDataList.add(new ScrapedData("Link", link.absUrl("href")));
                            }
                            break;
                        case "images":
                            Elements images = doc.select("img[src]");
                            for (Element img : images) {
                                scrapedDataList.add(new ScrapedData("Image", img.absUrl("src")));
                            }
                            break;
                    }
                }
            }

            
            Gson gson = new Gson();
            String json = gson.toJson(scrapedDataList);

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.write(json);

        } catch (IOException e) {
            e.printStackTrace();
            response.getWriter().println("Error connecting to the URL: " + e.getMessage());
        }
    }
}


class ScrapedData {
    private String type;
    private String content;

    public ScrapedData(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
