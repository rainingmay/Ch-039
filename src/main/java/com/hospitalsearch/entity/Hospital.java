package com.hospitalsearch.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * 
 * @author Oleksandr Mukonin
 *
 */
@Entity
@Table(name = "hospital")

@NamedQueries
(
		{
			@NamedQuery(name = Hospital.DELETE_HOSPITAL_BY_ID, query = Hospital.DELETE_HOSPITAL_BY_ID_QUERY),
			@NamedQuery(name = Hospital.GET_LIST_BY_BOUNDS, query = Hospital.GET_LIST_BY_BOUNDS_QUERY)
		}
		)
public class Hospital{

	static final String GET_LIST_BY_BOUNDS_QUERY = "from Hospital h where "
			+ "(latitude < :nelat) and "
			+ "(latitude > :swlat) and "
			+ "(longitude < :nelng) and "
			+ "(longitude > :swlng)";
	public static final String GET_LIST_BY_BOUNDS = "GET_LIST_BY_BOUNDS";

	static final String DELETE_HOSPITAL_BY_ID_QUERY = "DELETE Hospital WHERE id = :id";
	public static final String DELETE_HOSPITAL_BY_ID = "DELETE_HOSPITAL_BY_ID";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hospital_gen")
	@SequenceGenerator(name = "hospital_gen", sequenceName = "hospital_id_seq")
	private Long id;

	@NotEmpty
	@Size(min = 10, max = 50)
	@Column(name = "NAME", nullable = false)
	private String name;

	@NotNull
	@Min(-90)
	@Max(90)
	@Column(name = "LATITUDE", nullable = false)
	private Double latitude;

	@NotNull
	@Min(-180)
	@Max(180)
	@Column(name = "LONGITUDE", nullable = false)
	private Double longitude;

	@NotEmpty
	@Size(min = 10, max = 100)
	@Column(name = "ADDRESS", nullable = false)
	private String address; 


	@Size(max = 150)
	@Column(name = "DESCRIPTION", nullable = false)
	private String description; 

	private String imagePath;

	@Column(name = "managers")
	@ManyToMany
	private List<User> managers;

	public List<User> getManagers() {
		return managers;
	}

	public void setManagers(List<User> managers) {
		this.managers = managers;
	}

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

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}
