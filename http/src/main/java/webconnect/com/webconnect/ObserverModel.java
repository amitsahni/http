package webconnect.com.webconnect;

/**
 * Created by clickapps on 8/3/18.
 */

public class ObserverModel {
    private Object model;
    private int type;

    public int getType() {
        return type;
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public void setType(int type) {
        this.type = type;
    }
}
