package nl.kabisa.service.quotes.database.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import nl.kabisa.service.quotes.database.model.enums.RatingEnum;

/**
 * Lombok issues on JPA entities:
 * https://dzone.com/articles/lombok-and-jpa-what-may-go-wrong
 *
 * lombok @ToString may be used but @ToString.Exclude must be set on properties with (default) FetchType. LAZY
 *                  to avoid that they do get loaded by the toString method.
 * Lombok @Data must not be used because it will generate a Hashcode method which is not reliable since
 *              JPA entities are mutable, it can also cause LazyInitializationException outside a transaction.
 * Lombok @NoArgsConstructor must be provided when using @Builder or @AllArgsConstructor
 * Lombok @EqualsAndHashCode must not be used, instead use the default java Object implementations.
 *
 */
@Entity(name = "Rating")
@Table(name = "RATING", uniqueConstraints = { @UniqueConstraint(name = "UniqueUserIdAndQuote", columnNames = { "user_id", "quote_id" }) })
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = RatingEntity.class)
public class RatingEntity implements Versioned {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "rating_generator")
    @SequenceGenerator(name="rating_generator", sequenceName = "rating_sequence", allocationSize = 50)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "user_id", length=25, nullable=false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rating", length=255, nullable=false)
    private RatingEnum rating;

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="quote_id", referencedColumnName = "id", nullable = false)
    @Setter(AccessLevel.NONE)
    private QuoteEntity quote;

     public void setQuote(QuoteEntity quote) {
        this.quote = quote;
        if(quote != null) {
            quote.addRating(this);
        }
    }

    @PreRemove
    private void preDeleteRating() {
        if(quote != null) {
            quote.removeRating(this);
        }
    }

    @Override
    public boolean validateVersion(Long version) {
        if(this.version == null) {
            return true;
        }

        if (this.getVersion().equals(version)) {
            return true;
        }

        return false;
    }
}
