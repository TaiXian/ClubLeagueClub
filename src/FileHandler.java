import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

class FileHandler implements HttpHandler {
    private String fileName;
    FileHandler(String fileName){
        this.fileName = fileName;
    }
    @Override
    public void handle(HttpExchange t) throws IOException {
        String s = "";
        try{
            File file = new File(fileName);
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                s += "\n" + sc.nextLine();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        helpServer.writeResponse(t, s);
    }
}
