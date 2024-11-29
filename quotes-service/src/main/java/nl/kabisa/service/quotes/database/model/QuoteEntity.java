package nl.kabisa.service.quotes.database.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Lombok issues on JPA entities:
 * https://dzone.com/articles/lombok-and-jpa-what-may-go-wrong
 *
 * Lombok @ToString may be used but @ToString.Exclude must be set on properties with (default) FetchType LAZY
 *          to avoid that they do get loaded by the toString method.
 * Lombok @Data must not be used because it will generate a Hashcode method which is not reliable since
 *          JPA entities are mutable, it can also cause LazyInitializationException outside a transaction.
 * Lombok @NoArgsConstructor must be provided when using @Builder or @AllArgsConstructor
 * Lombok @EqualsAndHashCode must not be used, instead use the default java Object implementations.
 *
 */
@Entity(name = "Quote")
@Table(name = "QUOTE")
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuoteEntity implements Versioned {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "quote_generator")
    @SequenceGenerator(name="quote_generator", sequenceName = "quote_sequence", allocationSize = 50)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "text", length=255, nullable=false)
    private String text;

    @Column(name = "author", length=255, nullable=false)
    private String author;

    @Builder.Default
    @Column(name = "creation_date", columnDefinition = "DATE", nullable = false)
    private LocalDate creationDate = LocalDate.now();

    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Builder.Default
    @OneToMany(mappedBy="quote", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    private List<RatingEntity> ratings = new ArrayList<>();

    public void addRating(RatingEntity rating) {
        if(rating != null && !ratings.contains(rating)) {
            ratings.add(rating);
            rating.setQuote(this);
        }
    }

    public void removeRating(RatingEntity ratingEntity) {
        if (this.ratings != null && this.ratings.contains(ratingEntity)) {
            this.ratings.remove(ratingEntity);
        }
    }

    @PreRemove
    private void preDeleteQuote() {
        if (!ratings.isEmpty()) {
            ratings.forEach(rating -> rating.setQuote(null)); // SYNCHRONIZING THE OTHER SIDE OF RELATIONSHIP
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
