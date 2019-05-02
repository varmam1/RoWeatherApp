import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class FlagGetter {

    /**
     * @return : String:
     *      The color of the flag at the moment the function was called
     */

    public static String getFlagColor() throws IOException{

        //Gets the information from the site which has the flag
        URL url = new URL("https://www.cucbc.org/flag");

        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        String line;
        String flagColor = "";
        while ((line = in.readLine()) != null) {

            // Goes through the HTML doc until it finds the phrase "The flag is currently " in which
            // it will find the color by getting the word between <strong> and </strong>

            if (line.contains("The flag is currently ")) {
                flagColor = line.substring(line.indexOf("<strong>") + 8, line.indexOf("</strong>"));
                
                // If the flag is Red/Yellow, most boats won't go out so we'll assume it's red for sake of simplicity 
                if (flagColor.equals("Red/Yellow"))
                    flagColor = "Red";
            }

        }
        in.close();
        return flagColor;
    }
}
