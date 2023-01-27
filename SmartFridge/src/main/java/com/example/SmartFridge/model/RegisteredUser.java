package com.example.SmartFridge.model;

import com.example.SmartFridge.DTO.userDTO;

import java.time.LocalDate;
import java.util.List;

public class RegisteredUser extends User{
        Fridge fridge;
        List<Review> reviews;

        // constructor
    /*
        public RegisteredUser(String id)
        {
            super.setId(id);
        }

        public RegisteredUser(String id, String username, String password, String email, LocalDate registrationDate)
        {
            super.setId(id);
            super.setUsername(username);
            super.setPassword(password);
            super.setRegistrationDate(registrationDate);

        }
*/
    public RegisteredUser(String id, String username,String password, String firstName, String lastName, String country, String email,LocalDate regdate)
        {
            super.setId(id);
            super.setUsername(username);
            super.setLastName(lastName);
            super.setFirstName(firstName);
            super.setCountry(country);
            super.setEmail(email);
            super.setPassword(password);
            super.setRegistrationDate(regdate);
        }

    public RegisteredUser(String id, String username) {
        super.setId(id);
        super.setUsername(username);
    }

    public RegisteredUser(userDTO selectedItem) {
        super.setId(selectedItem.getId());
        super.setUsername(selectedItem.getUsername());
        super.setLastName(selectedItem.getSurname());
        super.setFirstName(selectedItem.getName());
        super.setCountry(selectedItem.getCountry());
        super.setEmail(selectedItem.getCountry());
    }

    public Fridge getFridge() {
            return fridge;
        }
        public void setFridge(Fridge fridge)
        {
            this.fridge = fridge;
        }

        public List<Review> getReviews() {
            return reviews;
        }
        public void setReviews(List<Review> reviews) {
            this.reviews = reviews;
        }
}

