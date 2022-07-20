import javafx.application.Platform;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Session implements Serializable {
    public ArrayList<Item> items;
    public ArrayList<Service> services;
    public ArrayList<Tag> tags;

    private transient AddWindow addWindow;
    private static final String path = "./recent.ser";


    public static @Nullable Session newSession(App app){
        File f = new File(path);
        if(!(f.exists() && !f.isDirectory())){
            System.err.println("WARNING: No session found! Making new one");
            return new Session(app);
        }
        Session s;
        try(FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn)){
            s = (Session) in.readObject();
            s.resetApp(app);
            return s;
        }catch (IOException e){
            System.out.println("WARNING: Unable to find recent");
        } catch (ClassNotFoundException e) {
            System.out.println("WARNING: Class not found");
        }
        return null;
    }

    private void getContent(){
        tags.add(Tag.IDENTIFY_BY_EMAIL);
    }
    private Session(App app){
        this.addWindow = new AddWindow(app);
        items = new ArrayList<>();
        services = new ArrayList<>();
        tags = new ArrayList<>();
        getContent();
    }

    public void resetApp(App app){
        this.addWindow = new AddWindow(app);
    }
    public void edit(Searchable thing){
        addWindow.edit(thing);
    }

    public void saveExit(){
        File recent = new File(Session.path);
        if(recent.exists() && !recent.isDirectory()){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd--HH-mm-ss");
            String now = LocalDateTime.now().format(dtf);
            try{
                Files.move(recent.toPath(), recent.toPath().resolveSibling( now+".ser"));
                writeOut();
            }catch (IOException e){
                System.err.println("WARNING: Unable to move previous session");
            }
        }else{
            writeOut();
        }
    }

    private void writeOut(){

        try(FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(this);
            Platform.exit();
        } catch (IOException e) {
            System.err.println("WARNING: Unable to save session");
        }
    }

}
