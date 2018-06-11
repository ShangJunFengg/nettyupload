import java.io.Serializable;

public class DataBean implements Serializable {
    private static final long serialVersionUID = 3534558296445562116L;
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
