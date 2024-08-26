
package View;

public class Detail {
    private String user;
    private String name;
    
    public Detail(){
        user="cuahangdienthoai";
        name="Cửa hàng Điện Thoại";
    }
    
    public Detail(String us, String na){
        this.user=us;
        this.name=na;
    }

    public Detail(Detail detail){
        this.user=detail.user;
        this.name=detail.name;
    }
    
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
