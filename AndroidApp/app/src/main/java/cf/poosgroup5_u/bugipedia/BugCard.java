package cf.poosgroup5_u.bugipedia;

//serves as the model class for each cardview
public class BugCard {

    private int id, image;
    private String name, sci_Name;


    public BugCard(int id, String name, String sci_Name, int image) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.sci_Name = sci_Name;
    }

    public int getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getsci_Name() {
        return sci_Name;
    }
}
