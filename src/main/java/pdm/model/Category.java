package pdm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name = "Category", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Category implements Serializable {
	private static final long serialVersionUID = 2430432548082388856L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
	@NotNull
    @Size(min = 1, max = 20, message = "1-20 letters and spaces")
    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    private String name;

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
}
