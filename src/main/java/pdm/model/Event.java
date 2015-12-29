package pdm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@XmlRootElement
@Table(name = "Event", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Event implements Serializable {
	private static final long serialVersionUID = 7949445533038467323L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

	@NotEmpty
	@NotNull
    @Size(min = 1, max = 20, message = "1-20 letters and spaces")
    private String name;
    
    @NotEmpty
    @NotNull
    @Size(min = 1, max = 50, message = "1-50 letters and spaces")
    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    private String address;
    
    @NotEmpty
    @NotNull
    @Size(min = 1, max = 20, message = "1-20 letters and spaces")
    private String timeEvent;

    @NotEmpty
    @NotNull
    @Size(min = 1, max = 25, message = "1-25 letters and spaces")
    private String website;
    
    
    @NotEmpty
    @NotNull
    @Size(min = 1, max = 250, message = "1-250 letters and spaces")
    private String description;
    
    @OneToOne
    private Member annoucer;
    
    @OneToOne
    private Category category;
    
    @OneToOne
    private Organization organization;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTimeEvent() {
		return timeEvent;
	}

	public void setTimeEvent(String timeEvent) {
		this.timeEvent = timeEvent;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Member getAnnoucer() {
		return annoucer;
	}

	public void setAnnoucer(Member annoucer) {
		this.annoucer = annoucer;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
}
