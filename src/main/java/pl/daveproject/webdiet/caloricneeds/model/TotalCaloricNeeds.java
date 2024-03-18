package pl.daveproject.webdiet.caloricneeds.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.daveproject.webdiet.bmi.model.UnitSystem;
import pl.daveproject.webdiet.security.model.ApplicationUser;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TotalCaloricNeeds {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private double weight;

    private double height;

    private double value;

    private LocalDate addedDate;

    @Enumerated(EnumType.STRING)
    private UnitSystem unit;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    private int age;

    @ManyToOne(cascade = CascadeType.MERGE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ApplicationUser applicationUser;
}
