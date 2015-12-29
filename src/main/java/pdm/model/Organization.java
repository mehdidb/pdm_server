package pdm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@XmlRootElement
@Table(name = "Organization", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Organization implements Serializable {
	private static final long serialVersionUID = 186057704784845316L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 25, message = "1-25 letters and spaces")
    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    private String name;
    
    @NotNull
    @Size(min = 1, max = 50, message = "1-50 letters and spaces")
    private String address;

    @NotNull
    @Size(min = 8, max = 8, message = "8 Numbers")
    @Digits(fraction = 0, integer = 8, message = "Not valid")
    private String phoneNumber;
    
    @NotNull
    @NotEmpty
    @Email(message = "Invalid format")
    private String email;
    
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 25, message = "1-25 letters and spaces")
    private String website;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
