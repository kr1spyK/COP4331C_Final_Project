package cf.poosgroup5_u.bugipedia;

//serves as the model class for each cardview
public class BugCard {

    private Integer id, image;
    private String name, sci_Name;//, image;


    public BugCard(Integer id, String name, String sci_Name, Integer image) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.sci_Name = sci_Name;
    }

    public Integer getId() {
        return id;
    }

    public Integer getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getsci_Name() {
        return sci_Name;
    }
}
