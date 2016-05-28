package com.hospitalsearch.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "patientcard")
public class PatientCard {

    @Id
<<<<<<< HEAD
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patientcard_gen")
	@SequenceGenerator(name = "patientcard_gen", sequenceName = "patientcard_id_seq")
=======
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patientcard_gen")
    @SequenceGenerator(name = "patientcard_gen", sequenceName = "patientcard_id_seq")
>>>>>>> 8b959c5fd5c98511995338fa6ecad026a0900ed8
    private Long id;

    @OneToMany(mappedBy = "patientCard", fetch = FetchType.EAGER)
    List<CardItem> cardItems;
    @OneToOne
    UserDetail userDetail;

    public PatientCard() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CardItem> getCardItems() {
        return cardItems;
    }

    public void setCardItems(List<CardItem> cardItems) {
        this.cardItems = cardItems;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    @Override
    public String toString() {
        return "PatientCard{" +
                "id=" + id +
                '}';
    }
	   
}
