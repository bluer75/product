package db.test.app.product;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Product entity.
 * Field id has auto-generated value that cannot be set externally.
 * Field creationData is set automatically to current date when entity is created and cannot be changed.
 */
@ApiModel(description = "Contains attributes of the product")
@Entity
public class Product {

    @ApiModelProperty(notes = "unique identifier of the product (auto-generated read only value)")
    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ApiModelProperty(notes = "name of the product (mandatory)")
    @NotBlank(message = "name of the product is mandatory")
    private String name;

    @ApiModelProperty(notes = "price of the product (mandatory positive value)")
    @Positive(message = "price of the product has to be a positive value")
    private double price;

    @ApiModelProperty(notes = "date when the product was created (auto-generated read only value)")
    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate creationDate;

    /**
     * Internal field indicating whether entity has been deleted.
     */
    @JsonIgnore
    private boolean deleted;

    /**
     * Gets the id.
     */
    public int getId() {

        return id;
    }

    /**
     * Sets the id.
     * Intentionally package access.
     */
    void setId(int id) {

        this.id = id;
    }

    /**
     * Gets the name.
     */
    public String getName() {

        return name;
    }

    /**
     * Sets the name.
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Gets the price.
     */
    public double getPrice() {

        return price;
    }

    /**
     * Sets the price.
     */
    public void setPrice(double price) {

        this.price = price;
    }

    /**
     * Gets the creationDate.
     */
    public LocalDate getCreationDate() {

        return creationDate;
    }

    /**
     * Sets the creationDate.
     * Intentionally package access.
     */
    void setCreationDate(LocalDate creationDate) {

        this.creationDate = creationDate;
    }

    /**
     * Gets the deleted.
     */
    public boolean isDeleted() {

        return deleted;
    }

    /**
     * Marks entity as deleted.
     */
    public void markAsDeleted() {

        this.deleted = true;
    }

    // generated methods

    @Override
    public String toString() {

        return "Product [id=" + id + ", " + (name != null ? "name=" + name + ", " : "") + "price="
                + price + ", " + (creationDate != null ? "creationDate=" + creationDate + ", " : "")
                + "deleted=" + deleted + "]";
    }

    @Override
    public int hashCode() {

        return Objects.hash(creationDate, deleted, id, name, price);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Product other = (Product) obj;
        return Objects.equals(creationDate, other.creationDate) && deleted == other.deleted
                && id == other.id && Objects.equals(name, other.name)
                && Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price);
    }

}
