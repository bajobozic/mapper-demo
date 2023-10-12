package rs.bajobozic.mapperdemo.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;
    @Column(nullable = false, unique = true, name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;
    @Column(nullable = false)
    private String department;
    @Column(nullable = false, name = "created_at")
    private LocalDate createdAt;
    @Column(name = "customer_items")
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerItem> customerItems = new ArrayList<CustomerItem>();

    // this is needed for correct saving customer into DB when using JPA
    // it's neccessary to do it manualy
    public void addCustomerItem(CustomerItem item) {
        if (customerItems == null)
            customerItems = new ArrayList<>();
        this.customerItems.add(item);
        item.setCustomer(this);
    }

    public void removeCustomerItem(CustomerItem item) {
         item.setCustomer(null);
        if (customerItems == null || customerItems.isEmpty())
            return;
        this.customerItems.remove(item);
    }

    // this is needed for correct saving customer into DB when using JPA
    // it's neccessary to do it manualy
    public void addAddress(Address address) {
        this.address = address;
        address.setCustomer(this);
    }

    public void removeAddress(Address address) {
        address.setCustomer(null);
        this.address=null;
    }
}
