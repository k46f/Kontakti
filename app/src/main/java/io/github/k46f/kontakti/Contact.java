package io.github.k46f.kontakti;

public class Contact {
    String name;
    String phone;
    String address;
    String email;
    String facebook;
    String birthday;
    String location;
    String photo;

    public Contact() {
    }

    public Contact(String name, String phone, String address, String email, String facebook, String birthday, String location, String photo) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.facebook = facebook;
        this.birthday = birthday;
        this.location = location;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
