import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.util.Elements;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebScraper {

	   public static void main(String[] args) {
	        String url = "https://www.bbc.com"; 
	        try {
	            Document doc = Jsoup.connect(url).get();
	            
	            
	            String title = doc.title();
	            System.out.println("Title: " + title);

	            
	            System.out.println("\nHeadings:");
	            for (int i = 1; i <= 6; i++) {
	                Elements headings = doc.select("h" + i);
	                for (Element heading : headings) {
	                    System.out.println("h" + i + ": " + heading.text());
	                }
	            }

	            
	            System.out.println("\nLinks:");
	            Elements links = doc.select("a[href]");
	            for (Element link : links) {
	                System.out.println(link.attr("abs:href") + " - " + link.text());
	            }

	           
	            List<NewsArticle> articles = new ArrayList<>();

	            Elements newsHeadlines = doc.select("h3, h2"); 
	            for (Element headline : newsHeadlines) {
	                String newsTitle = headline.text();
	                String publicationDate = "Not Available"; 
	                String authorName = "Not Available";

	                
	                
	                NewsArticle article = new NewsArticle(newsTitle, publicationDate, authorName);
	                articles.add(article);
	            }

	            
	            System.out.println("\nExtracted News Articles:");
	            for (NewsArticle article : articles) {
	                System.out.println(article);
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}

	
	class NewsArticle {
	    private String headline;
	    private String publicationDate;
	    private String authorName;

	    public NewsArticle(String headline, String publicationDate, String authorName) {
	        this.headline = headline;
	        this.publicationDate = publicationDate;
	        this.authorName = authorName;
	    }

	    @Override
	    public String toString() {
	        return "Headline: " + headline + "\nPublication Date: " + publicationDate + "\nAuthor: " + authorName + "\n";
	    }
	}
