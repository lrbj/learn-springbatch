package hello;

import java.math.BigInteger;

/**
 * @Author: Kayla,Ye
 * @Description:
 * @Date:Created in 4:32 PM 7/25/2018
 */
public class Person {

    private  BigInteger peopleId;

    private String lastName;
    private String firstName;

    public Person() {
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "firstName: " + firstName + ", lastName: " + lastName;
    }
}
