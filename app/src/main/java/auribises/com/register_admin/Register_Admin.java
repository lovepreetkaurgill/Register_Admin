package auribises.com.register_admin;

import java.io.Serializable;


// POJO or Bean or Model
public class Register_Admin implements Serializable{

    // Attributes
    int id;
    String name,phone,email,birth_date,gender,address,qualification,experience;

    //Constructors
    public Register_Admin() {
    }

    public Register_Admin(int id, String name, String phone, String email, String birth_date, String gender, String address, String qualification, String experience) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.birth_date = birth_date;
        this.gender = gender;
        this.address = address;
        this.qualification = qualification;
        this.experience = experience;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    @Override
    public String toString() {
        return "Details of Register_Admin\n" +
                "\nID is: " + id +
                "\nName is: " + name +
                "\nPhone is: " + phone +
                "\nEmail is: " + email +
                "\nBirth_date is: " + birth_date +
                "\nGender is: " + gender +
                "\nAddress is: " + address +
                "\nQualification is: " + qualification +
                "\nExperience is: " + experience ;
    }
}
